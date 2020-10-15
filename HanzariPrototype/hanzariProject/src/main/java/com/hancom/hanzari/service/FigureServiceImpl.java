package com.hancom.hanzari.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.Figure;
import com.hancom.hanzari.repository.FigureRepository;

@Service
public class FigureServiceImpl implements FigureService {

	@Autowired
	FigureRepository figureRepository;

	@Override
	public List<Figure> findAll() {
		List<Figure> figures = new ArrayList<>();
		figureRepository.findAll().forEach(e -> figures.add(e));
		return figures;
	}

	@Override
	public Figure findById(String figureId) throws Exception {
		return figureRepository.findById(figureId)
				.orElseThrow(() -> new ResourceNotFoundException("Figure", "figure_id", figureId));

	}

	@Override
	public void deleteById(String figureId) {
		figureRepository.deleteById(figureId);
	}

	@Override
	public Figure save(Figure figure) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateById(String figureId) {
		// TODO Auto-generated method stub

	}

}
