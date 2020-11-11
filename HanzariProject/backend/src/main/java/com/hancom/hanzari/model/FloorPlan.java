package com.hancom.hanzari.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	
	@OneToOne(mappedBy = "floorPlan")
	private Floor floor;
	
	public FloorPlan() {

	}
}