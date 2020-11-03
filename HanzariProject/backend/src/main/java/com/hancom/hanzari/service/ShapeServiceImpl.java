package com.hancom.hanzari.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.Shape;
import com.hancom.hanzari.repository.ShapeRepository;

@Service
public class ShapeServiceImpl implements ShapeService {

	@Autowired
	private ShapeRepository shapeRepository;

	@Override
	public List<Shape> findAll() {
		List<Shape> shapes = new ArrayList<Shape>();
		shapeRepository.findAll().forEach(e -> shapes.add(e));
		return shapes;
	}

	@Override
	public Shape findById(String shape_id) throws Exception {
		return shapeRepository.findById(shape_id)
				.orElseThrow(() -> new ResourceNotFoundException("Shape", "shape_id", shape_id));
	}

	@Override
	public void deleteById(String shape_id) {
		shapeRepository.deleteById(shape_id);
	}

	@Override
	public Shape save(Shape shape) {
		// TODO 나중에 도형모양 확장 시 작성해야 할 부분
		return null;
	}
}
