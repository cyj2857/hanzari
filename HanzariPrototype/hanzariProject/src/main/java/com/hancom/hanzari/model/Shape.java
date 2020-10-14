package com.hancom.hanzari.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	@Column(name = "shape_id", nullable = false)
	private String shape_id; // 그려질 모양의 id

	@Column(name = "shape_name", nullable = false)
	private String shape_name; // 그려질 모양의 이름 (ex. 네모, 세모, 원, 타원, ...)

	public Shape() {
	};
}
