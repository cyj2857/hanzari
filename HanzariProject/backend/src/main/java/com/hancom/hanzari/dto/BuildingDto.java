package com.hancom.hanzari.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BuildingDto {
	@NotBlank
	public String building_id;

	@NotBlank
	public String building_name;

	public BuildingDto() {
	}
}
