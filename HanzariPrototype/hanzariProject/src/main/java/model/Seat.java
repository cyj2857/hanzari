package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_id", nullable = false)
	int seat_id;

	@Column(name = "building_id", nullable = false)
	String building_id;

	@Column(name = "floor", nullable = false)
	String floor;

	@Column(name = "is_group", nullable = false)
	boolean is_group;

	@Column(name = "group_id", nullable = true)
	String group_id;

	@Column(name = "x", nullable = false)
	double x;

	@Column(name = "y", nullable = false)
	double y;

	@Column(name = "figure_id", nullable = true)
	String figure_id;

	public Seat(String building_id, String floor, boolean is_group, String group_id, double x, double y,
			String figure_id) {
		// 생성자
	}
	
	
}
