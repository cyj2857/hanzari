package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.model.Floor;

public interface FloorService {
	public List<Floor> findAll();

	public Floor findByFloorNameAndBuilding(String floorName, Building building) throws Exception;

	public List<Floor> findByBuiding(Building building);
	
	public void deleteByFloorNameAndBuilding(String floorName, Building building);

	public Floor save(Floor floor);

	public Floor updateByFloorNameAndBuilding(String floorName, Building building);
}
// 구현중