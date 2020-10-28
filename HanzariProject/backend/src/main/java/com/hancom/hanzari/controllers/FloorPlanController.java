package com.hancom.hanzari.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hancom.hanzari.model.FloorPlan;
import com.hancom.hanzari.service.FloorPlanService;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

// CORS 오류 해결하기 위한 어노테이션
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/images")
public class FloorPlanController {
	
	@Autowired
	private FloorPlanService floorPlanService;
	
	@Autowired
	private MinioClient minioClient;
	
	@Autowired
	private static SessionFactory sessionFactory;
	
	// 버킷명(Amazon S3 Bucket policy를 지켜야 한다.)
	String bucketName = "hanzari";
	
	//API가 완성되면 다시 수정하기
	//이미지 파일 MinIO 서버에 업로드
	//IOException은 imagePutInputStream의 예외 상황 처리를 위해서이다.
	@PostMapping(value="buildings/{building_id}/floors/{floor_id}")
	public boolean putImageFile(@PathVariable("building_id") String buildingId, @PathVariable("floor_id") String floorId, @RequestParam("imageFile") MultipartFile file) throws IOException {
		boolean result = false;
		String floorPlanId = buildingId + "-" + floorId;
		FloorPlan putfloorPlan = FloorPlan.builder().buildingId(buildingId)
				.floorId(floorId).floorPlanId(floorPlanId).build();
		InputStream imagePutInputStream = file.getInputStream();
		
		floorPlanService.save(putfloorPlan);
		
		//System.out.println("getOriginalFilename : "+ file.getOriginalFilename());
		//Path path = Path.of(file.getOriginalFilename());
		try {
			//object 속성이 MinIO 버킷에 저장되는 파일 이름이된다.
			//stream 속성은 이미지 사이즈 크기 만큼 메모리를 사용하여 파일을 전송한다.	
			minioClient.putObject(
				    PutObjectArgs.builder()
				    .bucket(bucketName)
				    .object(floorPlanId)//file.getOriginalFilename())
				    .stream(imagePutInputStream, imagePutInputStream.available(), -1)
				    .contentType(file.getContentType())
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
	@GetMapping(value="buildings/{building_id}/floors/{floor_id}")
	public boolean getImageFile(@PathVariable("building_id") String buildingId, @PathVariable("floor_id") String floorId,  HttpServletResponse response) throws IOException {
		// 이미지 파일이 잘 전송되었는지 boolean 값으로 알려주기 위한 필드
		boolean result = false;
		InputStream imageGetInputStream = null;
		String floorPlanId = null;
		
		//System.out.println("object : "+ object);
		FloorPlan getFloorPlan;
		try {
			getFloorPlan = floorPlanService.findByBuildingIdAndFloorId(buildingId, floorId);
			floorPlanId = getFloorPlan.getFloorPlanId();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			imageGetInputStream = minioClient.getObject(
					 GetObjectArgs.builder()
					 .bucket(bucketName)
					 .object(floorPlanId)
					 .build());
			//InputStreamResource inputStreamResource = new InputStreamResource(imageGetInputStream);
			response.addHeader("Content-disposition", floorPlanId);
			response.setContentType(URLConnection.guessContentTypeFromName(floorPlanId));
			IOUtils.copy(imageGetInputStream, response.getOutputStream());
			response.flushBuffer();
			result = true;
		} catch(Exception e) {
			System.out.println("Error occurred: " + e);
		}
		return result;
	}
}