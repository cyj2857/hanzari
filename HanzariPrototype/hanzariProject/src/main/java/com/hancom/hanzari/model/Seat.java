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
	String seat_id;

	@Column(name = "floor", nullable = false)
	String floor;

	@Column(name = "x", nullable = false)
	double x;

	@Column(name = "y", nullable = false)
	double y;

	@Column(name = "is_group", nullable = false)
	Boolean is_group;

	@Column(name = "group_id", nullable = true)
	String group_id;

	@ManyToOne(cascade = CascadeType.ALL) // 관계의 주인
	@JoinColumn(name = "building_id")
	Building building;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "employee_id") // 관계의 주인
	Employee employee;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "figure_id")
	Figure figure;

	public Seat() {
	};

	public Seat(String seat_id, String floor, double x, double y, Boolean is_group, String group_id) {
		this.seat_id = seat_id;
		this.floor = floor;
		this.x = x;
		this.y = y;
		this.is_group = is_group;
		this.group_id = group_id;
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
		return new SeatDto(seat_id, floor, x, y, is_group, group_id, building.getBuilding_id(),
				employee.getEmployee_id(), figure.getWidth(), figure.getHeight(), figure.getDegree(),
				figure.getShape().getShape_id());
	}
}
