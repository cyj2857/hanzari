package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.Floor;
import com.hancom.hanzari.model.Seat;

public interface SeatService {
	public List<Seat> findAll();

	public Seat findById(String seatId) throws Exception;
	
	public Seat findBySeatName(String seatName) throws Exception;

	public Seat findByIdNullable(String seatId);

	public List<Seat> findByEmployee(Employee employee) throws Exception;

	public List<Seat> findByFloor(Floor floor) throws Exception;

	public void deleteById(String seatId);

	public Seat save(Seat seat);
}