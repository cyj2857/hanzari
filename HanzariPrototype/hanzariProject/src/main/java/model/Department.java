package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name="temp_departments")
public class Department {
	
	@Getter
	@Column(name = "department_id", nullable = false)
	String department_id;
	
	@Column(name = "department_name", nullable = false)
	String department_name;
}