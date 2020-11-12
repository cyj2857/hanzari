package com.hancom.hanzari.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.hancom.hanzari.vo.EmployeeVo;

import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;

public class JsonFileItemReaderJobConfig {
	 @Bean
	    public JsonItemReader<EmployeeVo> jsonItemReader(){
	        return new JsonItemReaderBuilder<EmployeeVo>()
	                .jsonObjectReader(new JacksonJsonObjectReader<>(EmployeeVo.class))
	                .resource(new ClassPathResource("/testdata/test-employee-data.json"))
	                .name("jsonItemReader")
	                .build();
	    }
}
