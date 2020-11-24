package com.hancom.hanzari.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;


//CORS 오류 해결하기 위한 어노테이션
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("api/buildings/{building_id}/floors/{floor_id}/images")
//TODO 현재 전송된 이미지 이름을 건물과 층 id를 조합하여 유니크하게 주고 있지만(따라서 같은 층에 도면을 다시 올릴 경우 덮어쓰기가 된다.) 만약 스냅샷 형태로 매달 자리배치도를 관리하게 된다면 매달 이미지 관리를 따로해주어야 한다. 따라서 이미지 이름에 날짜에 대한 정보도 추가해야한다.
//TODO GetMapping에서 데이터베이스에 floor_plan에 대한 레코드가 없다면(층 정보로 생성된 레코드가 없다면) get 요청이 실패했다는 정보 넘겨주기
public class FloorPlanController {	
	//버킷명(Amazon S3 Bucket policy를 지켜야 한다.)
	private static final String BUCKET_NAME = "hanzari";

	private final FloorPlanService floorPlanService;
	private final MinioClient minioClient;
	private final Logger LOGGER = LoggerFactory.getLogger("EngineLogger");
	
	//이미지 파일 MinIO 서버에 업로드
	//IOException은 imagePutInputStream의 예외 상황 처리를 위해서이다.
	//axios 요청 횟수와 메소드가 실행되는 것은 synchronized 되어있지 않다.(요청에 따라 메소드가 동시에 실행되고 그에 따라 동시에 변수에 접근하여 데이터 일관성이 깨지는 문제가 발생한다.)
	//따라서 하나의 요청이 끝난 후에 다른 요청이 메소드에 진입할 수 있도록 synchronized 키워드를 사용해주었다.
	//TODO 현재는 HTTP 통신의 결과값을 클라이언트에게 보내주지는 않지만(리턴타입 void) 백엔드단에서 요청 처리가 어떻게 되었는지를 알려주기 위해 메세지를 보내주어도 좋다. 예를 들어 "SUCCESS", "FAILURE" 등의 메세지를 JSON 스트럭쳐 형태로 리턴해준다.
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@PostMapping
	public void putImageFile(@PathVariable("building_id") String buildingId, @PathVariable("floor_id") String floorId, @RequestParam("imageFile") MultipartFile file) throws IOException {
		
		LOGGER.info("FloorPlanController.putImageFile called. (building_id : {}, floor_id : {})", buildingId, floorId);
		
		FloorPlan latestFloorPlan;
		FloorPlan putFloorPlan;
		StringBuilder putFloorPlanFileName;
		
		try {
			//해당 층의 가장 최신으로 연결되어있는 floorPlan 레코드의 latest 컬럼을 false로 변경해준다.
			latestFloorPlan = floorPlanService.findByFloorIdAndLatest(floorId, true);
			latestFloorPlan.setLatest(false);
			floorPlanService.save(latestFloorPlan);
		} catch(Exception e) {
			LOGGER.error("Can't find matched floorPlan record. Exception message : ", e);
		}

		InputStream imagePutInputStream = file.getInputStream();
		putFloorPlanFileName = new StringBuilder();
		
		try {
			//이미지 파일 이름에 일별로 구분해주기 위한 레퍼런스 변수들
			Date currentTime = new Date();
			
			putFloorPlanFileName.append(floorId);
			putFloorPlanFileName.append("-");
			putFloorPlanFileName.append(currentTime.toString());
			UUID floorPlanId = UUID.randomUUID();
			LOGGER.info("Image File name change to {} for store in MinIO bucket", putFloorPlanFileName.toString());
			putFloorPlan = FloorPlan.builder().floorPlanId(floorPlanId.toString()).floorId(floorId).latest(true).floorPlanFileName(putFloorPlanFileName.toString()).build();
			floorPlanService.save(putFloorPlan);
			
			minioClient.putObject(
				    PutObjectArgs.builder()
				    .bucket(BUCKET_NAME)
					//object 속성이 MinIO 버킷에 저장되는 파일 이름이 된다.
				    .object(putFloorPlanFileName.toString())
					//stream 속성은 이미지 사이즈 크기 만큼 메모리를 사용하여 파일을 전송한다.
				    //Object의 사이즈를 알 경우에는 3번째 인자인 partSize를 자동감지를 위해 -1로 준다.
				    .stream(imagePutInputStream, file.getSize() , -1)
				    /*TODO getContentType을 사용하면 클라이언트 측에서 이름을 변경하여 보낼 경우 다른 형식으로 업로드 되어 후에 클라이언트에 내려줄 때 명시적 contentType과 실제 데이터의 contentType이 달라 문제가 생길 수 있다.
				    따라서 해당 문제를 해결하기 위해서는 엄격히 업로드되는 contentType을 제한하거나(해당 인자를 "image/png"로 제한하는 등) 실제 데이터를 열어보아 어떤 contentType인지 알아내는 방법들을 사용해야한다.*/
				    .contentType(file.getContentType())
				    .build());
		} catch (Exception e) {
			LOGGER.error("Can't put object in the MinIO bucket. Exception message : ", e);
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
	//axios 요청 횟수와 메소드가 실행되는 것은 synchronized 되어있지 않다.(요청에 따라 메소드가 동시에 실행되고 그에 따라 동시에 변수에 접근하여 데이터 일관성이 깨지는 문제가 발생한다.)
	//따라서 하나의 요청이 끝난 후에 다른 요청이 메소드에 진입할 수 있도록 synchronized 키워드를 사용해주었다.
	//TODO putImageFile 메소드 상단에 작성한 내용 참조
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@GetMapping
	public void getImageFile(@PathVariable("building_id") String buildingId, @PathVariable("floor_id") String floorId,  HttpServletResponse response) throws IOException {
		
		LOGGER.info("FloorPlanController.getImageFile called. (building_id : {}, floor_id : {})", buildingId, floorId);
		
		FloorPlan getFloorPlan;
		StringBuilder getFloorPlanFileName;
		InputStream imageGetInputStream = null;
		
		//클라이언트에서 요청을 할 때 잘못된 요청(floorId가 undefined가 오는 등)이 오더라도 try catch문을 두개로 나눠두었기에 이전 getFloorPlanFileName에 저장되어있던 이전 이미지 파일을 가져오게 된다.
		//따라서 모든 과정을 하나의 try catch문에 넣어주어 하나의 과정에서 에러가 난다면 다음 단계로 넘어갈 수 없도록 해주어야한다.
		try {
			//DB에서 요청한 층 정보와 연결된 레코드를 찾아 이미지 이름 찾기
			getFloorPlan = floorPlanService.findByFloorIdAndLatest(floorId, true);
			getFloorPlanFileName = new StringBuilder();
			getFloorPlanFileName.append(getFloorPlan.getFloorPlanFileName());
			
			//MinIO 서버 버킷에서 이미지 가져오기
			imageGetInputStream = minioClient.getObject(
					 GetObjectArgs.builder()
					 .bucket(BUCKET_NAME)
					 .object(getFloorPlanFileName.toString())
					 .build());
			response.addHeader("Content-disposition", getFloorPlanFileName.toString());
			response.setContentType(URLConnection.guessContentTypeFromName(getFloorPlanFileName.toString()));
			IOUtils.copy(imageGetInputStream, response.getOutputStream());
			response.flushBuffer();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			//TODO putImageFile 메소드 안의 finally문에 작성한 내용 참조
			if(imageGetInputStream != null)
				imageGetInputStream.close();
		}
	}
}