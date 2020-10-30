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

import com.hancom.hanzari.model.FloorPlan;
import com.hancom.hanzari.service.FloorPlanService;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;


@CrossOrigin(origins = "*", maxAge = 3600) // CORS 오류 해결하기 위한 어노테이션
@RestController
@RequestMapping("api/buildings/{building_id}/floors/{floor_id}/images")
public class FloorPlanController {
	
	@Autowired
	private FloorPlanService floorPlanService;
	
	@Autowired
	private MinioClient minioClient;
	
	// 버킷명(Amazon S3 Bucket policy를 지켜야 한다.)
	String bucketName = "hanzari";
	
	//이미지 파일 MinIO 서버에 업로드
	//IOException은 imagePutInputStream의 예외 상황 처리를 위해서이다.
	//TODO 현재는 HTTP 통신의 결과값을 클라이언트에게 보내주지는 않지만(리턴타입 void) 백엔드단에서 요청 처리가 어떻게 되었는지를 알려주기 위해 메세지를 보내주어도 좋다. 예를 들어 "SUCCESS", "FAILURE" 등의 메세지를 JSON 스트럭쳐 형태로 리턴해준다.
	@PostMapping
	public void putImageFile(@PathVariable("building_id") String buildingId, @PathVariable("floor_id") String floorId, @RequestParam("imageFile") MultipartFile file) throws IOException {
		String floorPlanId = buildingId + "-" + floorId;
		FloorPlan putfloorPlan = FloorPlan.builder().buildingId(buildingId)
				.floorId(floorId).floorPlanId(floorPlanId).build();
		InputStream imagePutInputStream = file.getInputStream();
		
		floorPlanService.save(putfloorPlan);
		
		try {
			//object 속성이 MinIO 버킷에 저장되는 파일 이름이된다.
			//stream 속성은 이미지 사이즈 크기 만큼 메모리를 사용하여 파일을 전송한다.	
			minioClient.putObject(
				    PutObjectArgs.builder()
				    .bucket(bucketName)
				    .object(floorPlanId) //file.getOriginalFilename())
				    .stream(imagePutInputStream, file.getSize() , -1) //Object의 사이즈를 알 경우에는 3번째 인자인 partSize를 자동감지를 위해 -1로 준다.
				    /*TODO getContentType을 사용하면 클라이언트 측에서 이름을 변경하여 보낼 경우 다른 형식으로 업로드 되어 후에 클라이언트에 내려줄 때 명시적 contentType과 실제 데이터의 contentType이 달라 문제가 생길 수 있다.
				    */
				    .contentType(file.getContentType())
				    .build());
		} catch (Exception e) {
			System.out.println("Error occurred: " + e);
		}
		//imagePutInputStream 닫아주기
		//TODO finally에 넣어주는 것이 좋다. 하지만 닫아 주는 경우에도 exception이 발생할 수 있기에 이러한 경우들을 위한 exception 클래스를 만들어서 사용하는 것이 좋다.
		imagePutInputStream.close();
	}

	//이미지 파일 MinIO 서버에서 다운로드
	//MinIO에 저장된 각 파일의 object명으로 찾아야한다.
	//IOException은 imageGetInputStream의 예외 상황 처리를 위해서이다.
	//TODO putImageFile 메소드 상단에 적은 내용 참조
	@GetMapping
	public void getImageFile(@PathVariable("building_id") String buildingId, @PathVariable("floor_id") String floorId,  HttpServletResponse response) throws IOException {
		InputStream imageGetInputStream = null;
		String floorPlanId = null;
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
			response.addHeader("Content-disposition", floorPlanId);
			response.setContentType(URLConnection.guessContentTypeFromName(floorPlanId));
			IOUtils.copy(imageGetInputStream, response.getOutputStream());
			response.flushBuffer();			
		} catch(Exception e) {
			System.out.println("Error occurred: " + e);
		}
		//TODO 동일하게 finally에 넣어주는 것이 좋다. 하지만 닫아 주는 경우에도 exception이 발생할 수 있기에 이러한 경우들을 위한 exception 클래스를 만들어서 사용하는 것이 좋다.
		//imageGetInputStream 닫아주기
		imageGetInputStream.close();
	}
}