package com.hancom.hanzari.dto;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EmployeeDto {

	@NotBlank
	private String employee_id;

	@NotBlank
	private String authority;

	@NotBlank
	private String status;

	@NotBlank
	private String employee_name;

	@NotBlank
	private String department_name;

	@NotBlank
	private String extension_number;

	private List<String> seatList;

	public EmployeeDto() {
	}
}
