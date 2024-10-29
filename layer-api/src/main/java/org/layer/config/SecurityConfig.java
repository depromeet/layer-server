package org.layer.config;


import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import org.layer.common.annotation.DisableSwaggerSecurity;
import org.layer.domain.jwt.JwtAuthenticationFilter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.method.HandlerMethod;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChainDev(HttpSecurity http) throws Exception {
        // exceptionHandling 추가 예정
        permitSwaggerUri(http);
        setHttp(http);
        return http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    private void setHttp(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/sign-in")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/reissue-token")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/sign-up")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/oauth2/google")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/oauth2/kakao")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/space/public/*")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/test")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/external/image/presigned")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/oauth2/apple")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/create-token")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/admin/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/greeting")).permitAll()
                                .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
    }

    // swagger
    private void permitSwaggerUri(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/docs/**")).permitAll());
    }

    // cors
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*")); // 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            DisableSwaggerSecurity methodAnnotation = handlerMethod.getMethodAnnotation(DisableSwaggerSecurity.class);
            if (methodAnnotation != null) {
                operation.setSecurity(Collections.emptyList());
            }
            return operation;
        };
    }
}
