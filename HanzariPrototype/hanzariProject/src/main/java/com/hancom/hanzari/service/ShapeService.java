package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Shape;

public interface ShapeService {
	public List<Shape> findAll();

	public Shape findById(String shapeId) throws Exception;

	public void deleteById(String shapeId);

	public Shape save(Shape shape);

	public void updateById(String shapeId);
}
