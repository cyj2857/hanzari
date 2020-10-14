package com.hancom.hanzari.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "employee_additional_info")
public class EmployeeAdditionalInfo {
	@Column(name = "employee_name", nullable = false)
	private String employee_name;

	@Column(name = "status", nullable = false)
	private String status;	// 재직상태. 휴직, 퇴사 등의 정보 표시

	@Column(name = "extension_number", nullable = true)
	private String extension_number;
	
	@Column(name = "department_id", nullable = false)
	private String department_id;

	@Column(name = "department_name", nullable = false)
	private String department_name;
}
