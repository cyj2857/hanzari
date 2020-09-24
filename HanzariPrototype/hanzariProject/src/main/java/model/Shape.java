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
@Table(name = "temp_shapes")
@AllArgsConstructor
public class Shape {

	@Id
	@Column(name = "shape_id", nullable = false)
	String shape_id; // 그려질 모양의 id

	@Column(name = "shape_name", nullable = false)
	String shape_name; // 그려질 모양의 이름 (ex. 네모, 세모, 원, 타원, ...)

	public Shape() {
	};
}
