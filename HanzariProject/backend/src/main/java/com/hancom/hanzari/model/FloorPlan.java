package com.hancom.hanzari.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@Column(name = "building_id", nullable = false)
	private String buildingId;
	
	@Id
	@Column(name = "floor_id", nullable = false)
	private String floorId;
	
	@Column(name = "floor_plan", nullable = false)
	private String floorPlanId;
	
	public FloorPlan() {

	}
}
