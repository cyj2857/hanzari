package com.hancom.hanzari.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo {
	@JsonProperty("resultCode")
	String resultCode;
	@JsonProperty("resultMessage")
	String resultMessage;
	@JsonProperty("resultDesc")
	String resultDesc;

	List<EmployeeVo> allEmployeeListVo;
}
