package com.hancom.hanzari.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
	@NotNull
	@Column(name = "employee_id", nullable = false)
	private String employeeId;

	@NotNull
	@Column(name = "authority", nullable = false)
	private String authority;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "additionalInfo", nullable = true)
	private EmployeeAdditionalInfo additionalInfo;

	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
	@Column(nullable = true)
	@JsonManagedReference
	private List<Seat> seat; // = new ArrayList<Seat>();

	public Employee() {
	}

	public void add(Seat seat) {
		seat.setEmployee(this);
		getSeat().add(seat);
	}

	public void setAdditionalInfo(EmployeeAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public List<String> seatIdList() {
		List<String> result = new ArrayList<String>();
		seat.forEach(e -> result.add(e.getSeatId()));
		return result;
	}

	public EmployeeDto toDto() {
		String status = (additionalInfo == null) ? null : additionalInfo.getStatus();
		String employeeName = (additionalInfo == null) ? null : additionalInfo.getEmployeeName();
		String departmentName = (additionalInfo == null) ? null : additionalInfo.getDepartmentName();
		String extensionNumber = (additionalInfo == null) ? null : additionalInfo.getExtensionNumber();
		return EmployeeDto.builder().employee_id(employeeId).authority(authority).status(status)
				.employee_name(employeeName).department_name(departmentName).extension_number(extensionNumber)
				.seatList(seatIdList()).build();
	}
}
