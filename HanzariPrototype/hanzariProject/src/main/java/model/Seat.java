package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@Table(name = "seats")
@AllArgsConstructor
public class Seat {

	@Id
	@Column(name = "seat_id", nullable = false)
	String seat_id;

	@ManyToOne(cascade = CascadeType.ALL)
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

	@ManyToOne
	@JoinColumn(name = "employee_id")
	Employee employee;

	public Seat() {
	};
}
