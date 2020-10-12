package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Figure;

public interface FigureService {
	public List<Figure> findAll();

	public Figure findById(String figure_id) throws Exception;

	public void deleteById(String figure_id);

	public Figure save(Figure figure);

	public void updateById(String figure_id);
}
