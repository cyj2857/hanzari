package com.hancom.hanzari.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = false) // mappedBy를 통해 Floor의 building로 맵핑이되고 Floor가 관계의 주인임을 나타낸다.
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonManagedReference
	private List<Floor> floors;

	@PreRemove
	public void preRemove() {
		if (floors != null) {
			floors.forEach(e -> e.setBuilding(null));
		}
	}

	public void setFloors(List<Floor> floors) {
		this.floors.clear();
		if (floors != null) {
			this.floors.addAll(floors);
		}
	}

	public void addFloor(Floor floor) {
		floors.add(floor); 
	}

	public void removeFloor(Floor floor) {
		floors.remove(floor);
	}

	public Building() {
	};

	public BuildingDto toDto() {
		return BuildingDto.builder().building_id(buildingId).building_name(buildingName).build();
	}
}