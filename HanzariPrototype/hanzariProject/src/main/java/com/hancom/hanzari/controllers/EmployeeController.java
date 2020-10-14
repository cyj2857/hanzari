package com.hancom.hanzari.controllers;

import java.net.URLDecoder;
import java.net.URLEncoder;
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
    
    // employee_id로 자리 조회
 	@GetMapping(value = "/by-departmentid/{department_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
 	public ResponseEntity<List<EmployeeDto>> getEmpBydepartmentId(@PathVariable("department_id") String department_id) {
 		List<Employee> employee = employeeService.findByDepartmentId(department_id);
 		List<EmployeeDto> result = new ArrayList<>();
 		employee.forEach(e -> result.add(e.toDto()));
 		System.out.println("-----------------\n\n\n" + String.valueOf(department_id) + "-----------------\n\n\n");
        
 		return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
 	}
 	
 	// keyword로 자리 조회 작성중
  	@GetMapping(value = "/keyword/{keyword}", produces = { MediaType.APPLICATION_JSON_VALUE })
  	public ResponseEntity<List<EmployeeDto>> getEmpByKeyword(@PathVariable("keyword") String keyword) throws Exception {
  		System.out.println("before Decode:: " + String.valueOf(keyword));
  		//URLDecoder.decode((URLDecoder.decode(keyword, "8859_1")), "UTF-8"); //방법1
  		String tempKeyword = URLDecoder.decode(keyword, "UTF-8"); //방법1
  		System.out.println("after Decode:: " + String.valueOf(tempKeyword));
  		//new String(param.getBytes("8859_1"), "utf-8"); //방법2
  		/*
  		List<Employee> employee = employeeService.findByKeyword(tempKeyword);
  		List<EmployeeDto> result = new ArrayList<>();
  		employee.forEach(e -> result.add(e.toDto()));
  		return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
  		*/
  		List<EmployeeDto> result = new ArrayList<>();
  		return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
  	}
  	
}
