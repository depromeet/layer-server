package org.layer.admin.config;

import org.layer.admin.interceptor.AdminAuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

	private final AdminAuthorizationInterceptor adminInterceptor;

	public AdminWebConfig(AdminAuthorizationInterceptor adminInterceptor) {
		this.adminInterceptor = adminInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminInterceptor)
			.addPathPatterns("/admin/**"); // /admin 으로 시작하는 모든 경로에 적용
	}
}
