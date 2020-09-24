package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "temp_figures")
public class Figure {

	@Id
	@Column(name = "figure_id", nullable = false)
	String figure_id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shape_id")
	Shape shape; // 그려질 모양에 대한 정보 (ex. 네모, 세모, 원 등)

	@Column(name = "width", nullable = false)
	double width; // 그려질 너비

	@Column(name = "height", nullable = false)
	double height; // 그려질 높이

	@Column(name = "degree", nullable = false)
	double degree; // 그려질 회전율

	public Figure() {
	};
}
