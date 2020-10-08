package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Seat;

public interface SeatService {
	public List<Seat> findAll(); // R

	public Seat findById(String seat_id) throws Exception; // R

	public List<Seat> findByEmpId(String employee_id);
	
	public void deleteById(String seat_id); // D

	public Seat save(Seat seat); // C

	public void updateById(String seat_id); // U
}