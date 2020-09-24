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
@Builder
@AllArgsConstructor

@Table(name = "temp_shapes")
public class Shape {

	@Id
	@Column(name = "shape_id", nullable = false)
	String shape_id; // 그려질 모양의 id

	@Column(name = "shape_name", nullable = false)
	String shape_name; // 그려질 모양의 이름 (ex. 네모, 세모, 원, 타원, ...)

	public Shape() {
	};
}
