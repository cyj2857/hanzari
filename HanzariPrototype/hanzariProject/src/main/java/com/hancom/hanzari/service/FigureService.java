package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Figure;

public interface FigureService {
	List<Figure> findAll();

	Figure findById(String figure_id) throws Exception;

	void deleteById(String figure_id);

	Figure save(Figure figure);

	void updateById(String figure_id);
}
