package com.hancom.hanzari.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class EmployeesVo {
	@JsonProperty("cmpName")
	private String cmpName;
	@JsonProperty("deptName")
	private String deptName;
	@JsonProperty("deptName1")
	private String deptName1;
	@JsonProperty("deptName2")
	private String deptName2;
	@JsonProperty("deptName3")
	private String deptName3;
	@JsonProperty("deptName4")
	private String deptName4;
	@JsonProperty("deptName5")
	private String deptName5;
	@JsonProperty("userName")
	private String userName;
	@JsonProperty("posName")
	private String posName;
	@JsonProperty("cellPhone")
	private String cellPhone;
	@JsonProperty("cmpPhone")
	private String cmpPhone;
	@JsonProperty("cmpEmail")
	private String cmpEmail;
	@JsonProperty("cmpId")
	private String cmpId;
	@JsonProperty("deptId")
	private String deptId;
	@JsonProperty("upDeptId")
	private String upDeptId;
	@JsonProperty("empId")
	private String empId;
	@JsonProperty("userId")
	private String userId;
}
