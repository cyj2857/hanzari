package com.hancom.hanzari.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.EmployeeAdditionalInfo;
import com.hancom.hanzari.service.EmployeeService;
import com.hancom.hanzari.vo.EmployeesVo;
import com.hancom.hanzari.vo.ResultVo;
import com.hancom.hanzari.vo.TokenVo;

import lombok.RequiredArgsConstructor;

@Configuration // Spring Batch의 모든 Job은 이 어노테이션을 이용해 등록하고 사용해야한다
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@EnableBatchProcessing
public class BatchEmployeeUpdateConfiguration {
	// Job 객체를 만드는 빌더, 여러 빌더를 통합하여 처리할 수 있다.
	private final JobBuilderFactory jobBuilderFactory;
	// Step 객체를 만드는 빌더, 여러 빌더를 통합하여 처리할 수 있다.
	private final StepBuilderFactory stepBuilderFactory;
	@Autowired
	private SimpleJobLauncher jobLauncher;
	// ResourceBundle을 이용하여 string value들을 불러옴 properties 확장자인데 확장자없이 파일명만 적어줘도
	// ResourceBundle이 알아서 해당 파일 찾아줌
	private final ResourceBundle stringValues = ResourceBundle.getBundle("string");
	private final Logger LOGGER = LoggerFactory.getLogger("ConsoleLogger");
	// 발행 될 토큰과 토큰 정보들을 넣어둘 VO	
	private TokenVo tokenVo;
	// 요청에 대한 응답 정보와 임직원 전체 목록을 리스트로 가지고 있을 VO
	private ResultVo resultVo;
	private final PasswordEncoder passwordEncoder;
	@Autowired
	EmployeeService employeeService;
	
