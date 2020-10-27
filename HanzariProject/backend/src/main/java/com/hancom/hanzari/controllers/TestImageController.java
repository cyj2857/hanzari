package com.hancom.hanzari.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

// CORS 오류 해결하기 위한 어노테이션
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/images")
public class TestImageController {

	@Autowired
	private MinioClient minioClient;
	
	// 버킷명(Amazon S3 Bucket policy를 지켜야 한다.)
	String bucketName = "hanzari";

	//이미지 파일 MinIO 서버에 업로드
	//IOException은 imagePutInputStream의 예외 상황 처리를 위해서이다.
	@PostMapping
	public boolean putImageFile(@RequestParam("iamgeFile") MultipartFile file) throws IOException {
		// 이미지 파일이 잘 저장되었는지 아닌지를 boolean 값으로 알려주기 위한 필드
		boolean result = false;
		InputStream imagePutInputStream = file.getInputStream();
		//System.out.println("getOriginalFilename : "+ file.getOriginalFilename());
		//Path path = Path.of(file.getOriginalFilename());
		try {
			//object 속성이 MinIO 버킷에 저장되는 파일 이름이된다.
			//stream 속성은 이미지 사이즈 크기 만큼 메모리를 사용하여 파일을 전송한다.	
			minioClient.putObject(
				    PutObjectArgs.builder()
				    .bucket(bucketName)
				    .object("hanzariFloor")//file.getOriginalFilename())
				    .stream(imagePutInputStream, imagePutInputStream.available(), -1)
				    .contentType("multipart/form-data")
				    .build());
			result = true;
		} catch (Exception e) {
			System.out.println("Error occurred: " + e);
		}
		//imagePutinputStream 닫아주기
		imagePutInputStream.close();
		return result;
	}

	//이미지 파일 MinIO 서버에서 다운로드
	//MinIO에 저장된 각 파일의 object명으로 찾아야한다.
	//IOException은 imageGetInputStream의 예외 상황 처리를 위해서이다.
	@GetMapping("/{object}")
	public boolean getImageFile(@PathVariable("object") String object, HttpServletResponse response) throws IOException {
		// 이미지 파일이 잘 전송되었는지 boolean 값으로 알려주기 위한 필드
		boolean result = false;
		InputStream imageGetInputStream = null;
		System.out.println("object : "+ object);
		try {
			imageGetInputStream = minioClient.getObject(
					 GetObjectArgs.builder()
					 .bucket(bucketName)
					 .object(object)
					 .build());
			//InputStreamResource inputStreamResource = new InputStreamResource(imageGetInputStream);
			response.addHeader("Content-disposition", object);
			response.setContentType(URLConnection.guessContentTypeFromName(object));
			IOUtils.copy(imageGetInputStream, response.getOutputStream());
			response.flushBuffer();
			result = true;
		} catch(Exception e) {
			System.out.println("Error occurred: " + e);
		}
		return result;
	}
}