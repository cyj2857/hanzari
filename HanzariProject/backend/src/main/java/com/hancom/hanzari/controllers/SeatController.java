package com.hancom.hanzari.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.hancom.hanzari.dto.SeatDto;
import com.hancom.hanzari.exception.ResourceNotFoundException;
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
import com.hancom.hanzari.util.CSVHelper;
import com.hancom.hanzari.vo.PlacementVo;

@CrossOrigin(origins = "*", exposedHeaders = { "Content-Disposition" }, maxAge = 3600)
@RestController
@RequestMapping("api/buildings/{building_id}/floors/{floor_id}/seats")
public class SeatController {

	@Autowired
	private SeatService seatService;
	@Autowired
	private FloorService floorService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ShapeService shapeService;

	// Logger
	private final Logger LOGGER = LoggerFactory.getLogger("EngineLogger");

	@Transactional
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<SeatDto>> getAllSeatsInFloorInBuilding(@PathVariable("building_id") String buildingId,
			@PathVariable("floor_id") String floorId) throws Exception {

		LOGGER.info("SeatController.getAllSeatsInFloorInBuilding called. (building_id : {}, floor_id : {})", buildingId,
				floorId);

		Building building = buildingService.findById(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}
		Floor floor = floorService.findByIdNullable(floorId);
		if (floor == null) {
			throw new ResourceNotFoundException("Floor", "floor_id", floorId);
		}
		List<Seat> seat = seatService.findByFloor(floor);
		List<SeatDto> result = new ArrayList<>();
		seat.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<SeatDto>>(result, HttpStatus.OK);
	}

	// seat_id로 한 자리 조회
	@Transactional
	@GetMapping(value = "/{seat_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<SeatDto> getSeat(@PathVariable("building_id") String buildingId,
			@PathVariable("floor_id") String floorId, @PathVariable("seat_id") String seat_id) throws Exception {

		LOGGER.info("SeatController.getSeat called. (building_id : {}, floor_id : {}, seat_id : {})", buildingId,
				floorId, seat_id);

		Building building = buildingService.findById(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}
		Floor floor = floorService.findById(floorId);
		if (floor == null) {
			throw new ResourceNotFoundException("Floor", "floor_id", floorId);
		}
		return new ResponseEntity<SeatDto>(seatService.findById(seat_id).toDto(), HttpStatus.OK);
	}

	// employee_id로 자리 조회
	@Transactional
	@GetMapping(value = "/by-empid/{employee_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<SeatDto>> getSeatsByEmpId(@PathVariable("building_id") String buildingId,
			@PathVariable("floor_id") String floorId, @PathVariable("employee_id") String employeeId) throws Exception {

		LOGGER.info("SeatController.getSeatsByEmpId called. (building_id : {}, floor_id : {}, emp_id : {})", buildingId,
				floorId, employeeId);

		Building building = buildingService.findById(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}
		Floor floor = floorService.findById(floorId);
		if (floor == null) {
			throw new ResourceNotFoundException("Floor", "floor_id", floorId);
		}
		Employee employee = employeeService.findById(employeeId);
		if (employee == null) {
			throw new ResourceNotFoundException("Employee", "employee_id", floorId);
		}
		List<Seat> seat = seatService.findByEmployee(employee);
		List<SeatDto> result = new ArrayList<SeatDto>();
		seat.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<SeatDto>>(result, HttpStatus.OK);
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Seat> save(@PathVariable("building_id") String buildingId,
			@PathVariable("floor_id") String floorId, @RequestBody SeatDto seatDto) throws Exception {

		LOGGER.info("SeatController.save called. (building_id : {}, floor_id : {}, seat_id : {})", buildingId, floorId,
				seatDto.getSeat_id());

		HttpStatus status = null;
		Building building = buildingService.findByIdNullable(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}
		Floor floor = floorService.findByIdNullable(floorId);
		if (floor == null) {
			throw new ResourceNotFoundException("Floor", "floor_id", floorId);
		}
		Employee employee = null;
		if (seatDto.getEmployee_id() != null) {
			employee = employeeService.findById(seatDto.getEmployee_id());
			if (employee == null) {
				throw new ResourceNotFoundException("Employee", "employee_id", floorId);
			}
		}
		Seat seat = seatService.findByIdNullable(seatDto.getSeat_id());
		if (seat != null) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.CREATED;
		}
		Shape shape = shapeService.findById(seatDto.getShape_id()); // ShapeRepository에서 seatDto의 shape_id를 통해 해당 Shape을
		Figure figure = Figure.builder().figureId(seatDto.getSeat_id()).shape(shape).width(seatDto.getWidth())
				.height(seatDto.getHeight()).degree(seatDto.getDegree()).build();
		seat = Seat.builder().seatId(seatDto.getSeat_id()).seatName(seatDto.getSeat_name()).floor(floor)
				.x(seatDto.getX()).y(seatDto.getY()).isGroup(seatDto.getIs_group()).groupId(seatDto.getGroup_id())
				.employee(employee).figure(figure).build(); // Create Or Update

		return new ResponseEntity<Seat>(seatService.save(seat), status);
	}

