package com.hancom.hanzari.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "floor_plan")
public class FloorPlan {
	
	@Id
	@Column(name = "building_id", nullable = false)
	private String buildingId;
	
	@Column(name = "floor_id", nullable = false)
	private String floorId;
	
	@Column(name = "floor_plan", nullable = false)
	private String floorPlan;
	
	public FloorPlan() {
		
	}
	
	
}
