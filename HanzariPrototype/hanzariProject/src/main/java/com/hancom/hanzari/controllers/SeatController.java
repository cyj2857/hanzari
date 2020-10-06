package com.hancom.hanzari.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.Seat;

import dto.SeatDto;
import service.EmployeeService;
import service.SeatService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("seats")
public class SeatController {
	@Autowired
	private SeatService seatService;
	@Autowired
	private EmployeeService employeeService;
	
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<SeatDto>> getAllSeats() {
		List<Seat> employee = seatService.findAll();
		List<SeatDto> result = new ArrayList<>();
		employee.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<SeatDto>>(result, HttpStatus.OK);
	}

	// seat_id로 한 자리 조회
	@GetMapping(value = "/{seat_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<SeatDto> getSeat(@PathVariable("seat_id") String seat_id) throws Exception {
		return new ResponseEntity<SeatDto>(seatService.findById(seat_id).toDto(), HttpStatus.OK);
	}

	// employee_id로 자리 조회
	@GetMapping(value = "/by-empid/{employee_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<SeatDto>> getSeatsByEmpId(@PathVariable("employee_id") String employee_id) {
		List<Seat> employee = seatService.findByEmpId(employee_id);
		List<SeatDto> result = new ArrayList<>();
		employee.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<SeatDto>>(result, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Seat> save(@RequestBody SeatDto seatDto) throws Exception{
		/*
		 * Shape shape; // ShapeRepository에서 seatDto의 shape_id를 통해 해당 Shape을 찾아와서 할당
		 * Figure figure = Figure.builder().
		 *  .... 해서 figure 지정.
		 * Building building;  // BuildingRepository에서 seatDto의 buiding_id를 통해 해당 Building을 찾아와서 할당
		 */
		Employee employee = employeeService.findById(seatDto.getEmployee_id());
		// employee_id 에 해당하는 객체가 반환되지 않았을 떄 오류 핸들링에 대해 고심해봐야 함.
		
		Seat seat = new Seat(seatDto.getSeat_id(),seatDto.getFloor(),seatDto.getX(),seatDto.getY(),
				seatDto.getIs_group(),seatDto.getGroup_id());
		 seat.setEmployee(employee);
			
		/*
		 * seat.setBuilding(building);
		 * seat.setFigure(figure);
		 */
		
		return new ResponseEntity<Seat>(seatService.save(seat), HttpStatus.OK);
	}
	
}
