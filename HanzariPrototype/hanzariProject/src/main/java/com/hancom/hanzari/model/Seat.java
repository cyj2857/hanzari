package com.hancom.hanzari.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hancom.hanzari.dto.SeatDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "seats")
public class Seat {

	@Id
	@Column(name = "seat_id", nullable = false)
	private String seatId;

	@Column(name = "floor", nullable = false)
	private String floor;

	@Column(name = "x", nullable = false)
	private double x;

	@Column(name = "y", nullable = false)
	private double y;

	@Column(name = "is_group", nullable = false)
	private Boolean isGroup;

	@Column(name = "group_id", nullable = true)
	private String groupId;

	@ManyToOne(cascade = CascadeType.ALL) // 관계의 주인
	@JoinColumn(name = "building_id", nullable = false)
	private Building building;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "employee_id") // 관계의 주인
	private Employee employee;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "figure_id", nullable = false)
	private Figure figure;

	public Seat() {
	};

	public Seat(String seat_id, String floor, double x, double y, Boolean isGroup, String groupId) {
		this.seatId = seat_id;
		this.floor = floor;
		this.x = x;
		this.y = y;
		this.isGroup = isGroup;
		this.groupId = groupId;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	public SeatDto toDto() {
		String employeeId = (employee == null) ? null : String.valueOf(employee.getEmployeeId());
		return new SeatDto(seatId, floor, x, y, isGroup, groupId, building.getBuildingId(), employeeId,
				figure.getWidth(), figure.getHeight(), figure.getDegree(), figure.getShape().getShapeId());
	}
}
