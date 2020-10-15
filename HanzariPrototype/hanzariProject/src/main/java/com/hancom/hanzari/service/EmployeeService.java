package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Employee;

public interface EmployeeService {
	public List<Employee> findAll(); // R

	public Employee findById(String employeeId) throws Exception; // R

	public void deleteById(String employeeId); // D

	public List<Employee> findByDepartmentId(String departmentId);

	public Employee save(Employee employee); // C

	public void updateById(String employeeId); // U
	
	public List<Employee> findByKeyword(String keyword); 
}
