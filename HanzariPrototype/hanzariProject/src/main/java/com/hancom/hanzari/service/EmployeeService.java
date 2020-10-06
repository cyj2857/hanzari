package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Employee;

public interface EmployeeService {
	List<Employee> findAll(); // R

	Employee findById(String employee_id) throws Exception; // R

	void deleteById(String employee_id); // D

	Employee save(Employee employee); // C

	void updateById(String employee_id); // U
}
