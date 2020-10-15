package com.hancom.hanzari.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hancom.hanzari.dto.FloorDto;

import org.hibernate.annotations.GenericGenerator;

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
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "floor_id", nullable = false)
	private String floorId;

	@Column(name = "floor_name", nullable = false)
	private String floorName;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "building_id", nullable = false)
	private Building building;

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Floor() {
	}

	public FloorDto toDto() {
		String buildingId = (building == null) ? null : building.getBuildingId();
		return FloorDto.builder().floor_name(floorName).building_id(buildingId).build();
	}
}
