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
@Table(name = "temp_figures")
@AllArgsConstructor
public class Figure {

	@Id
	@Column(name = "figure_id", nullable = false)
	String figure_id;

	@Column(name = "figure_name", nullable = false)
	String figure_name;

	public Figure() {
	};
}
