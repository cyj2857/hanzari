package com.hancom.hanzari.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FloorDto {

	@NotBlank
	private String floor_name;

	@NotBlank
	private String building_id;

	public FloorDto() {
	}
}
