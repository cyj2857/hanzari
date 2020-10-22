package com.hancom.hanzari.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.hancom.hanzari.model.Floor;
import com.hancom.hanzari.model.Seat;
import com.hancom.hanzari.model.Shape;
import com.hancom.hanzari.service.BuildingService;
import com.hancom.hanzari.service.EmployeeService;
import com.hancom.hanzari.service.FloorService;
import com.hancom.hanzari.service.SeatService;
import com.hancom.hanzari.service.ShapeService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/seats")
public class SeatController {

	@Autowired
	private SeatService seatService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ShapeService shapeService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private FloorService floorService;

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
		/*for (Field field : seatDto.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(seatDto);
			System.out.println(field.getName() + " : " + value);
		}*/

		Shape shape = shapeService.findById(seatDto.getShape_id()); // ShapeRepository에서 seatDto의 shape_id를 통해 해당 Shape을
		Figure figure = Figure.builder().figureId(seatDto.getSeat_id()).shape(shape).width(seatDto.getWidth())
				.height(seatDto.getHeight()).degree(seatDto.getDegree()).build();

		Building building = buildingService.findById(seatDto.getBuilding_id());
		Employee employee = null;
		Floor floor = floorService.findByFloorNameAndBuilding(seatDto.getFloor(), building);
		//System.out.println("FLOOR:: " + floor.getFloorId() + " / " + floor.getFloorName());
		if (seatDto.getEmployee_id() != null) {
			employee = employeeService.findById(seatDto.getEmployee_id());
		}

		Seat newSeat = Seat.builder().seatId(seatDto.getSeat_id()).floor(floor).x(seatDto.getX()).y(seatDto.getY())
				.isGroup(seatDto.getIs_group()).groupId(seatDto.getGroup_id()).employee(employee).figure(figure)
				.build();

		return new ResponseEntity<Seat>(seatService.save(newSeat), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{seat_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteSeat(@PathVariable("seat_id") String seat_id) {
		System.out.println("==================================================================");
		System.out.println("seatId: " + seat_id);
		System.out.println("==================================================================");

		// figureService.deleteById(seat_id); // seat_id랑 동일한 값으로 figure_id가 지정된 1:1로
		// 생성된 figure 레코드 선행 삭제
		seatService.deleteById(seat_id);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}
}