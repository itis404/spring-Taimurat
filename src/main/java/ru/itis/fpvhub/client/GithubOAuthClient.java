package ru.itis.fpvhub.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import ru.itis.fpvhub.client.dto.GithubOAuthProfile;

@Component
public class GithubOAuthClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String clientId;
    private final String clientSecret;

    public GithubOAuthClient(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            @Value("${app.oauth.github.client-id:}") String clientId,
            @Value("${app.oauth.github.client-secret:}") String clientSecret
    ) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String exchangeCodeForAccessToken(String code, String redirectUri) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        String response = restClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            if (root.hasNonNull("error")) {
                throw new IllegalStateException("GitHub token endpoint returned error: " + root.path("error_description").asText(root.path("error").asText()));
            }
            String token = root.path("access_token").asText(null);
            if (token == null || token.isBlank()) {
                throw new IllegalStateException("GitHub token endpoint did not return access_token");
            }
            return token;
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot parse GitHub token response", exception);
        }
    }

    public GithubOAuthProfile loadProfile(String accessToken) {
        String userResponse = restClient.get()
                .uri("https://api.github.com/user")
                .headers(headers -> applyBearer(headers, accessToken))
                .retrieve()
                .body(String.class);

        try {
            JsonNode user = objectMapper.readTree(userResponse);
            String id = user.path("id").asText();
            String login = user.path("login").asText();
            String name = user.path("name").asText(null);
            String publicEmail = user.path("email").asText(null);
            String avatarUrl = user.path("avatar_url").asText(null);
            String email = publicEmail == null || publicEmail.isBlank() ? loadPrimaryEmail(accessToken) : publicEmail;
            return new GithubOAuthProfile(id, login, name, email, avatarUrl);
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot parse GitHub user response", exception);
        }
    }

    private String loadPrimaryEmail(String accessToken) {
        String response = restClient.get()
                .uri("https://api.github.com/user/emails")
                .headers(headers -> applyBearer(headers, accessToken))
                .retrieve()
                .body(String.class);

        try {
            JsonNode emails = objectMapper.readTree(response);
            if (!emails.isArray()) {
                return null;
            }
            for (JsonNode email : emails) {
                boolean primary = email.path("primary").asBoolean(false);
                boolean verified = email.path("verified").asBoolean(false);
                if (primary && verified) {
                    return email.path("email").asText(null);
                }
            }
            for (JsonNode email : emails) {
                boolean verified = email.path("verified").asBoolean(false);
                if (verified) {
                    return email.path("email").asText(null);
                }
            }
            return null;
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot parse GitHub emails response", exception);
        }
    }

    private void applyBearer(HttpHeaders headers, String accessToken) {
        headers.setBearerAuth(accessToken);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        headers.add("X-GitHub-Api-Version", "2022-11-28");
    }
}
