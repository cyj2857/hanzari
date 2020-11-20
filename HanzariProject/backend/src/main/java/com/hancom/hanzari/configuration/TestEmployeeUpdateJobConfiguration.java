package com.hancom.hanzari.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.EmployeeAdditionalInfo;
import com.hancom.hanzari.service.EmployeeService;
import com.hancom.hanzari.vo.EmployeesVo;
import com.hancom.hanzari.vo.ResultVo;
import com.hancom.hanzari.vo.TokenVo;

import lombok.RequiredArgsConstructor;

@Configuration //Spring Batch의 모든 Job은 이 어노테이션을 이용해 등록하고 사용해야한다
@RequiredArgsConstructor //생성자 DI를 위한 lombok 어노테이션
public class TestEmployeeUpdateJobConfiguration {
	//Job 객체를 만드는 빌더, 여러 빌더를 통합하여 처리할 수 있다.
	private final JobBuilderFactory jobBuilderFactory;
	//Step 객체를 만드는 빌더, 여러 빌더를 통합하여 처리할 수 있다.
	private final StepBuilderFactory stepBuilderFactory;

	private final Logger LOGGER = LoggerFactory.getLogger("ConsoleLogger");
	
	//발행 될 토큰과 토큰 정보들을 넣어둘 VO
	private TokenVo tokenVo;
	//요청에 대한 응답 정보와 임직원 전체 목록을 리스트로 가지고 있을 VO
	private ResultVo resultVo;
	
	@Autowired
	EmployeeService employeeService;

	@Bean
	//GetEmployeesInfoJob이란 이름으로 Batch Job을 생성
	//Job의 이름은 별도로 지정하지 않고 Builder를 통해 지정한다.
	public Job getEmployeesInfoJob() {
		return jobBuilderFactory.get("getEmployeesInfoJob").start(firstStep()).on(ExitStatus.FAILED.getExitCode()) // FAILED 일 경우
				.to(thirdStep()) // stepC으로 이동한다.
				.on("*") // stepC의 결과 관계 없이
				.end() // stepC으로 이동하면 Flow가 종료한다.
				.from(firstStep()) // stepA로부터
				.on("*") // FAILED 외에 모든 경우
				.to(secondStep()) // stepB로 이동한다.
				.next(thirdStep()) // stepB가 정상 종료되면 stepC으로 이동한다.
				.on("*") // stepC의 결과 관계 없이
				.end() // stepC으로 이동하면 Flow가 종료한다.
				.end() // Job 종료
				.build();
	}

