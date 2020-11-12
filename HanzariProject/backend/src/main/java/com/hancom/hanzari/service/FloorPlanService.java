package com.hancom.hanzari.service;

import com.hancom.hanzari.model.FloorPlan;

public interface FloorPlanService {
	public FloorPlan save(FloorPlan floorPlan);
	
	//public FloorPlan findByBuildingIdAndFloorId(String buildingId, String floorId) throws Exception;
	//public FloorPlan findByFloorPlanId(String floorPlanId) throws Exception;
	
	public FloorPlan findByFloorIdAndLatest(String floorId, Boolean latest);
}
