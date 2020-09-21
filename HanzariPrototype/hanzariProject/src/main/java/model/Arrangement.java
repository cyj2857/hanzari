package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Getter
@Table(name="arrangements")
public class Arrangement {
	
	@JoinColumn(name = "seat_id")
	@Column(name = "seat_id", nullable = false)
	Seat seat;
	
	@JoinColumn(name = "employee_id")
	@Column(name = "employee_id", nullable = false)
	Employee employee;
}
