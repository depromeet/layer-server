package org.layer.config;

import lombok.RequiredArgsConstructor;

import org.layer.domain.jwt.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
					.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
					.requestMatchers(new AntPathRequestMatcher("/actuator/health")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/admin/**")).permitAll()
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
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = buildCorsConfig(); // 외부에서 주입된 allowedOrigins 사용

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config); // 모든 경로에 적용
		return source;
	}

	private CorsConfiguration buildCorsConfig() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(List.of(
			"http://localhost:8080",
			"https://api.layerapp.io",
			"https://layerapp.io",
			"https://www.layerapp.io",
			"http://localhost:5173",
			"https://stg.layerapp.io",
			"https://stgapi.layerapp.io"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		config.setAllowCredentials(false);
		return config;
	}

}
