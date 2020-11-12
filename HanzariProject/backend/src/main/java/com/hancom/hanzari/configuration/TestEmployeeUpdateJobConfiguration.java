package com.hancom.hanzari.configuration;

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

@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class TestEmployeeUpdateJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
	private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

	@Bean
	public Job simpleJob() {
		return jobBuilderFactory.get("simpleJob").start(simpleStep1()).build();
	}

	@Bean
	public Step simpleStep1() {
		return stepBuilderFactory.get("simpleStep1").tasklet((contribution, chunkContext) -> {
			// log.info(">>>>> This is Step1"); // 테스트를 위한 코드
			// TODO 1. JSON파일의 "employees" 부분을 List<EmployeeVo> 리스트로 받아온다.
			// TODO 2. empVo.forEach()에서 각각의 empVo에 해당하는 employee,와 employee_additional_info 객체를 만들어준다. (toEntity())
			// TODO 2. employeeService.save()과정을 수행해준다.
			return RepeatStatus.FINISHED;
		}).build();
	}

	// JSON 객체를 읽어오는 Reader
	/*
	 * public JsonItemReader<EmployeeVo> jsonItemReader() { return new
	 * JsonItemReaderBuilder<EmployeeVo>().jsonObjectReader(new
	 * JacksonJsonObjectReader<>(EmployeeVo.class)) .resource(new
	 * ClassPathResource("/testdata/test-employee-data.json")).name("jsonItemReader"
	 * ).build(); }
	 */

}
