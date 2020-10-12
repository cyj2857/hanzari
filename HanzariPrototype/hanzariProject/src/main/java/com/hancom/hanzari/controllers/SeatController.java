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

import com.hancom.hanzari.dto.SeatDto;
import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.Figure;
import com.hancom.hanzari.model.Seat;
import com.hancom.hanzari.model.Shape;
import com.hancom.hanzari.service.BuildingService;
import com.hancom.hanzari.service.EmployeeService;
import com.hancom.hanzari.service.FigureService;
import com.hancom.hanzari.service.SeatService;
import com.hancom.hanzari.service.ShapeService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("seats")
public class SeatController {

	@Autowired
	private SeatService seatService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ShapeService shapeService;
	@Autowired
	private FigureService figureService;
	@Autowired
	private BuildingService buildingService;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<SeatDto>> getAllSeats() {
		List<Seat> seat = seatService.findAll();
		List<SeatDto> result = new ArrayList<>();
		seat.forEach(e -> result.add(e.toDto()));
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
	public ResponseEntity<Seat> save(@RequestBody SeatDto seatDto) throws Exception {

		Shape shape = shapeService.findById(seatDto.getShape_id()); // ShapeRepository에서 seatDto의 shape_id를 통해 해당 Shape을
		Figure figure = Figure.builder().figure_id(seatDto.getSeat_id()).shape(shape).width(seatDto.getWidth())
				.height(seatDto.getHeight()).degree(seatDto.getDegree()).build();
		figureService.save(figure);
		Building building = buildingService.findById(seatDto.getBuilding_id());
		Employee employee = employeeService.findById(seatDto.getEmployee_id()); // read까진 문제없음.
		System.out.println("seat_id:" + seatDto.getSeat_id());
		System.out.println("employee_id:" + seatDto.getEmployee_id());
		System.out.println("group_id:" + seatDto.getGroup_id());
		System.out.println("x:" + seatDto.getX());
		System.out.println("shape_id:" + seatDto.getShape_id());
		System.out.println("employee_name:" + employee.getEmployee_name());
		System.out.println("employee_id:" + employee.getEmployee_id());
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n");

		Seat newSeat = new Seat(seatDto.getSeat_id(), seatDto.getFloor(), seatDto.getX(), seatDto.getY(),
				seatDto.getIs_group(), seatDto.getGroup_id());

		newSeat.setEmployee(employee);
		newSeat.setBuilding(building);
		newSeat.setFigure(figure);
		return new ResponseEntity<Seat>(seatService.save(newSeat), HttpStatus.OK);
	}
}
