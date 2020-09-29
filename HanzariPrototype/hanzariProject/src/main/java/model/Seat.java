package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator;

import dto.SeatDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import model.Figure.FigureBuilder;

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

	@ManyToOne(cascade = CascadeType.ALL) // 관계의 주인
	@JoinColumn(name = "building_id")
	Building building;

	@Column(name = "floor", nullable = false)
	String floor;

	@Column(name = "is_group", nullable = false)
	Boolean is_group;

	@Column(name = "group_id", nullable = true)
	String group_id;

	@Column(name = "x", nullable = false)
	double x;

	@Column(name = "y", nullable = false)
	double y;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "figure_id")
	Figure figure;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "employee_id") // 관계의 주인
	Employee employee;

	public Seat() {
	};

	public SeatDto toDto() {
		return new SeatDto(seat_id, floor, x, y, building.getBuilding_id(), employee.getEmployee_id(),
				figure.getWidth(), figure.getHeight(), figure.getDegree(), figure.getShape().getShape_id());
	}
}
