package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "temp_departments")
public class Department {

	@Id
	@Column(name = "department_id", nullable = false)
	String department_id;

	@Column(name = "department_name", nullable = false)
	String department_name;

	public Department() {
	};
}