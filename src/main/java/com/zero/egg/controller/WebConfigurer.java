package com.zero.egg.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfigurer implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 设置允许跨域的路径
		registry.addMapping("/**")
				// 设置允许跨域请求的域名
				.allowedOrigins("*")
				// 是否允许证书 不再默认开启
				.allowCredentials(true)
				// 设置允许的方法
				.allowedMethods("*")
				// 跨域允许时间
				.maxAge(3600);
	}

	/**
	 * 添加静态资源--过滤swagger-api (开源的在线API文档)
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 过滤swagger
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		registry.addResourceHandler("/swagger-resources/**")
				.addResourceLocations("classpath:/META-INF/resources/swagger-resources/");

		registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/META-INF/resources/swagger*");

		registry.addResourceHandler("/v2/api-docs/**")
				.addResourceLocations("classpath:/META-INF/resources/v2/api-docs/");

	}
}
