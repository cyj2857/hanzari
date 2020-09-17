package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@AllArgsConstructor
@Table(name="employee")
public class Employee {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "employee_id", nullable = false)
	@Getter
	String employee_id;
	
	@Column(name = "level", nullable = false)
	String level;
	
	@Column(name = "name", nullable = false)
	@Getter
	String name;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "department_id")
	@Column(name = "department_id", nullable = true)
	Department department;
	
	@Column(name = "extension_number", nullable = true)
	String extension_number;
}
