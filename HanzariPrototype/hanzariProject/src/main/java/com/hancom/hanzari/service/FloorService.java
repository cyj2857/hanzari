package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Floor;

public interface FloorService {
	public List<Floor> findAll();

	public Floor findByNameAndBuildingName(String floor_name, String building_name) throws Exception;

	public void deleteByNameAndBuildingName(String floor_name, String building_name);

	public Floor save(Floor floor);

	public Floor updateByNameAndBuildingName(String floor_name, String building_name);
}
// 구현중