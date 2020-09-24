package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@JsonIdentityInfo(generator = IntSequenceGenerator.class, property = "id") // 추가
@Builder
@Table(name = "temp_buildings")
public class Building {

	@Id
	@Column(name = "building_id", nullable = false)
	String building_id;

	@Column(name = "building_name", nullable = false)
	String building_name;

	public Building() {
	};
}
