package com.hancom.hanzari.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "shapes")
public class Shape {

	@Id
	@NotNull
	@Column(name = "shape_id", nullable = false)
	private String shapeId; // 그려질 모양의 id

	@NotNull
	@Column(name = "shape_name", nullable = false)
	private String shapeName; // 그려질 모양의 이름 (ex. 네모, 세모, 원, 타원, ...)
	
	@OneToMany(mappedBy = "shape", fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Figure> figures;

	public Shape() {
	};
}