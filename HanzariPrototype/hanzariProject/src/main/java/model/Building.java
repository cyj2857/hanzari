package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="temp_buildings")
public class Building {
	
	@Column(name = "building_id", nullable = false)
	String building_id;
	
	@Column(name = "building_name", nullable = false)
	String building_name;
}