	@Bean
	//토큰 발행 step
	public Step firstStep() {
		//Job 이름 지정과 마찬가지로 Builder를 통해 이름을 지정한다.
		//tasklet안에는 해당 step에서 수행될 기능들을 작성한다.
		return stepBuilderFactory.get("firstStep").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> First step(토큰 발행 step)");
			
			URL tokenUrl;
			HttpsURLConnection tokenCreatedConnection;
			BufferedReader tokenbufferedReader;
			BufferedWriter tokenBufferedWriter;
			//URL String으로 설정
			final String stringTokenUrl = "https://infosys-gateway.hancom.com/common/oauth2/token";
			//URL뒤에 들어갈 Parameter들 설정
			final String stringTokenUrlParameter = String.format("client_id=%s&client_secret=%s&grant_type=%s", 
					URLEncoder.encode("8SqT9fPgDyNS2d4mn3PBFsaeD65dVvg2", "UTF-8"), URLEncoder.encode("ypseY19GpZIXcA8wSi4TS7WU7XVknXEs", "UTF-8"), URLEncoder.encode("client_credentials", "UTF-8"));
			
			try {
				tokenUrl = new URL(stringTokenUrl + "?" + stringTokenUrlParameter);
				tokenCreatedConnection = (HttpsURLConnection)tokenUrl.openConnection();
				tokenCreatedConnection.setRequestMethod("POST");
				//아래 설정들은 입출력 가능상태로 만들기 위한 것
				tokenCreatedConnection.setDoInput(true);
				tokenCreatedConnection.setDoOutput(true);
				tokenCreatedConnection.setInstanceFollowRedirects(false);
				
				//Parameter를 HttpsURLConnection에 설정
				tokenBufferedWriter = new BufferedWriter(new OutputStreamWriter(tokenCreatedConnection.getOutputStream(), "UTF-8"));
				tokenBufferedWriter.write(stringTokenUrlParameter);
				
				tokenbufferedReader = new BufferedReader(new InputStreamReader(tokenCreatedConnection.getInputStream(), "UTF-8"));
				
				//jackson 라이브러리를 이용하여 손쉽게 Json형식에서 VO 형식에 매핑해줄 수 있다.
				tokenVo = new ObjectMapper().readValue(tokenbufferedReader.readLine(),TokenVo.class);
				tokenbufferedReader.close();
			} catch(IOException e) {
				LOGGER.error("IOException in First step", e);
			} catch(Exception e) {
				LOGGER.error("Exception in First step", e);
			}
			
			LOGGER.info("토큰 타입 : " + tokenVo.getTokenType());
			LOGGER.info("토큰 : " + tokenVo.getAccessToken());
			LOGGER.info("유효시간 : " + tokenVo.getExpiresIn());

			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	//임직원 리스트 받아온 후 프로젝트단 VO 객체에 매핑
	public Step secondStep() {
		return stepBuilderFactory.get("secondStep").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> Second step(발행된 토큰으로부터 API 콜을 통해 JSON형식의 임직원 리스트를 받아온 후 VO 객체에 매핑해주는 step)");
			URL allEmployeeListUrl;
			HttpsURLConnection allEmployeeListGetConnection;
			BufferedReader allEmployeeListReader;
			final String stringTokenUrl = "https://infosys-gateway.hancom.com/gw/organization/v1/employees";
			final String stringCmpIdParameter = String.format("cmpId=%s",URLEncoder.encode("C100171030", "UTF-8"));	
			
			try {
				allEmployeeListUrl = new URL(stringTokenUrl + "?" + stringCmpIdParameter);
				allEmployeeListGetConnection = (HttpsURLConnection)allEmployeeListUrl.openConnection();
				allEmployeeListGetConnection.setRequestMethod("GET");
				allEmployeeListGetConnection.setRequestProperty("Authorization", tokenVo.getAccessToken());
				allEmployeeListReader = new BufferedReader(new InputStreamReader(allEmployeeListGetConnection.getInputStream(), "UTF-8"));
				//현재 응답받은 형태가 Json안에 nested Json이있고 그안에 employees 키와 매핑된 array(각각의 임직원 정보 Json 리스트)가 있다. 따라서 일반적인 방법으로 ObjectMapper의 readValue 메소드를 사용할 수 없다.
				//우선 응답받은 original Json을 originalJsonNode 객체에 넣어준다.
				JsonNode originalJsonNode = new ObjectMapper().readTree(allEmployeeListReader.readLine());

				//forEach문을 돌며 각각의 임직원 정보를 EmployeesVo 객체에 넣어준다.
				List<EmployeesVo> listEmployeesVo = new ArrayList<>();
				originalJsonNode.get("result").get("employees").forEach(e -> {
					//e가 JsonNode 형식이라 readValue()가 아닌 convertValue() 메소드를 사용해야한다.
					//내부 로직적으로 setter가 필요하기에 VO 클래스에 @Data 어노테이션을 사용하였다.
					EmployeesVo employeesVo = new ObjectMapper().convertValue(e, EmployeesVo.class);
					listEmployeesVo.add(employeesVo);
				});
				
				//응답받은 Json의 메타정보들과 모든 임직원 리스트를 저장해두기 위한 resultVo 객체 생성
				resultVo = new ResultVo(originalJsonNode.get("result").get("resultCode").textValue(), 
						originalJsonNode.get("result").get("resultMessage").textValue(), 
						originalJsonNode.get("result").get("resultDesc").textValue(),
						listEmployeesVo);
				
				LOGGER.info("임직원 리스트({})", new Date());
				resultVo.getAllEmployeeListVo().forEach(e -> LOGGER.info(e.toString()));
				allEmployeeListReader.close();
			} catch(IOException e) {
				LOGGER.error("IOException in Second step", e);
			} catch (Exception e) {
				LOGGER.error("Exception in Second step", e);
			}
			
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	//VO에 매핑된 임직원 리스트를 데이터베이스에 저장
	//TODO authority와 status에 대한 정보 추후에 변경하기
	public Step thirdStep() {
		return stepBuilderFactory.get("thirdStep").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> Third step(VO에 매핑된 임직원 리스트를 데이터베이스에 삽입하는 step)");
			
			try{
				resultVo.getAllEmployeeListVo().forEach(e -> {
					employeeService.save(employeeService.save(Employee.builder().employeeId(e.getEmpId()).authority("viewer")
							.additionalInfo(EmployeeAdditionalInfo.builder().employeeId(e.getEmpId()).employeeName(e.getUserName()).status("재직")
							.extensionNumber(e.getCmpPhone()).departmentId(e.getDeptId()).departmentName(e.getDeptId()).build()).build()));
				});
			} catch(Exception e) {
				LOGGER.error("Exception in Third step", e);
			}
			return RepeatStatus.FINISHED;
		}).build();
	}
}
