package com.hancom.hanzari.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.Floor;
import com.hancom.hanzari.model.Seat;
import com.hancom.hanzari.repository.EmployeeRepository;
import com.hancom.hanzari.repository.FloorRepository;
import com.hancom.hanzari.repository.SeatRepository;
import com.hancom.hanzari.util.CSVHelper;
import com.hancom.hanzari.vo.PlacementVo;

@Service
public class SeatServiceImpl implements SeatService {

	@Autowired
	private SeatRepository seatRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private FloorRepository floorRepository;
	// Logger
	private final Logger LOGGER = LoggerFactory.getLogger("EngineLogger");

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
	public Seat findBySeatName(String seatName) throws Exception {
		Seat seat = seatRepository.findBySeatName(seatName);
		if (seat != null)
			return seat;
		else
			throw new ResourceNotFoundException("Seat", "seatName", seatName);
	}

	@Override
	public Seat findBySeatNameAndFloor(String seatName, Floor floor) throws Exception {
		Seat seat = seatRepository.findBySeatNameAndFloor(seatName, floor);
		if (seat != null)
			return seat;
		else
			throw new ResourceNotFoundException("Seat", "seatName", seatName);
	}

	@Override
	public Seat findByIdNullable(String seatId) {
		Optional<Seat> seat = seatRepository.findById(seatId);
		if (!seat.isPresent()) {
			LOGGER.info("Seat is not exist (seatId: {})", seatId);
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

	public List<Seat> save(MultipartFile file) {
		LOGGER.info("SeatService.save(MultipartFile file) called.");
		try {
			List<PlacementVo> placements = CSVHelper.csvToSeat(file.getInputStream());
			List<Seat> seats = new ArrayList<Seat>();

			// TODO 지금 for문안에서 여러 repository에 대해 각각 접근하여 처리하는데 이부분을 깔끔하게 가다듬을 필요가 있어보임.
			for (PlacementVo placement : placements) {
				Optional<Floor> floor = floorRepository.findById(placement.getFloor());
				if (!floor.isPresent()) {
					// TODO 실패 처리
				}
				Seat seat = seatRepository.findBySeatNameAndFloor(placement.getSeatName(), floor.get());
				// TODO Seat이 안찾아지는 경우도 생각해봐야할 듯 
				Optional<Employee> employee = null;
				employee = employeeRepository.findById(placement.getEmployeeId());
				if (!employee.isPresent()) {
					// TODO 실패 처리
					System.out.println("################### employee not found ###################");
					seat.setEmployee(null);
				} else {
					seat.setEmployee(employee.get());
				}

				seats.add(seat);
				LOGGER.info("Seat added. (seatId: {}, seat.seatName: {}, placement.seatName: {})", seat.getSeatId(),
						seat.getSeatName(), placement.getSeatName());
			}
			return seatRepository.saveAll(seats);

		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

}
