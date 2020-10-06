package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Shape;

public interface ShapeService {
	List<Shape> findAll();

	Shape findById(String shape_id) throws Exception;

	void deleteById(String shape_id);

	Shape save(Shape shape);

	void updateById(String shape_id);
}
