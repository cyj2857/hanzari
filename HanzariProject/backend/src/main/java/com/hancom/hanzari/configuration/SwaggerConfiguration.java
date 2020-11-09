package com.hancom.hanzari.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.hancom.hanzari")).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Hanzari REST API Doc.").version("1.0")
				.description("한글과컴퓨터 일학습병행제 참여 학생들이 제작한 웹기반 자리배치프로그램 Hanzari의 REST API Doc.입니다.").license("라이센스 작성")
				.licenseUrl("라이센스 URL 작성").build();
	}

}