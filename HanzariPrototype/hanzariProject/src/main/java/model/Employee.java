package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	String employee_id;
	String level;
	String name;
	String department_id;
	String extension_number;
	
	protected Employee() {}
	
}
