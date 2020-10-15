package com.hancom.hanzari.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name= "floors")
public class Floor {
	@Id
	@GeneratedValue
	@Column(name = "floor_id", nullable = false)
	private String floorId;
	
	@Column(name = "floor_name", nullable = false)
	private String floorName;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "building_id", nullable = false)
	private Building building;
}
