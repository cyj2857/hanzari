package com.hancom.hanzari.service;

import org.springframework.stereotype.Service;

@Service
public interface FloorPlanService {
	public String save(String buildingId);
	
	public String findById(String buildingId);
}
