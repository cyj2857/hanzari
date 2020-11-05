package com.hancom.hanzari.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hancom.hanzari.dto.BuildingDto;
import com.hancom.hanzari.dto.FloorDto;
import com.hancom.hanzari.dto.SeatDto;
import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.EmployeeAdditionalInfo;
import com.hancom.hanzari.model.Floor;
import com.hancom.hanzari.model.Seat;
import com.hancom.hanzari.model.Shape;
import com.hancom.hanzari.service.BuildingService;
import com.hancom.hanzari.service.EmployeeService;
import com.hancom.hanzari.service.FloorService;
import com.hancom.hanzari.service.SeatService;
import com.hancom.hanzari.service.ShapeService;

/*
 * TEST에 필요한 메소드들을 모아놓은 Controller
*/
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("test-api/")
public class TestController {

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
	
	private final Logger LOGGER = LoggerFactory.getLogger("EngineLogger");
	

	@Transactional
	@GetMapping("/inserttestdata")
	public void InsertTestData() {

		List<Shape> shapes = new ArrayList<Shape>();
		shapes.add(new Shape("1", "네모", null));
		shapes.add(new Shape("2", "세모", null));
		shapes.add(new Shape("3", "원", null));
		shapes.add(new Shape("4", "타원", null));

		List<Building> buildings = new ArrayList<Building>();
		buildings.add(new Building("HANCOM01", "한글과컴퓨터본사", null));
		buildings.add(new Building("HANCOM02", "한글과컴퓨터사옥1", null));
		buildings.add(new Building("HANCOM03", "한글과컴퓨터사옥2", null));
		buildings.add(new Building("HANSUNG01", "한성대학교", null));

		List<EmployeeAdditionalInfo> additionalInfo = new ArrayList<EmployeeAdditionalInfo>();
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("10052204").employeeName("김인사").status("재직")
				.extensionNumber("111-0001").departmentId("1").departmentName("부서1").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101101").employeeName("심청일").status("재직")
				.extensionNumber("333-0002").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101102").employeeName("심청이").status("휴직")
				.extensionNumber("333-0003").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101103").employeeName("심청삼").status("재직")
				.extensionNumber("333-0004").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101104").employeeName("심청넷").status("재직")
				.extensionNumber("333-0005").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101199").employeeName("박팀장").status("재직")
				.extensionNumber("444-0003").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("20091101").employeeName("홍길일").status("재직")
				.extensionNumber("222-0002").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("20091102").employeeName("홍길둘").status("재직")
				.extensionNumber("222-0003").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("20091103").employeeName("홍길삼").status("재직")
				.extensionNumber("222-0004").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("20091104").employeeName("홍길넷").status("재직")
				.extensionNumber("222-0005").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("94111201").employeeName("이팀장").status("재직")
				.extensionNumber("222-0001").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("95032205").employeeName("박팀장").status("재직")
				.extensionNumber("333-0001").departmentId("3").departmentName("부서3").build());

		List<Employee> employee = new ArrayList<Employee>();
		employee.add(Employee.builder().employeeId("10052204").authority("admin").additionalInfo(additionalInfo.get(0))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101101").authority("viewer").additionalInfo(additionalInfo.get(1))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101102").authority("viewer").additionalInfo(additionalInfo.get(2))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101103").authority("viewer").additionalInfo(additionalInfo.get(3))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101104").authority("viewer").additionalInfo(additionalInfo.get(4))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101199").authority("viewer").additionalInfo(additionalInfo.get(5))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("20091101").authority("viewer").additionalInfo(additionalInfo.get(6))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("20091102").authority("viewer").additionalInfo(additionalInfo.get(7))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("20091103").authority("viewer").additionalInfo(additionalInfo.get(8))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("20091104").authority("viewer").additionalInfo(additionalInfo.get(9))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("94111201").authority("manager")
				.additionalInfo(additionalInfo.get(10)).seat(null).build());
		employee.add(Employee.builder().employeeId("95032205").authority("manager")
				.additionalInfo(additionalInfo.get(11)).seat(null).build());

		shapes.forEach(e -> shapeService.save(e));
		buildings.forEach(e -> buildingService.save(e));
		employee.forEach(e -> employeeService.save(e));
	}
	
	@Transactional
	@RequestMapping("/get-all-buildings")
	public ResponseEntity<List<BuildingDto>>  getAllBuildings() throws Exception {

		LOGGER.info("TestController.getAllSeats called.");
		List<Building> buildings = buildingService.findAll();
		List<BuildingDto> result = new ArrayList<>();
		buildings.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<BuildingDto>>(result, HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping("/get-all-floors")
	public ResponseEntity<List<FloorDto>>  getAllFloors() throws Exception {

		LOGGER.info("TestController.getAllSeats called.");
		List<Floor> floors = floorService.findAll();
		List<FloorDto> result = new ArrayList<>();
		floors.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<FloorDto>>(result, HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping("/get-all-seats")
	public ResponseEntity<List<SeatDto>>  getAllSeats() throws Exception {

		LOGGER.info("TestController.getAllSeats called.");
		List<Seat> seats = seatService.findAll();
		List<SeatDto> result = new ArrayList<>();
		seats.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<SeatDto>>(result, HttpStatus.OK);
	}
	
	
	
}