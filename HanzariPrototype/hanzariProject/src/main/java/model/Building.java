package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Getter
@Table(name="temp_buildings")
public class Building {
	
	@Id
	@Column(name = "building_id", nullable = false)
	String building_id;
	
	@Column(name = "building_name", nullable = false)
	String building_name;
}
