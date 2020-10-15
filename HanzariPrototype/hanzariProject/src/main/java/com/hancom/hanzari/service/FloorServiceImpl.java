package com.hancom.hanzari.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.model.Floor;
import com.hancom.hanzari.repository.FloorRepository;

@Service
public class FloorServiceImpl implements FloorService {

	@Autowired
	private FloorRepository floorRepository;

	@Override
	public List<Floor> findAll() {
		List<Floor> floor = new ArrayList<Floor>();
		floorRepository.findAll().forEach(e -> floor.add(e));
		return floor;
	}

	@Override
	public Floor findByFloorNameAndBuilding(String floorName, Building building) throws Exception {
		Floor floor = floorRepository.findByFloorNameAndBuilding(floorName, building);
		if (floor != null)
			return floor;
		else
			throw new ResourceNotFoundException("Floor", "FloorName", floorName);
	}

	@Override
	public List<Floor> findByBuiding(Building building) {
		List<Floor> floor = floorRepository.findByBuilding(building);
		if (floor != null)
			return floor;
		else
			throw new ResourceNotFoundException("Floor", "building", building);
	}

	@Override
	public void deleteByFloorNameAndBuilding(String floorName, Building building) {
		floorRepository.deleteByFloorNameAndBuilding(floorName, building);

	}

	@Override
	public Floor save(Floor floor) {
		floorRepository.save(floor);
		return floor;
	}

	@Override
	public Floor updateByFloorNameAndBuilding(String floorName, Building building) {
		// TODO Auto-generated method stub
		return null;
	}

}
