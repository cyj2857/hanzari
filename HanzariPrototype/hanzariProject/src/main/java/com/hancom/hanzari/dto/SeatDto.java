package com.hancom.hanzari.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {

	@NotBlank
	private String seat_id;

	@NotBlank
	private String floor;

	@NotBlank
	private double x;

	@NotBlank
	private double y;

	@NotBlank
	private Boolean is_group;

	private String group_id;

	private String building_id;

	private String employee_id; // nullable

	@NotBlank
	private double width;

	@NotBlank
	private double height;

	@NotBlank
	private double degree;

	@NotBlank
	private String shape_id;
}