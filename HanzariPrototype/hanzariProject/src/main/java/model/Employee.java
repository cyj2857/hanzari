package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "employee_id", nullable = false)
	String employee_id;

	@Column(name = "level", nullable = false)
	String level;

	@Column(name = "name", nullable = false)
	String name;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "department_id")
	Department department;

	@Column(name = "extension_number", nullable = true)
	String extension_number;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "seat_id")
	@Column(nullable = true)
	Seat seat;

	public Employee() {
	};
}
