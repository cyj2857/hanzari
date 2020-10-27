package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.Floor;
import com.hancom.hanzari.model.Seat;

public interface SeatService {
	public List<Seat> findAll(); // R

	public Seat findById(String seatId) throws Exception; // R

	public List<Seat> findByEmployee(Employee employee) throws Exception;

	public List<Seat> findByFloor(Floor floor) throws Exception;

	public void deleteById(String seatId); // D

	public Seat save(Seat seat); // C
}