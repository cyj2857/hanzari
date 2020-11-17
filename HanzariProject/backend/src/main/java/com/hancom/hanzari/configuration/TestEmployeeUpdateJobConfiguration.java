package com.hancom.hanzari.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hancom.hanzari.vo.EmployeesVo;
import com.hancom.hanzari.vo.ResultVo;
import com.hancom.hanzari.vo.TokenVo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class TestEmployeeUpdateJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
	private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

	private final Logger LOGGER = LoggerFactory.getLogger("ConsoleLogger");
	
	//발행될 토큰을 넣어둘 Vo
	private TokenVo tokenVo;
	
	private ResultVo resultVo;

	@Bean
	public Job conditionalStepJob(Step stepA, Step stepB, Step stepC) {
		return jobBuilderFactory.get("conditionalStepJob").start(stepA).on(ExitStatus.FAILED.getExitCode()) // FAILED 일 경우
				.to(stepC) // stepC으로 이동한다.
				.on("*") // stepC의 결과 관계 없이
				.end() // stepC으로 이동하면 Flow가 종료한다.
				.from(stepA) // stepA로부터
				.on("*") // FAILED 외에 모든 경우
				.to(stepB) // stepB로 이동한다.
				.next(stepC) // stepB가 정상 종료되면 stepC으로 이동한다.
				.on("*") // stepC의 결과 관계 없이
				.end() // stepC으로 이동하면 Flow가 종료한다.
				.end() // Job 종료
				.build();
	}

	@Bean
	public Step stepA() {
		return stepBuilderFactory.get("stepA").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> This is StepA");
			
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
				LOGGER.error("IOException in StepA", e);
			} catch(Exception e) {
				LOGGER.error("Exception in StepA", e);
			}
			
			System.out.println("토큰 타입 : " + tokenVo.getTokenType());
			System.out.println("토큰 : " + tokenVo.getAccessToken());
			System.out.println("유효시간 : " + tokenVo.getExpiresIn());
			/**
			 * ExitStatus를 FAILED로 지정한다. 해당 status를 보고 flow가 진행된다.
			 **/
			//contribution.setExitStatus(ExitStatus.FAILED);

			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Step stepB() {
		return stepBuilderFactory.get("stepB").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> This is StepB");
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

				System.out.println(allEmployeeListReader.readLine());
				resultVo = new ObjectMapper().readValue(allEmployeeListReader.readLine(), ResultVo.class);
				allEmployeeListReader.close();
			} catch(IOException e) {
				LOGGER.error("IOException in StepB", e);
			} catch (Exception e) {
				LOGGER.error("Exception in StepB", e);
			}
			
			System.out.println(resultVo.getResultDesc());
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Step stepC() {
		return stepBuilderFactory.get("stepC").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> This is StepC");
			return RepeatStatus.FINISHED;
		}).build();
	}
	
	// 여기다 작성하면 될 듯
	@Bean
	public Job simpleJob() {
		return jobBuilderFactory.get("simpleJob").start(simpleStep1()).build();
	}
	
	// simpleJob에 물려있는 step.
	@Bean
	public Step simpleStep1() {
		return stepBuilderFactory.get("simpleStep1").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> This is Step1"); // 테스트를 위한 코드 
			// TODO 1. JSON파일의 "employees" 부분을 List<EmployeeVo> 리스트로 받아온다.
			// TODO 2. empVo.forEach()에서 각각의 empVo에 해당하는 employee와 employee_additional_info 객체를 만들어준다. (toEntity())
			// TODO 3. employeeService.save()과정을 수행해준다.
			// TODO 4. 이 job을 일정한 스케쥴로 돌려야 함. ex. 00:00마다 돌린다던지...
			return RepeatStatus.FINISHED;
		}).build();
	}

	// JSON 객체를 읽어오는 Reader
	public JsonItemReader<EmployeesVo> jsonItemReader() {
		return new JsonItemReaderBuilder<EmployeesVo>().jsonObjectReader(new JacksonJsonObjectReader<>(EmployeesVo.class))
				.resource(new ClassPathResource("/testdata/test-employee-data.json")).name("jsonItemReader").build();
	}

}
