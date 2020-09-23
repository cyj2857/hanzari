package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "temp_departments")
@AllArgsConstructor
public class Department {

	@Id
	@Column(name = "department_id", nullable = false)
	String department_id;

	@Column(name = "department_name", nullable = false)
	String department_name;

	public Department() {
	};
}