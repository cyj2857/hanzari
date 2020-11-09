package com.hancom.hanzari.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "figures")
public class Figure {

	@Id
	@NotNull
	@Column(name = "figure_id", nullable = false)
	private String figureId; // seat_id와 동일

	@ManyToOne // 관계의 주인
	@NotNull
	@JoinColumn(name = "shape_id", nullable = false)
	@JsonBackReference
	private Shape shape; // 그려질 모양에 대한 정보 (ex. 네모, 세모, 원 등)

	@NotNull
	@Column(name = "width", nullable = false)
	private double width; // 그려질 너비

	@NotNull
	@Column(name = "height", nullable = false)
	private double height; // 그려질 높이

	@NotNull
	@Column(name = "degree", nullable = false)
	private double degree; // 그려질 회전율

	public Figure() {
	};
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}
}