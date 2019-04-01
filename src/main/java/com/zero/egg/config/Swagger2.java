package com.zero.egg.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.models.Contact;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName Swagger2
 * @Description Swagger配置
 * @Author lyming
 * @Date 2018/11/11 10:16 AM
 **/
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", value = {"enable"}, havingValue = "true")
public class Swagger2 {

	   @Bean
	    public Docket platformApi() {

	        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).forCodeGeneration(true)
	                .select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
	                .apis(RequestHandlerSelectors.any())
	                .paths(PathSelectors.regex("^.*(?<!auth)$"))
	                .build()
	                .securitySchemes(securitySchemes())
	                .securityContexts(securityContexts());


	    }
	    private List<ApiKey> securitySchemes() {
	        List<ApiKey> apiKeyList= new ArrayList<ApiKey>();
	        apiKeyList.add(new ApiKey("x-auth-token", "x-auth-token", "header"));
	        return apiKeyList;
	    }

	    private List<SecurityContext> securityContexts() {
	        List<SecurityContext> securityContexts=new ArrayList<>();
	        securityContexts.add(
	                SecurityContext.builder()
	                        .securityReferences(defaultAuth())
	                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
	                        .build());
	        return securityContexts;
	    }

	    List<SecurityReference> defaultAuth() {
	        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	        authorizationScopes[0] = authorizationScope;
	        List<SecurityReference> securityReferences=new ArrayList<>();
	        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
	        return securityReferences;
	    }
	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder().title("前后端测试API").description("©2018 Copyright. Powered By starmark.")
	                // .termsOfServiceUrl("")
	        		.contact("zero")
	                .version("2.0").build();
	    }

}
