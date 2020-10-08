package com.hancom.hanzari.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "temp_departments")
public class Department {

	@Id
	@Column(name = "department_id", nullable = false)
	private String department_id;

	@Column(name = "department_name", nullable = false)
	private String department_name;

	public Department() {
	};
}