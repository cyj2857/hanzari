package service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exception.ResourceNotFoundException;
import model.Seat;
import repository.SeatRepository;

@Service
public class SeatServiceImpl implements SeatService {

	@Autowired
	private SeatRepository seatRepository;
	
	@Override
	public List<Seat> findAll() {
		List<Seat> seats = new ArrayList<Seat>();
		seatRepository.findAll().forEach(e->seats.add(e));
		return seats;
	}

	@Override
	public Seat findById(String seat_id) throws Exception {
		return seatRepository.findById(seat_id).orElseThrow(()-> new ResourceNotFoundException("Seat", "seat_id", seat_id));
	}

	@Override
	public void deleteById(String seat_id) {
		seatRepository.deleteById(seat_id);
	}

	@Override
	public Seat save(Seat seat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateById(String seat_id) {
		// TODO Auto-generated method stub

	}

}
