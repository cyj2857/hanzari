package com.hancom.hanzari.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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


//CORS 오류 해결하기 위한 어노테이션
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/buildings/{building_id}/floors/{floor_id}/images")
//TODO 현재 전송된 이미지 이름을 건물과 층 id를 조합하여 유니크하게 주고 있지만(따라서 같은 층에 도면을 다시 올릴 경우 덮어쓰기가 된다.) 만약 스냅샷 형태로 매달 자리배치도를 관리하게 된다면 매달 이미지 관리를 따로해주어야 한다. 따라서 이미지 이름에 날짜에 대한 정보도 추가해야한다.
//TODO GetMapping에서 데이터베이스에 floor_plan에 대한 레코드가 없다면(층 정보로 생성된 레코드가 없다면) get 요청이 실패했다는 정보 넘겨주기
public class FloorPlanController {
	
	@Autowired
	private FloorPlanService floorPlanService;
	
	@Autowired
	private MinioClient minioClient;
	
	//버킷명(Amazon S3 Bucket policy를 지켜야 한다.)
	private String bucketName = "hanzari";
	//테스트용 버킷
	private String spareBucketName = "hanzari-spare";
	
	//이미지 파일 이름에 일별로 구분해주기 위한 레퍼런스 변수들
	private Date currentTime;
	private LocalDate localDate;
	private int year;
	private int month;
	private int day;
	
	//이미지 파일 MinIO 서버에 업로드
	//IOException은 imagePutInputStream의 예외 상황 처리를 위해서이다.
	//TODO 현재는 HTTP 통신의 결과값을 클라이언트에게 보내주지는 않지만(리턴타입 void) 백엔드단에서 요청 처리가 어떻게 되었는지를 알려주기 위해 메세지를 보내주어도 좋다. 예를 들어 "SUCCESS", "FAILURE" 등의 메세지를 JSON 스트럭쳐 형태로 리턴해준다.
	@PostMapping
	public void putImageFile(@PathVariable("building_id") String buildingId, @PathVariable("floor_id") String floorId, @RequestParam("imageFile") MultipartFile file) throws IOException {
		currentTime = new Date();
		localDate = currentTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		year = localDate.getYear();
		month = localDate.getMonthValue(); //getMonthValue 메소드를 사용하면 0을 포함하지 않고 1 ~ 12를 리턴한다.
		day = localDate.getDayOfMonth();
				
		//이미지 도면 파일 이름은 floorId + 연/월/일로 변경해주었다. 일별로 이미지 도면 파일을 구분해주기 위해 해당 방법을 사용하였고 동일한 날에 동일한 층의 이미지 도면 파일을 업데이트하면 덮어쓰기가 된다.
		String floorPlanFileName = floorId + "-" + Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
		//floor_plan_id 컬럼은 auto increment이기에 build할 때 안 적어주어도 된다.
		FloorPlan putfloorPlan = FloorPlan.builder().floorId(floorId).latest(true).floorPlanFileName(floorPlanFileName).build();
		InputStream imagePutInputStream = file.getInputStream();
		
		floorPlanService.save(putfloorPlan);
		
		try {
			minioClient.putObject(
				    PutObjectArgs.builder()
				    .bucket(spareBucketName)
					//object 속성이 MinIO 버킷에 저장되는 파일 이름이 된다.
				    .object(floorPlanFileName)
					//stream 속성은 이미지 사이즈 크기 만큼 메모리를 사용하여 파일을 전송한다.
				    //Object의 사이즈를 알 경우에는 3번째 인자인 partSize를 자동감지를 위해 -1로 준다.
				    .stream(imagePutInputStream, file.getSize() , -1) 
				    /*TODO getContentType을 사용하면 클라이언트 측에서 이름을 변경하여 보낼 경우 다른 형식으로 업로드 되어 후에 클라이언트에 내려줄 때 명시적 contentType과 실제 데이터의 contentType이 달라 문제가 생길 수 있다.
				    따라서 해당 문제를 해결하기 위해서는 엄격히 업로드되는 contentType을 제한하거나(해당 인자를 "image/png"로 제한하는 등) 실제 데이터를 열어보아 어떤 contentType인지 알아내는 방법들을 사용해야한다.*/
				    .contentType(file.getContentType())
				    .build());
		} catch (Exception e) {
			System.out.println("Error occurred: " + e);
		} finally {
			//finally안에는 try안에서 리턴문에 의해 메소드가 종료될 경우 그 전에 해야할 작업들을 적어줄 수 있다.
			//해당 finally문 안에는 후에 try문 안에 return문을 작성할 경우를 대비해 imagePutInputStream을 닫아주어야한다.
			//하지만 닫아 주는 경우에도 exception이 발생할 가능성이 있다. 그렇게 되면 finally안에 또 try~catch문을 사용해야하는데 이럴 경우 코드가 너무 길어진다.
			/*TODO 따라서 이러한 경우 IOUtils.closequietly()와 같은 메소드를 사용하여 열어둔 stream을 닫아주고 Null일 경우에도 알아서 처리해주는 라이브러리를 사용해도 좋다.
			 그러나 이름이 같은 클래스에 대해 해당 라이브러리의 import 경로와 개발 중인 프로젝트의 import 경로가 다른 경우가 있을 수 있고 이런 경우 오류가 날 가능성도 있다.
			 따라서 이렇게 동일한 작업을 처리해주는 메소드가 필요한 경우 직접 작성을 하고 프로젝트 내에서 필요한 클래스에서 import하는 방법으로 진행하는 것이 좋다.*/
			if(imagePutInputStream!=null)
				imagePutInputStream.close();
		}
	}

	//이미지 파일 MinIO 서버에서 다운로드
	//MinIO에 저장된 각 파일의 object명으로 찾아야한다.
	//IOException은 imageGetInputStream의 예외 상황 처리를 위해서이다.
	//TODO putImageFile 메소드 상단에 작성한 내용 참조
	@GetMapping
	public void getImageFile(@PathVariable("building_id") String buildingId, @PathVariable("floor_id") String floorId,  HttpServletResponse response) throws IOException {
		InputStream imageGetInputStream = null;
		String floorPlanFileName = null;
		FloorPlan getFloorPlan;
		
		try {
			getFloorPlan = floorPlanService.findByFloorPlanId(floorId);
			floorPlanFileName = getFloorPlan.getFloorPlanFileName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			imageGetInputStream = minioClient.getObject(
					 GetObjectArgs.builder()
					 .bucket(spareBucketName)
					 .object(floorPlanFileName)
					 .build());
			response.addHeader("Content-disposition", floorPlanFileName);
			response.setContentType(URLConnection.guessContentTypeFromName(floorPlanFileName));
			IOUtils.copy(imageGetInputStream, response.getOutputStream());
			response.flushBuffer();
		} catch(Exception e) {
			System.out.println("Error occurred: " + e);
		} finally {
			//TODO putImageFile 메소드 안의 finally문에 작성한 내용 참조
			if(imageGetInputStream != null)
				imageGetInputStream.close();
		}
	}
}