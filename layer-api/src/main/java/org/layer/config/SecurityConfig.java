package org.layer.config;


import lombok.RequiredArgsConstructor;
import org.layer.auth.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
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
                                .anyRequest().authenticated()
                );
    }

    // swagger
    private void permitSwaggerUri(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/docs/**")).permitAll());
    }



}
