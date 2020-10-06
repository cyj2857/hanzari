package com.hancom.hanzari.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.repository.BuildingRepository;

@Service
public class BuildingServiceImpl implements BuildingService {

	@Autowired
	private BuildingRepository buildingRepository;

	@Override
	public List<Building> findAll() {
		List<Building> buildings = new ArrayList<>();
		buildingRepository.findAll().forEach(e -> buildings.add(e));
		return buildings;
	}

	@Override
	public Building findById(String building_id) throws Exception {
		return buildingRepository.findById(building_id)
				.orElseThrow(() -> new ResourceNotFoundException("Building", "building_id", building_id));
	}

	@Override
	public void deleteById(String building_id) {
		buildingRepository.deleteById(building_id);
	}

	@Override
	public Building save(Building building) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateById(String building_id) {
		// TODO Auto-generated method stub

	}

}
