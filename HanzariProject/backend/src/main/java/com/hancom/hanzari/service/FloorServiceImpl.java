package com.hancom.hanzari.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	// Logger
	private final Logger LOGGER = LoggerFactory.getLogger("EngineLogger");

	@Override
	public List<Floor> findAll() {
		List<Floor> floor = new ArrayList<Floor>();
		floorRepository.findAll().forEach(e -> floor.add(e));
		return floor;
	}

	@Override
	public Floor findById(String floorId) throws Exception {
		return floorRepository.findById(floorId)
				.orElseThrow(() -> new ResourceNotFoundException("Floor", "floor_id", floorId));
	}

	@Override
	public Floor findByIdNullable(String floorId) {
		Optional<Floor> floor = floorRepository.findById(floorId);
		if (!floor.isPresent()) {
			LOGGER.info("Floor is not exist (floorId: {})", floorId);
			return null;
		} else {
			return floor.get();
		}
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
	public Floor findFirstByOrderByFloorOrderDesc() {
		Optional<Floor> floor = Optional.ofNullable(floorRepository.findFirstByOrderByFloorOrderDesc());
		if (!floor.isPresent()) {
			LOGGER.info("Floor is not exist");
			return null;
		} else {
			return floor.get();
		}
	}

	@Override
	public List<Floor> findByBuilding(Building building) throws Exception {
		List<Floor> floor = floorRepository.findByBuilding(building);
		if (floor != null)
			return floor;
		else
			throw new ResourceNotFoundException("Floor", "building", building);
	}

	@Override
	public void deleteById(String floorId) {
		floorRepository.deleteById(floorId);
	}

	@Override
	public void deleteAllInBatch(List<Floor> floors) {
		floorRepository.deleteInBatch(floors);
	}

	@Override
	public void deleteByFloorNameAndBuilding(String floorName, Building building) {
		floorRepository.deleteByFloorNameAndBuilding(floorName, building);

	}

	@Override
	public Floor save(Floor floor) {
		return floorRepository.save(floor);
	}
}
