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
@Table(name = "temp_buildings")
@AllArgsConstructor
public class Building {

	@Id
	@Column(name = "building_id", nullable = false)
	String building_id;

	@Column(name = "building_name", nullable = false)
	String building_name;

	public Building() {
	};
}
