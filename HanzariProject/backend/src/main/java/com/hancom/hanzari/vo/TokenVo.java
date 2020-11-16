package com.hancom.hanzari.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//VO = Value Object(값 객체) 말 그대로 데이터를 담는 객체를 의미한다.
//값을 표현하기 위해 불변 클래스를 만들어서 사용하는데, readOnly의 특성만을 가진다. 따라서 중간에 값을 바꿀 수 없고 새로 만들어야 한다는 특징이 있다.
//Json으로부터 받아온 값을 초기에 할당하고 그 후에는 백엔드 내부적으로도 값을 수정할 일이 없기에(DB에 값을 넣기만 하면 되니까) Batch와 관련된 클래스들은 VO 개념으로 만들어 주었다.
public class TokenVo {
	String tokenType;;
	String accessToken;
	String expiresIn;
}
