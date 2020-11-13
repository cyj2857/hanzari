package com.hancom.hanzari.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "floor_plan")
@Builder
public class FloorPlan {
	
	//데이터베이스에 각각의 이미지 파일 이름을 찾아내기 위한(이전 이력들도 포함) 기본키와 연결된 필드
	@Id
	@NotNull
	@Column(name = "floor_plan_id", nullable = false)
	private String floorPlanId;
	
	//어떤 층의 도면이었는지 알기 위해 만들어둔 필드
	@NotNull
	@Column(name = "floor_id")
	private String floorId;
	
	//최신의 도면이면 true, 이전 도면들은 false
	@NotNull
	@Column(name = "latest")
	private boolean latest;
	
	//MinIO 버킷에 들어갈 이미지 파일 이름과 연결된 필드
	@NotNull
	@Column(name = "floor_plan_file_name", nullable = false)
	private String floorPlanFileName;
	
	//Floor 모델 클래스의 floorPlan 필드 주석처리한 이유와 동일
	//현재까지 구현 상황에서는 DB의 floor_plan과 floor 테이블이 연결되지 않는다.(주된 이유는 클라이언트와 통신할 때 floorPlan(이미지)과 관련된 axios 호출과 floor와 관련된 axios 호출을 나눠서 해주기 때문이다.)
	//따라서 현재까지는 두 테이블이 연결될 필요가 없어 참조되는 필드를 사용하지 않지만 후에 연결하여 호출할 경우가 있을 수도 있기에 주석처리를 해두었다.
	//@OneToOne(mappedBy = "floorPlan")
	//private Floor floor;
	
	public FloorPlan() {

	}
}