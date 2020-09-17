package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="temp_figures")
public class Figure {
	
	@Column(name = "figure_id", nullable = false)
	String figure_id;
	
	@Column(name = "figure_name", nullable = false)
	String figure_name;
}
