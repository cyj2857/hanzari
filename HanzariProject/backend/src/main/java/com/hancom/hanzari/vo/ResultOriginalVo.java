package com.hancom.hanzari.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultOriginalVo {
	@JsonProperty("result")
	ResultVo resultVo;
}