	@Scheduled(cron = "0 0 * * * *")
	//@Scheduled(fixedDelay = 1000)
	public void perform() throws Exception {

		System.out.println("Job Started at :" + new Date());

		JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();

		JobExecution execution = jobLauncher.run(getEmployeesInfoJob(), param);

		System.out.println("Job finished with status :" + execution.getStatus());
	}
	
	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		return launcher;
	}
	
	@Bean
	// GetEmployeesInfoJob이란 이름으로 Batch Job을 생성
	// Job의 이름은 별도로 지정하지 않고 Builder를 통해 지정한다.
	public Job getEmployeesInfoJob() {
		return jobBuilderFactory.get("getEmployeesInfoJob").start(firstStep()) // firstStep 실행
				.on("FAILED") // firstStep이 FAILED일 경우
					.end() // flow를 종료한다.
					.from(firstStep()) // firstStep으로부터
					.on("*") // FAILED 외에 모든 경우에
					.to(secondStep()) // secondStep으로 이동한다.
						.on("FAILED") // secondStep이 FAILED일 경우
						.end() // Flow를 종료한다.
						.from(secondStep()) // secondStep으로부터
							.on("*") // FAILED 외에 모든 경우에
							.to(thirdStep()) // thirdStep으로 이동한다.
							.on("*") // thirdStep의 결과에 관계없이
							.end() // thirdStep으로 이동하면 flow를 종료한다.
				.end() // Job 종료
				.build();
	}

	// 토큰 발행 step
	public Step firstStep() {
		// Job 이름 지정과 마찬가지로 Builder를 통해 이름을 지정한다.
		// tasklet안에는 해당 step에서 수행될 기능들을 작성한다.
		return stepBuilderFactory.get("firstStep").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> First step(토큰 발행 step)");

			URL tokenUrl;
			HttpsURLConnection tokenCreatedConnection = null;
			//null로 초기화를 시켜주어야 아래 finally block의 if문에서 에러가 나지 않는다.
			//Request Body에 Data를 담기 위한 레퍼런스 변수
			OutputStream tokenCreatedConnectionSetRequestBody = null;
			//
			BufferedReader tokenBufferedReader = null;
			//Request Body에 들어갈 값들을 URLEncoder.encode() 메소드를 사용하여 String을 encoding해준다.
			//UTF-8 설정을 빼면 메소드가 deprecated가 된다.
			final String stringTokenUrlParameter = String.format(stringValues.getString("TOKEN_FORMAT"),
					URLEncoder.encode(stringValues.getString("CLIENT_ID"), "UTF-8"),
					URLEncoder.encode(stringValues.getString("CLIENT_SECRET"), "UTF-8"),
					URLEncoder.encode(stringValues.getString("GRANT_TYPE"), "UTF-8"));

			/*TODO try with resources statement를 사용하려고 했지만 tokenBufferedWriter와 tokenBufferedReader를 null로 초기화시키는 문장은 ()안에 사용할 수 없다. 
			또한 실제 생성자를 통해 객체를 생성해주는 부분도 try문에서 다른 문장들이 실행된 결과값을 가지고 객체를 생성하기에 ()안에 먼저 사용할 수도 없다.
			따라서 해당 객체를 생성하기전까지의 문장을 다른 try~catch문을 생성하여 작성하고 해당 객체를 생성하는 부분부터 try문을 새로만들어 ()안에 객체를 생성하는 방법 등의 다른 방법들을 생각해 봐야겠다.*/
			try {
				tokenUrl = new URL(stringValues.getString("TOKEN_URL"));
				tokenCreatedConnection = (HttpsURLConnection) tokenUrl.openConnection();
				
				//요청 방식 POST
				tokenCreatedConnection.setRequestMethod("POST");
				//OutPutStream으로 POST 데이터를 넘겨주겠다는 설정
				tokenCreatedConnection.setDoOutput(true);
				//InputStream으로 서버로부터 응답받겠다는 설정
				tokenCreatedConnection.setDoInput(true);
				//참고 Request Header 값들은 setRequestProperty를 사용하면 된다
				
				System.out.println(tokenCreatedConnection.getInstanceFollowRedirects());

				//Request Body에 Data를 담기 위해 OutputStream 객체를 생성
				tokenCreatedConnectionSetRequestBody = tokenCreatedConnection.getOutputStream();
				//write() 메소드중에 String을 인자로 받는 오버로딩된 메소드가 없기에 stringTokenUrlParameter를 byte형식으로 변환시켜 줘서 매개변수로 주어야 한다.
				//Request Body에 Data 세팅
				tokenCreatedConnectionSetRequestBody.write(stringTokenUrlParameter.getBytes());
				//Request Body에 Data 입력
				//flush() 메소드를 호출하지 않아도 프로그램이 정상 작동하지만 명시적으로 이 때 입력한 버퍼 내용들을 비워준다는 의미로 작성하였다.
				tokenCreatedConnectionSetRequestBody.flush();

				//먼저 요청에 대한 응답이 잘 왔는지 확인
				if(tokenCreatedConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
					//보낸 요청에 따라 서버에서 받아온 정보들을 BufferedReader 객체에 넣어줌.
					tokenBufferedReader = new BufferedReader(new InputStreamReader(tokenCreatedConnection.getInputStream()));
					//전체 Json라인을 한 줄로 받는 StringBuilder
					StringBuilder jsonOneLine = new StringBuilder();
					//한 줄씩 받는 String
					String jsonEachLine;
					
					//while문 조건에 jsonEachLine에 readLine 된 것을 대입해주어야한다.
					//readLine은 다음번 호출할 때 마지막 읽은 다음 줄 부터 읽기에 만약 첫 줄로 끝나는 데이터이고(대부분의 응답받을 Json은 이런 형태일 것 같다.)
					//while문 안에 대입문을 작성할 경우 NullPointerException이 발생한다.
					while((jsonEachLine = tokenBufferedReader.readLine()) != null) {
						jsonOneLine.append(jsonEachLine);
					}
					// jackson 라이브러리를 이용하여 손쉽게 Json형식에서 VO 형식에 매핑해줄 수 있다.
					tokenVo = new ObjectMapper().readValue(jsonOneLine.toString(), TokenVo.class);
				}
				//응답이 제대로 오지 않을 경우
				else {
					//ExitStatus를 FAILED로 지정한다. 해당 status를 보고 flow가 진행된다.
					//ExitStatus가 FAILED일 때는 flow를 종료하도록 job을 설정해두었다.
					contribution.setExitStatus(ExitStatus.FAILED);
				}
			} catch (IOException e) {
				LOGGER.error("IOException in First step", e);
				contribution.setExitStatus(ExitStatus.FAILED);
			} catch (Exception e) {
				LOGGER.error("Exception in First step", e);
				contribution.setExitStatus(ExitStatus.FAILED);
			//try block이 종료하기 전 finally block 실행
			} finally {
				if(tokenCreatedConnectionSetRequestBody != null)
					tokenCreatedConnectionSetRequestBody.close();
				if(tokenBufferedReader != null)
					tokenBufferedReader.close();
			}

			LOGGER.info("토큰 타입 : " + tokenVo.getTokenType());
			LOGGER.info("토큰 : " + tokenVo.getAccessToken());
			LOGGER.info("유효시간 : " + tokenVo.getExpiresIn());

			return RepeatStatus.FINISHED;
		}).build();
	}

	// 임직원 리스트 받아온 후 프로젝트단 VO 객체에 매핑
	public Step secondStep() {
		return stepBuilderFactory.get("secondStep").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> Second step(발행된 토큰으로부터 API 콜을 통해 JSON형식의 임직원 리스트를 받아온 후 VO 객체에 매핑해주는 step)");
			URL allEmployeeListUrl;
			HttpsURLConnection allEmployeeListGetConnection;
			BufferedReader allEmployeeListReader = null;
			final String stringEmployeesUrl = stringValues.getString("EMPLOYEES_URL");
			final String stringCmpIdParameter = String.format("cmpId=%s", URLEncoder.encode(stringValues.getString("COMPANY_ID"), "UTF-8"));

			/*TODO try with resources statement를 사용하려고 했지만 allEmployeeListReader를 null로 초기화시키는 문장은 ()안에 사용할 수 없다. 
			또한 실제 생성자를 통해 객체를 생성해주는 부분도 try문에서 다른 문장들이 실행된 결과값을 가지고 객체를 생성하기에 ()안에 먼저 사용할 수도 없다.
			따라서 해당 객체를 생성하기전까지의 문장을 다른 try~catch문을 생성하여 작성하고 해당 객체를 생성하는 부분부터 try문을 새로만들어 ()안에 객체를 생성하는 방법 등의 다른 방법들을 생각해 봐야겠다.*/
			try {
				allEmployeeListUrl = new URL(stringEmployeesUrl + "?" + stringCmpIdParameter);
				allEmployeeListGetConnection = (HttpsURLConnection) allEmployeeListUrl.openConnection();
				allEmployeeListGetConnection.setRequestMethod("GET");
				//Request Headers에 추가할 내용
				allEmployeeListGetConnection.setRequestProperty("Authorization", tokenVo.getAccessToken());
				
				//먼저 요청에 대한 응답이 잘 왔는지 확인
				if(allEmployeeListGetConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
					allEmployeeListReader = new BufferedReader(new InputStreamReader(allEmployeeListGetConnection.getInputStream()));
					
					// 현재 응답받은 형태가 Json안에 nested Json이있고 그안에 employees 키와 매핑된 array(각각의 임직원 정보 Json 리스트)가 있다. 
					// 따라서 일반적인 방법으로 ObjectMapper의 readValue 메소드를 사용할 수 없다.
					// 우선 응답받은 original Json을 originalJsonNode 객체에 넣어준다.
					// token 발행 시에 작성했던 방식과 마찬가지로 한 줄씩 읽어오는 작업을 한다.
					StringBuilder jsonOneLine = new StringBuilder(); //전체 Json라인을 한 줄로 받는 StringBuilder
					String jsonEachLine; //한 줄씩 받는 String
					
					//while문 조건에 jsonEachLine에 readLine 된 것을 대입해주어야한다.
					//readLine은 다음번 호출할 때 마지막 읽은 다음 줄 부터 읽기에 만약 첫 줄로 끝나는 데이터이고(대부분의 응답받을 Json은 이런 형태일 것 같다.)
					//while문 안에 대입문을 작성할 경우 NullPointerException이 발생한다.
					while((jsonEachLine = allEmployeeListReader.readLine()) != null) {		
						jsonOneLine.append(jsonEachLine);
					}
					
					JsonNode originalJsonNode = new ObjectMapper().readTree(jsonOneLine.toString());
					
					// forEach문을 돌며 각각의 임직원 정보를 EmployeesVo 객체에 넣어준다.
					List<EmployeesVo> listEmployeesVo = new ArrayList<>();
					originalJsonNode.get("result").get("employees").forEach(e -> {
						// e가 JsonNode 형식이라 readValue()가 아닌 convertValue() 메소드를 사용해야한다.
						// 내부 로직적으로 setter가 필요하기에 VO 클래스에 @Data 어노테이션을 사용하였다.
						//jsonOneLine을 사용하여 받아온 Json 데이터를 한 줄로 변환해주었기에 여기서 또 한 줄씩 읽어오는 변환 작업을 할 필요가 없다.
						EmployeesVo employeesVo = new ObjectMapper().convertValue(e, EmployeesVo.class);
						listEmployeesVo.add(employeesVo);
					});

					// 응답받은 Json의 메타정보들과 모든 임직원 리스트를 저장해두기 위한 resultVo 객체 생성
					resultVo = new ResultVo(originalJsonNode.get("result").get("resultCode").textValue(),
							originalJsonNode.get("result").get("resultMessage").textValue(),
							originalJsonNode.get("result").get("resultDesc").textValue(), listEmployeesVo);
					LOGGER.info("임직원 리스트({})", new Date());
					resultVo.getAllEmployeeListVo().forEach(e -> LOGGER.info(e.toString()));
				}
				else {
					contribution.setExitStatus(ExitStatus.FAILED);
				}
				
			} catch (IOException e) {
				LOGGER.error("IOException in Second step", e);
				contribution.setExitStatus(ExitStatus.FAILED);
			} catch (Exception e) {
				LOGGER.error("Exception in Second step", e);
				contribution.setExitStatus(ExitStatus.FAILED);
			} finally {
				if(allEmployeeListReader != null)
					allEmployeeListReader.close();
			}

			return RepeatStatus.FINISHED;
		}).build();
	}

	// VO에 매핑된 임직원 리스트를 데이터베이스에 저장
	// TODO authority와 status에 대한 정보 추후에 변경하기
	public Step thirdStep() {
		return stepBuilderFactory.get("thirdStep").tasklet((contribution, chunkContext) -> {
			LOGGER.info(">>>>> Third step(VO에 매핑된 임직원 리스트를 데이터베이스에 삽입하는 step)");
			
			try{
				resultVo.getAllEmployeeListVo().forEach(e -> {
					employeeService.save(Employee.builder().employeeId(e.getEmpId()).authority("viewer").password(passwordEncoder.encode("0000")).roles(Collections.singletonList("ROLE_USER"))
							.additionalInfo(EmployeeAdditionalInfo.builder().employeeId(e.getEmpId())
									.employeeName(e.getUserName()).status("재직").extensionNumber(e.getCmpPhone())
									.departmentId(e.getDeptId()).departmentName(e.getDeptId()).build())
							.build());
				});
			} catch (Exception e) {
				LOGGER.error("Exception in Third step", e);
			}
			return RepeatStatus.FINISHED;
		}).build();
	}
}
