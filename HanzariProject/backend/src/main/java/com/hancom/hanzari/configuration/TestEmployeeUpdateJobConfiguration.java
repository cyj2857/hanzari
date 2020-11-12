package com.hancom.hanzari.configuration;

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

import com.hancom.hanzari.vo.EmployeeVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class TestEmployeeUpdateJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
	private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

	private final Logger LOGGER = LoggerFactory.getLogger("ConsoleLogger");

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////	conditionalStepJob, stepA, stepB, stepC는 Batch 흐름을 파악하기위해 추가해 둔 Bean들. 제거해도 무관!!	/////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

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
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////아래 부분을 작성하면 될 듯!!!! //////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
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
	public JsonItemReader<EmployeeVo> jsonItemReader() {
		return new JsonItemReaderBuilder<EmployeeVo>().jsonObjectReader(new JacksonJsonObjectReader<>(EmployeeVo.class))
				.resource(new ClassPathResource("/testdata/test-employee-data.json")).name("jsonItemReader").build();
	}

}
