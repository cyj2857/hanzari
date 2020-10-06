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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hancom.hanzari.dto.EmployeeDto;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.service.EmployeeService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<EmployeeDto>> getAllEmps() {
		List<Employee> employee = employeeService.findAll();
		List<EmployeeDto> result = new ArrayList<>();
		employee.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
	}
	
	// employee_id로 한명의 사원 조회
    @GetMapping(value = "/{employee_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<EmployeeDto> getEmp(@PathVariable("employee_id") String employee_id) throws Exception {
          return new ResponseEntity<EmployeeDto>(employeeService.findById(employee_id).toDto(), HttpStatus.OK);
    }
}
