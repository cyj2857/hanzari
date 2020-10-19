package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Figure;

public interface FigureService {
	public List<Figure> findAll();

	public Figure findById(String figureId) throws Exception;

	public void deleteById(String figureId);

	public Figure save(Figure figure);
}
