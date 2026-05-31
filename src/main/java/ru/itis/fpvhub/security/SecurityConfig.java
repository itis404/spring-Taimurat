package ru.itis.fpvhub.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(
                                "/",
                                "/login",
                                "/registration/**",
                                "/email-verification/**",
                                "/oauth2/github/**",
                                "/system/status",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/error",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/articles", "/api/v1/articles/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/articles").hasAnyRole("WRITER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/articles/*").hasAnyRole("WRITER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/articles/*").hasAnyRole("WRITER", "ADMIN")
                        .requestMatchers("/profile/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/articles/*/comments", "/comments/*/delete").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ajax/articles/*/reaction", "/ajax/comments/*/delete").authenticated()
                        .requestMatchers("/my/articles").hasAnyRole("WRITER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/articles/new", "/articles/*/edit").hasAnyRole("WRITER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/articles", "/articles/*/edit", "/articles/*/delete").hasAnyRole("WRITER", "ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("usernameOrEmail")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"status\":401,\"error\":\"UNAUTHORIZED\",\"message\":\"Authentication is required\"}");
                        }, new AntPathRequestMatcher("/api/**"))
                        .defaultAccessDeniedHandlerFor((request, response, exception) -> {
                            response.setStatus(403);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"status\":403,\"error\":\"FORBIDDEN\",\"message\":\"Access denied\"}");
                        }, new AntPathRequestMatcher("/api/**"))
                        .accessDeniedPage("/error/403")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
