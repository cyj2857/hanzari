package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Building;

public interface BuildingService {
	public List<Building> findAll();
	
	public Building findById(String buildingId) throws Exception;
	
	public Building findByIdNullable(String buildingId);
	
	public void deleteById(String buildingId);
	
	public Building save(Building building);
}
