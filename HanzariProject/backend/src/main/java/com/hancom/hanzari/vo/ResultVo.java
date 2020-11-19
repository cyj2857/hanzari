package com.hancom.hanzari.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
//nested Json의 텍스트 형태로 읽어와 해당 클래스에 대입하기 때문에 사실 JsonProperty를 써주지 않아도 된다.(jackson 사용하여 해당 필드로 자동 찾아가는 형식이 아니니)
//하지만 개념상으로 어떻게 구성을 하였는지 나타내기 위해 해당 어노테이션을 적어주었다.
public class ResultVo {
	@JsonProperty("resultCode")
	private String resultCode;
	@JsonProperty("resultMessage")
	private String resultMessage;
	@JsonProperty("resultDesc")
	private String resultDesc;

	List<EmployeesVo> allEmployeeListVo;
}
