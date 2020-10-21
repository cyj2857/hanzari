package com.hancom.hanzari.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "buildings")
public class Building {

	@Id
	@Column(name = "building_id", nullable = false)
	private String buildingId;

	@Column(name = "building_name", nullable = false)
	private String buildingName;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Floor> floors;
	
	public Building() {
	};
}
