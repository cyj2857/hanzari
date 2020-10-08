package com.hancom.hanzari.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hancom.hanzari.dto.EmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "employee")
public class Employee {

	@Id
	@Column(name = "employee_id", nullable = false)
	private String employee_id;

	@Column(name = "authority", nullable = false)
	private String authority;

	@Column(name = "employee_name", nullable = false)
	private String employee_name;

	@ManyToOne(cascade = CascadeType.ALL) // 관계의 주인
	@JoinColumn(name = "department_id")
	private Department department;

	@Column(name = "extension_number", nullable = true)
	private String extension_number;

	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Column(nullable = true)
	private List<Seat> seat = new ArrayList<Seat>();

	public Employee() {
	}

	public void add(Seat seat) {
		seat.setEmployee(this);
		getSeat().add(seat);
	}

	public List<String> seatIdList() {
		List<String> result = new ArrayList<String>();
		seat.forEach(e -> result.add(e.getSeat_id()));
		return result;
	}
	
	public EmployeeDto toDto() {
		return new EmployeeDto(employee_id, authority, employee_name, department.getDepartment_name(), extension_number, seatIdList());
	}
}
