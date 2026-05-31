package ru.itis.fpvhub.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.fpvhub.client.dto.EmailValidationResult;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

@Component
public class EmailValidationApiClient {

    private static final Logger log = LoggerFactory.getLogger(EmailValidationApiClient.class);
    private static final String STATUS_DELIVERABLE = "deliverable";
    private static final String STATUS_DETAIL_VALID_EMAIL = "valid_email";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiUrl;
    private final String apiKey;

    public EmailValidationApiClient(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            @Value("${app.email-validation.api-url:https://emailreputation.abstractapi.com/v1/}") String apiUrl,
            @Value("${app.email-validation.api-key:}") String apiKey,
            @Value("${app.email-validation.proxy-host:}") String proxyHost,
            @Value("${app.email-validation.proxy-port:0}") int proxyPort
    ) {
        this.restClient = buildRestClient(restClientBuilder, proxyHost, proxyPort);
        this.objectMapper = objectMapper;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public EmailValidationResult validate(String email) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Email validation API key is not configured");
        }

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("api_key", apiKey)
                .queryParam("email", email)
                .build()
                .toUriString();

        String response = restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode deliverabilityNode = root.path("email_deliverability");
            JsonNode qualityNode = root.path("email_quality");
            JsonNode riskNode = root.path("email_risk");

            boolean validFormat = deliverabilityNode.path("is_format_valid").asBoolean(false);
            boolean mxFound = deliverabilityNode.path("is_mx_valid").asBoolean(false);
            boolean smtpValid = deliverabilityNode.path("is_smtp_valid").asBoolean(false);
            boolean disposable = qualityNode.path("is_disposable").asBoolean(false);

            String deliverability = deliverabilityNode.path("status").asText("unknown");
            String statusDetail = deliverabilityNode.path("status_detail").asText("unknown");
            double qualityScore = qualityNode.path("score").asDouble(0.0);
            String addressRisk = riskNode.path("address_risk_status").asText("unknown");
            String domainRisk = riskNode.path("domain_risk_status").asText("unknown");

            log.debug(
                    "Email reputation raw result: email={}, deliverability={}, statusDetail={}, qualityScore={}, addressRisk={}, domainRisk={}",
                    email, deliverability, statusDetail, qualityScore, addressRisk, domainRisk
            );

            if (!validFormat) {
                return invalid("Некорректный формат email", deliverability, disposable, mxFound, validFormat, response);
            }
            if (disposable) {
                return invalid("Одноразовые email-адреса запрещены", deliverability, true, mxFound, validFormat, response);
            }
            if (!mxFound) {
                return invalid("У домена email не найдены MX-записи", deliverability, disposable, false, validFormat, response);
            }
            if (!smtpValid) {
                return invalid("Email не проходит SMTP-проверку", deliverability, disposable, mxFound, validFormat, response);
            }
            if (!STATUS_DELIVERABLE.equalsIgnoreCase(deliverability)) {
                return invalid("Email не считается доставляемым: " + deliverability, deliverability, disposable, mxFound, validFormat, response);
            }
            if (!STATUS_DETAIL_VALID_EMAIL.equalsIgnoreCase(statusDetail)) {
                return invalid("Email не прошёл проверку: " + statusDetail, deliverability, disposable, mxFound, validFormat, response);
            }
            if (qualityScore < 0.50) {
                return invalid("Недостаточный reputation score email: " + qualityScore, deliverability, disposable, mxFound, validFormat, response);
            }
            if ("high".equalsIgnoreCase(addressRisk) || "high".equalsIgnoreCase(domainRisk)) {
                return invalid("Email имеет высокий риск по внешней проверке", deliverability, disposable, mxFound, validFormat, response);
            }

            return new EmailValidationResult(true, "Email прошёл внешнюю проверку", deliverability, false, true, true, response);
        } catch (Exception exception) {
            log.warn("Cannot parse email reputation response for {}", email, exception);
            throw new IllegalStateException("Cannot parse email reputation API response", exception);
        }
    }

    private RestClient buildRestClient(RestClient.Builder restClientBuilder, String proxyHost, int proxyPort) {
        if (!StringUtils.hasText(proxyHost) || proxyPort <= 0) {
            return restClientBuilder.build();
        }

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(20));
        requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));

        return restClientBuilder
                .requestFactory(requestFactory)
                .build();
    }

    private EmailValidationResult invalid(
            String message,
            String deliverability,
            boolean disposable,
            boolean mxFound,
            boolean validFormat,
            String rawResponse
    ) {
        return new EmailValidationResult(false, message, deliverability, disposable, mxFound, validFormat, rawResponse);
    }
}
