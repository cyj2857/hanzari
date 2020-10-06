package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Building;

public interface BuildingService {
	List<Building> findAll();
	
	Building findById(String building_id) throws Exception;
	
	void deleteById(String building_id);
	
	Building save(Building building);
	
	void updateById(String building_id);
}
