package com.hancom.hanzari.controllers;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jlefebure.spring.boot.minio.MinioService;

import io.minio.MinioClient;
import io.minio.errors.MinioException;

@CrossOrigin(origins = "*", maxAge = 3600) // CORS 오류 해결하기 위한 어노테이션
@RestController
@RequestMapping("api/images")
public class TestImageController {

	// @Autowired
	private MinioClient minioClient;

	// @Autowired
	private MinioService minioService;
	// 버킷명
	String bucketName = "hanzari";

	/*
	 * @GetMapping("/{}") public List<Item> getAllImageFiles(){
	 * Iterable<Result<Item>> allImageFiles = minioClient.listObjects("hanzari");
	 * return }
	 */

	// 이미지 파일 MinIO 서버에 업로드
//	@PostMapping
//	public boolean putImageFile(@RequestParam("iamgeFile") MultipartFile file) {
//		boolean result = false;
//
//		// RequestParam으로 받은 이미지 파일 이름 알아내기
//		Path path = Path.of(file.getOriginalFilename());
//
//		try {
//			minioClient = new MinioClient("http://172.30.1.56:9000", "AKIAIOSFODNN7EXAMPLE",
//					"wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
//			// 버킷이 있는지 먼저 검사해주기
//			boolean found = minioClient.bucketExists(bucketName);
//			if (found) {
//				minioClient.putObject(bucketName, file.getOriginalFilename(), file.getInputStream(), file.getSize(),
//						null, null, file.getContentType());
//				result = true;
//			}
//		} catch (Exception e) {
//			System.out.println("Error occurred: " + e);
//		}
//		return result;
//	}

//	@GetMapping
//	public List<Item> testMinio() throws MinioException {
//		return minioService.list();
//	}
//
//	@GetMapping("/{object}") //한글과컴퓨터-8층.png.png public void
//	  getObject(@PathVariable("object") String object, HttpServletResponse
//	  response) throws MinioException, IOException { InputStream inputStream =
//	  minioService.get(Path.of(object)); InputStreamResource inputStreamResource =
//	  new InputStreamResource(inputStream);
//	  
//	  // Set the content type and attachment header.
//	  response.addHeader("Content-disposition", "attachment;filename=" + object);
//	  response.setContentType(URLConnection.guessContentTypeFromName(object));
//	  
//	  // Copy the stream to the response's output stream. IOUtils.copy(inputStream,
//	response.getOutputStream()); response.flushBuffer(); }

	@PostMapping
	public void addAttachement(@RequestParam("imageFile") MultipartFile file)
			throws com.jlefebure.spring.boot.minio.MinioException, MinioException {
		Path path = Path.of(file.getOriginalFilename());
		try {
			minioService.upload(path, file.getInputStream(), file.getContentType());
		} catch (IOException e) {
			throw new IllegalStateException("The file cannot be read", e);
		}
	}

}