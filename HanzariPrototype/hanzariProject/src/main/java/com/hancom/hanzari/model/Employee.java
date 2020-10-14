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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "additionalInfo", nullable = true)
	private EmployeeAdditionalInfo additionalInfo;

	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Column(nullable = true)
	private List<Seat> seat = new ArrayList<Seat>();

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
		seat.forEach(e -> result.add(e.getSeat_id()));
		return result;
	}

	public EmployeeDto toDto() {
		String status = (additionalInfo == null) ? null : additionalInfo.getStatus();
		String employee_name = (additionalInfo == null) ? null : additionalInfo.getEmployee_name();
		String department_name = (additionalInfo == null) ? null : additionalInfo.getDepartment_name();
		String extension_number = (additionalInfo == null) ? null : additionalInfo.getExtension_number();
		return EmployeeDto.builder().employee_id(employee_id).authority(authority).status(status)
				.employee_name(employee_name).department_name(department_name).extension_number(extension_number)
				.seatList(seatIdList()).build();
	}
}