	@DeleteMapping(value = "/{seat_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteSeat(@PathVariable("seat_id") String seat_id) {

		LOGGER.info("SeatController.DeleteSeat called. (seat_id : {})", seat_id);
		seatService.deleteById(seat_id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@Transactional
	@PostMapping(value = "/update-by-file", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<SeatDto>> updateByFile(@RequestParam("file") MultipartFile file) throws Exception {

		LOGGER.info("SeatController.updateByFile called.");
		HttpStatus status = null;

		String message = "";
		if (CSVHelper.hasCSVFormat(file)) {
			try {
				List<Seat> seats = seatService.save(file);
				List<SeatDto> result = new ArrayList<SeatDto>();
				seats.forEach(e -> result.add(e.toDto()));
				status = HttpStatus.OK;
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				LOGGER.error("{}", message);
				return new ResponseEntity<List<SeatDto>>(result, status);
			} catch (Exception e) {
				status = HttpStatus.EXPECTATION_FAILED;
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				LOGGER.error("{}", message);
				return new ResponseEntity<List<SeatDto>>(status);
			}
		}
		status = HttpStatus.BAD_REQUEST;
		message = "TYPE ERROR(Not CSV File)!";
		LOGGER.error("{}", message);
		return new ResponseEntity<List<SeatDto>>(status);
	}

	@Transactional
	@GetMapping(value = "/get-csv-file")
	public void exportEachFloorSeatCsvFile(@PathVariable("floor_id") String floorId, HttpServletResponse response) throws Exception {

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=placements_" + currentDateTime + ".csv"; // 파일명을 정해주는 부분. 앞에 attachment; 가 붙는 경우 뒤에오는 filename으로 해당 data를 다운로드 받게 하는 옵션이다.

		response.setContentType("text/csv"); // response contentType을 text/csv로 지정
		response.setHeader(headerKey, headerValue); // header에 

		Floor floor = floorService.findByIdNullable(floorId);
		if (floor == null) {
			throw new ResourceNotFoundException("Floor", "floor_id", floorId);
		}
		List<Seat> seat = seatService.findByFloor(floor);
		List<PlacementVo> placementVos = new ArrayList<PlacementVo>();

		seat.forEach(e -> {
			if (e.getEmployee() != null) {
				placementVos.add(PlacementVo.builder().floor(e.getFloor().getFloorId()).seatName(e.getSeatName())
						.employeeId(e.getEmployee().getEmployeeId()).build());

			} else {
				placementVos.add(PlacementVo.builder().floor(e.getFloor().getFloorId()).seatName(e.getSeatName())
						.employeeId(null).build());
			}
		});

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = CSVHelper.HEADERs;
		String[] nameMapping = { "floor", "seatName", "employeeId" };

		csvWriter.writeHeader(csvHeader);
		for (PlacementVo placementVo : placementVos) {
			csvWriter.write(placementVo, nameMapping);
		}
		csvWriter.close();

	}

}