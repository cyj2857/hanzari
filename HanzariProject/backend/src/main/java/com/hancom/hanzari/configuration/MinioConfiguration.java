package com.hancom.hanzari.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
@Configuration
public class MinioConfiguration {
	@Bean(name="minioClient")
	public MinioClient minioClient() throws InvalidEndpointException, InvalidPortException {
		// 공식 MinioClient 클래스 사용하기
		return MinioClient.builder().endpoint("http://172.30.1.56:9000").credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY").build();
	}
}