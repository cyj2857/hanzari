package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Employee;

public interface EmployeeService {
	public List<Employee> findAll();

	public Employee findById(String employeeId) throws Exception;
	
	public Employee findByIdNullable(String employeeId);

	public void deleteById(String employeeId);

	public List<Employee> findByDepartmentId(String departmentId);

	public Employee save(Employee employee);
	
	public List<Employee> findByKeyword(String keyword); 
}
