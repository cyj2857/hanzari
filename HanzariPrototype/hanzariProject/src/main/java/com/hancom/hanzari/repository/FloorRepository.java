package com.hancom.hanzari.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.model.Floor;

@Repository
public interface FloorRepository extends JpaRepository<Floor, String> {
	List<Floor> findByBuilding(Building building);
	
	Floor findByFloorNameAndBuilding(String floorName, Building building);
}
