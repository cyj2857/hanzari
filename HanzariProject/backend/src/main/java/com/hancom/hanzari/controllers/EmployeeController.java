package com.hancom.hanzari.controllers;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hancom.hanzari.dto.EmployeeDto;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.EmployeeAdditionalInfo;
import com.hancom.hanzari.service.EmployeeService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/employee")
public class EmployeeController {

	private final EmployeeService employeeService;

	// Logger
	private final Logger LOGGER = LoggerFactory.getLogger("EngineLogger");

	// 전체사원 조회
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<EmployeeDto>> getAllEmps() {
		
		LOGGER.info("EmployeeController.getAllEmps called.");

		List<Employee> employee = employeeService.findAll();
		List<EmployeeDto> result = new ArrayList<>();
		employee.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
	}

	// employee_id로 한명의 사원 조회
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@GetMapping(value = "/{employee_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<EmployeeDto> getEmp(@PathVariable("employee_id") String employee_id) throws Exception {

		LOGGER.info("EmployeeController.getEmp called. (employee_id : {} )", employee_id);

		return new ResponseEntity<EmployeeDto>(employeeService.findById(employee_id).toDto(), HttpStatus.OK);
	}

	// department_id로 사원 조회
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@GetMapping(value = "/by-departmentid/{department_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<EmployeeDto>> getEmpBydepartmentId(@PathVariable("department_id") String department_id) {
		
		LOGGER.info("EmployeeController.getEmpBydepartmentId called. (department_id : {} )", department_id);

		List<Employee> employee = employeeService.findByDepartmentId(department_id);
		List<EmployeeDto> result = new ArrayList<>();
		employee.forEach(e -> result.add(e.toDto()));

		return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
	}

	// keyword로 자리 조회
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@GetMapping(value = "/keyword/{keyword}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<EmployeeDto>> getEmpByKeyword(@PathVariable("keyword") String keyword) throws Exception {

		LOGGER.info("EmployeeController.getEmpByKeyword called. (keyword : {} )", keyword);

		keyword = URLDecoder.decode(keyword, "UTF-8"); // 방법1
		List<Employee> employee = employeeService.findByKeyword(keyword);
		List<EmployeeDto> result = new ArrayList<>();
		employee.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@PostMapping
	public ResponseEntity<Employee> save(@RequestBody EmployeeDto employeeDto) throws Exception {
		
		LOGGER.info("EmployeeController.save called. (employee_id : {} )", employeeDto.getEmployee_id());
		
		HttpStatus status = null;
		Employee employee = employeeService.findByIdNullable(employeeDto.getEmployee_id());
		if (employee != null) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.CREATED;
		}
		EmployeeAdditionalInfo additionalInfo = EmployeeAdditionalInfo.builder()
				.employeeId(employeeDto.getEmployee_id()).employeeName(employeeDto.getEmployee_name())
				.status(employeeDto.getStatus()).extensionNumber(employeeDto.getExtension_number())
				.departmentName(employeeDto.getDepartment_name()).build();

		employee = Employee.builder().employeeId(employeeDto.getEmployee_id()).authority(employeeDto.getAuthority())
				.additionalInfo(additionalInfo).build(); // Create Or Update

		return new ResponseEntity<Employee>(employeeService.save(employee), status);
	}

	// employee_id로 삭제
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@DeleteMapping(value = "/{employee_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteEmp(@PathVariable("employee_id") String employee_id) {

		LOGGER.info("EmployeeController.deleteEmp called. (employee_id : {} )", employee_id);
		
		employeeService.deleteById(employee_id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}