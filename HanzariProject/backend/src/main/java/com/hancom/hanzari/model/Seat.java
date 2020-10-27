package com.hancom.hanzari.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "floor_id", nullable = false) // 관계의 주인
	@JsonBackReference
	private Floor floor;

	@Column(name = "x", nullable = false)
	private double x;

	@Column(name = "y", nullable = false)
	private double y;

	@Column(name = "is_group", nullable = false)
	private Boolean isGroup;

	@Column(name = "group_id", nullable = true)
	private String groupId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id") // 관계의 주인
	@JsonBackReference
	private Employee employee;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "figure_id", nullable = false)
	private Figure figure;

	public Seat() {
	};

	@PreRemove
	public void preRemove() {
		if (figure != null) {
			figure.setShape(null);
		}
		if (employee != null) {
			setEmployee(null);
		}
	}

	public SeatDto toDto() {
		String employeeId = (employee == null) ? null : String.valueOf(employee.getEmployeeId());
		return SeatDto.builder().seat_id(seatId).floor(floor.getFloorName()).x(x).y(y).is_group(isGroup)
				.group_id(groupId).building_id(floor.getBuilding().getBuildingId()).employee_id(employeeId)
				.width(figure.getWidth()).height(figure.getHeight()).degree(figure.getDegree())
				.shape_id(figure.getShape().getShapeId()).build();
	}
}
