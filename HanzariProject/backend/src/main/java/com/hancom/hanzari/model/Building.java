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
@Table(name = "buildings")
public class Building {

	@Id
	@Column(name = "building_id", nullable = false)
	private String buildingId;

	@Column(name = "building_name", nullable = false)
	private String buildingName;

	public Building() {
	};
}