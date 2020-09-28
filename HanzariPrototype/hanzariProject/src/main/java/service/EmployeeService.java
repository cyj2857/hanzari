package service;

import java.util.List;

import dto.EmployeeDto;

public interface EmployeeService {
	List<EmployeeDto> findAll(); // R
	
	EmployeeDto findById(String employee_id) throws Exception; // R
	
	void deleteById(String employee_id); // D
	
	EmployeeDto save(EmployeeDto employee); // C

	void updateById(String employee_id); // U
}
