package com.hancom.hanzari.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlefebure.spring.boot.minio.MinioException;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/images")
public class TestImageController {

	// MinioClient 클래스 사용하기
	@GetMapping("/{object}")
	// 한글과컴퓨터-8층.png.png
	public void getObject(@PathVariable("object") String object, HttpServletResponse response)
			throws MinioException, IOException {
//		InputStream inputStream = minioClient.get(Path.of(object));
//		InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
//
//		// Set the content type and attachment header.
//		response.addHeader("Content-disposition", "attachment;filename=" + object);
//		response.setContentType(URLConnection.guessContentTypeFromName(object));
//
//		// Copy the stream to the response's output stream.
//		IOUtils.copy(inputStream, response.getOutputStream());
//		response.flushBuffer();
	}

}