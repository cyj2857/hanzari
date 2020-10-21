package com.hancom.hanzari.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hancom.hanzari.dto.FloorDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "floors")
@Builder
public class Floor {
	
	@Id
	@Column(name = "floor_id", nullable = false)
	private String floorId;

	@Column(name = "floor_name", nullable = false)
	private String floorName;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "building_id", nullable = true)
	private Building building;

	@Column(name = "floor_index", nullable = false)
	private int floorIndex;

	@OneToMany
	private List<Seat> seats;
	
	public void setBuilding(Building building) {
		this.building = building;
	}
	
	public Floor() {
	}

	public FloorDto toDto() {
		String buildingId = (building == null) ? null : building.getBuildingId();
		return FloorDto.builder().floor_id(floorId).floor_name(floorName).building_id(buildingId).floor_index(floorIndex).build();
	}
}
