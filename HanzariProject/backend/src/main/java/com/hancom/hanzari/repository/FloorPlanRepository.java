package com.hancom.hanzari.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hancom.hanzari.model.FloorPlan;

@Repository
public interface FloorPlanRepository  extends JpaRepository<FloorPlan, String> {

}