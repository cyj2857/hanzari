package com.hancom.hanzari.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hancom.hanzari.model.Floor;

@Repository
public interface FloorRepository extends JpaRepository<Floor, String> {

}
