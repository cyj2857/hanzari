package com.hancom.hanzari.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;

import io.minio.MinioClient;
import io.minio.messages.Item;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/images")
public class TestImageController {

	// MinioClient 클래스 사용하기
	@Autowired
	private MinioClient minioClient;


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