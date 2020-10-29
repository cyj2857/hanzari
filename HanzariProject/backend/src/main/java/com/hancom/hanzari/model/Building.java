package com.hancom.hanzari.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hancom.hanzari.dto.BuildingDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "buildings")
public class Building {

	@Id
	@NotNull
	@Column(name = "building_id", nullable = false)
	private String buildingId;

	@NotNull
	@Column(name = "building_name", nullable = false)
	private String buildingName;

	@OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, orphanRemoval = true)
	@JsonManagedReference
	private List<Floor> floors;

	@PreRemove
	public void preRemove() {
		if (floors != null) {
			floors.forEach(e -> e.setBuilding(null));
		}
	}

	public Building() {
	};

	public BuildingDto toDto() {
		return BuildingDto.builder().building_id(buildingId).building_name(buildingName).build();
	}
}
