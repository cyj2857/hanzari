package com.hancom.hanzari.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

//VO = Value Object(값 객체) 말 그대로 데이터를 담는 객체를 의미한다.
//값을 표현하기 위해 불변 클래스를 만들어서 사용하는데, readOnly의 특성만을 가진다. 따라서 중간에 값을 바꿀 수 없고 새로 만들어야 한다는 특징이 있다.
//Json으로부터 받아온 값을 초기에 할당하고 그 후에는 백엔드 내부적으로도 값을 수정할 일이 없기에(DB에 값을 넣기만 하면 되니까) Batch와 관련된 클래스들은 VO 개념으로 만들어 주었다.
@Getter
@NoArgsConstructor
@AllArgsConstructor
//Json과 VO의 자동 매핑 기능을 모두 막음
@JsonAutoDetect(fieldVisibility=Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
//Json 문자열로 표현할 때 표현되는 멤버필드의 순서를 정하는 어노테이션
//Json 필드명이 아닌 클래스의 필드명을 주어야 한다.
@JsonPropertyOrder({"tokenType","accessToken","expiresIn"})
public class TokenVo {
	@JsonProperty("token_type")
	private String tokenType;;
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("expires_in")
	private int expiresIn;
}
