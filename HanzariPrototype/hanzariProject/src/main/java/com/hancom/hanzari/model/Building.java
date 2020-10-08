package com.hancom.hanzari.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "temp_buildings")
public class Building {

	@Id
	@Column(name = "building_id", nullable = false)
	private String building_id;

	@Column(name = "building_name", nullable = false)
	private String building_name;

	public Building() {
	};
}
