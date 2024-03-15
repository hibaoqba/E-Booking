package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private static final String[] SWAGGER_WHITELIST = {
                        "/api/v1/auth/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
        };

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                // disable csrf
                http.csrf(csrf -> csrf.disable());

                // Enable CORS
                http.cors(cors -> cors.configurationSource(request -> {
                        CorsConfiguration config = new CorsConfiguration();

                        config.setAllowedOrigins(
                                        Arrays.asList("*")); // Arrays.asList("http://localhost:8080/"));

                        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        config.setAllowedHeaders(
                                        Arrays.asList("authorization", "content-type", "x-auth-token", "api_key"));
                        config.setExposedHeaders(Arrays.asList("x-auth-token"));
                        return config;
                }));

                // requests authorization
                http.authorizeHttpRequests(authz -> authz
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/api/cars/**").permitAll()
                                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                                .anyRequest().authenticated());


                return http.build();

        }

}
