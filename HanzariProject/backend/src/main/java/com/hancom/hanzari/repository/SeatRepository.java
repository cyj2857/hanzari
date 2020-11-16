package com.hancom.hanzari.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.Floor;
import com.hancom.hanzari.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {
	List<Seat> findByFloor(Floor floor);

	Seat findBySeatName(String seatName);

	List<Seat> findByEmployee(Employee employee);
}