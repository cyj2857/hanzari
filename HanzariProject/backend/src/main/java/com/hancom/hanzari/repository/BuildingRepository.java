package com.hancom.hanzari.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hancom.hanzari.model.Building;

@Repository
public interface BuildingRepository extends JpaRepository<Building, String> {

}
