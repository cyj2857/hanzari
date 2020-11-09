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
import com.hancom.hanzari.repository.BuildingRepository;

@Service
public class BuildingServiceImpl implements BuildingService {

	@Autowired
	private BuildingRepository buildingRepository;

	// Logger
	private final Logger LOGGER = LoggerFactory.getLogger("EngineLogger");

	@Override
	public List<Building> findAll() {
		List<Building> buildings = new ArrayList<>();
		buildingRepository.findAll().forEach(e -> buildings.add(e));
		return buildings;
	}

	@Override
	public Building findById(String buildingId) throws Exception {
		return buildingRepository.findById(buildingId)
				.orElseThrow(() -> new ResourceNotFoundException("Building", "building_id", buildingId));
	}

	@Override
	public Building findByIdNullable(String buildingId) {
		Optional<Building> building = buildingRepository.findById(buildingId);
		if (!building.isPresent()) {
			LOGGER.info("Building is not exist (buildingId: {})", buildingId);
			return null;
		} else {
			return building.get();
		}
	}

	@Override
	public void deleteById(String buildingId) {
		buildingRepository.deleteById(buildingId);
	}

	@Override
	public Building save(Building building) {
		buildingRepository.save(building);
		return building;
	}
}
