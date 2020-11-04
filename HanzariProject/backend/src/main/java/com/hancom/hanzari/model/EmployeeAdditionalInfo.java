package com.hancom.hanzari.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "employee_additional_info")
public class EmployeeAdditionalInfo {

	@Id
	@NotNull
	@Column(name = "addi_info_id", nullable = false)
	private String employeeId; // employee_id와 동일

	@NotNull
	@Column(name = "employee_name", nullable = false)
	private String employeeName;

	@NotNull
	@Column(name = "status", nullable = false)
	private String status; // 재직상태. 휴직, 퇴사 등의 정보 표시

	@Column(name = "extension_number", nullable = true)
	private String extensionNumber;

	@Column(name = "department_id", nullable = true)
	private String departmentId;

	@Column(name = "department_name", nullable = true)
	private String departmentName;

	public EmployeeAdditionalInfo() {
	}
}
