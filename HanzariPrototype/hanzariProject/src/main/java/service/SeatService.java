package service;

import java.util.List;

import model.Seat;

public interface SeatService {
	List<Seat> findAll(); // R

	Seat findById(String seat_id) throws Exception; // R

	void deleteById(String seat_id); // D

	Seat save(Seat seat); // C

	void updateById(String seat_id); // U
}