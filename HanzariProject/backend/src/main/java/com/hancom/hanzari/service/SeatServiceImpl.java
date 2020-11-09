package com.hancom.hanzari.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.Floor;
import com.hancom.hanzari.model.Seat;
import com.hancom.hanzari.repository.SeatRepository;

@Service
public class SeatServiceImpl implements SeatService {

	@Autowired
	private SeatRepository seatRepository;

	@Override
	public List<Seat> findAll() {
		List<Seat> seats = new ArrayList<Seat>();
		seatRepository.findAll().forEach(e -> seats.add(e));
		return seats;
	}

	@Override
	public Seat findById(String seat_id) throws Exception {
		return seatRepository.findById(seat_id)
				.orElseThrow(() -> new ResourceNotFoundException("Seat", "seat_id", seat_id));
	}

	@Override
	public Seat findByIdNullable(String seatId) {
		Optional<Seat> seat = seatRepository.findById(seatId);
		if (!seat.isPresent()) {
			return null;
		} else {
			return seat.get();
		}
	}

	@Override
	public List<Seat> findByEmployee(Employee employee) throws Exception {
		List<Seat> seats = seatRepository.findByEmployee(employee);
		if (seats != null)
			return seats;
		else
			throw new ResourceNotFoundException("Employee", "employee", employee);
	}

	@Override
	public List<Seat> findByFloor(Floor floor) throws Exception {
		List<Seat> seats = seatRepository.findByFloor(floor);
		if (seats != null)
			return seats;
		else
			throw new ResourceNotFoundException("Floor", "floor", floor);
	}

	@Override
	public void deleteById(String seat_id) {
		seatRepository.deleteById(seat_id);
	}

	@Override
	public Seat save(Seat seat) {
		seatRepository.save(seat);
		return seat;
	}
}
