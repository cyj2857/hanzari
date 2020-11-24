package com.hancom.hanzari.configuration;

import java.util.ResourceBundle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

@Configuration
public class MinioConfiguration {
	
	// ResourceBundle을 이용하여 string value들을 불러옴 properties 확장자인데 확장자없이 파일명만 적어줘도
	// ResourceBundle이 알아서 해당 파일 찾아줌
	private final ResourceBundle stringValues = ResourceBundle.getBundle("string");
	
	@Bean(name = "minioClient")
	public MinioClient minioClient() throws InvalidEndpointException, InvalidPortException {
		// 공식 MinioClient 클래스 사용하기
		return MinioClient.builder().endpoint(stringValues.getString("MINIO_SERVER_ADDRESS"))
				.credentials(stringValues.getString("ACCESS_KEY"), stringValues.getString("SECRET_KEY")).build();
	}
}