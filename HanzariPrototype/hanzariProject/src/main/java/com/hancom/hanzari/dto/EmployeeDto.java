package com.hancom.hanzari.dto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EmployeeDto {

	@NotBlank
	private String employee_id;

	@NotBlank
	private String authority;

	@NotBlank
	private String employee_name;

	@NotBlank
	private String department_name;

	private String extension_number;
	
	private List<String> seatList = new ArrayList<>();
}
