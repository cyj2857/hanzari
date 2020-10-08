package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Building;

public interface BuildingService {
	public List<Building> findAll();
	
	public Building findById(String building_id) throws Exception;
	
	public void deleteById(String building_id);
	
	public Building save(Building building);
	
	public void updateById(String building_id);
}
