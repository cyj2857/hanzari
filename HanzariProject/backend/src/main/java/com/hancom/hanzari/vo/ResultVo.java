package com.hancom.hanzari.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

//@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo {
	//@JsonProperty("resultCode")
	private String resultCode;
	//@JsonProperty("resultMessage")
	private String resultMessage;
	//@JsonProperty("resultDesc")
	private String resultDesc;

	List<EmployeesVo> allEmployeeListVo;
	
	@JsonSetter("result.resultCode")
	public void setResultCode(String resultCode) { this.resultCode = resultCode; }
	
	public String getResultCode() { return this.resultCode; }
}
