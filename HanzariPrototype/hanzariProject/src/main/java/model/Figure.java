package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Table(name="temp_figures")
@AllArgsConstructor
public class Figure {
	
	@Column(name = "figure_id", nullable = false)
	String figure_id;
	
	@Column(name = "figure_name", nullable = false)
	@Getter
	String figure_name;
}
