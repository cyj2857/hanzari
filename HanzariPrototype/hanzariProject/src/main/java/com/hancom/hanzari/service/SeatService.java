package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Seat;

public interface SeatService {
	List<Seat> findAll(); // R

	Seat findById(String seat_id) throws Exception; // R

	List<Seat> findByEmpId(String employee_id);
	
	void deleteById(String seat_id); // D

	Seat save(Seat seat); // C

	void updateById(String seat_id); // U
}