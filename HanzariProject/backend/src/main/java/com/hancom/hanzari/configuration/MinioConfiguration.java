//package com.hancom.hanzari.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import io.minio.MinioClient;
//import io.minio.errors.InvalidEndpointException;
//import io.minio.errors.InvalidPortException;
//
//@Configuration
//public class MinioConfiguration {
//	@Bean(name="minioClient")
//	public MinioClient minioClient() throws InvalidEndpointException, InvalidPortException {
//		// 공식 MinioClient 클래스 사용하기
//		return new MinioClient("http://172.30.1.56:9000", "AKIAIOSFODNN7EXAMPLE",
//				"wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
//		//아래 코드는 builder() 에러가 난다. 구글 검색 결과 lombok과 관련된 에러 같지만 해결을 하지 못했다. 다시 확인을 해봐야겠다.
//		return MinioClient.builder().endpoint("http://172.30.1.56:9000").credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY").build();
//		
//	
//	}
//}