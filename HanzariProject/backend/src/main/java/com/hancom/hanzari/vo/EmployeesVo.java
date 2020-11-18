package com.hancom.hanzari.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesVo {
	@JsonProperty("cmpName")
	String cmpName;
	@JsonProperty("deptName")
	String deptName;
	@JsonProperty("deptName1")
	String deptName1;
	@JsonProperty("deptName2")
	String deptName2;
	@JsonProperty("deptName3")
	String deptName3;
	@JsonProperty("deptName4")
	String deptName4;
	@JsonProperty("deptName5")
	String deptName5;
	@JsonProperty("userName")
	String userName;
	@JsonProperty("posName")
	String posName;
	@JsonProperty("cellPhone")
	String cellPhone;
	@JsonProperty("cmpPhone")
	String cmpPhone;
	@JsonProperty("cmpEmail")
	String cmpEmail;
	@JsonProperty("cmpId")
	String cmpId;
	@JsonProperty("deptId")
	String deptId;
	@JsonProperty("upDeptId")
	String upDeptId;
	@JsonProperty("empId")
	String empId;
	@JsonProperty("userId")
	String userId;
}
