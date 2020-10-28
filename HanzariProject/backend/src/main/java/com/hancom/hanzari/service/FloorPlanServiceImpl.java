package com.hancom.hanzari.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.FloorPlan;
import com.hancom.hanzari.repository.FloorPlanRepository;

@Service
public class FloorPlanServiceImpl implements FloorPlanService {
	
	@Autowired
	private FloorPlanRepository floorPlanRepository;

	@Override
	public FloorPlan save(FloorPlan floorPlan) {
		floorPlanRepository.save(floorPlan);
		return floorPlan;
	}

	@Override
	public FloorPlan findByBuildingIdAndFloorId(String buildingId, String floorId) {
		FloorPlan floorPlan = floorPlanRepository.findByBuildingIdAndFloorId(buildingId, floorId);
		if(floorPlan != null)
			return floorPlan;
		else
			throw new ResourceNotFoundException("FloorPlan", "buildingId", buildingId);
	}
	
}