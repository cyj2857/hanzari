package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Shape;

public interface ShapeService {
	public List<Shape> findAll();

	public Shape findById(String shape_id) throws Exception;

	public void deleteById(String shape_id);

	public Shape save(Shape shape);

	public void updateById(String shape_id);
}
