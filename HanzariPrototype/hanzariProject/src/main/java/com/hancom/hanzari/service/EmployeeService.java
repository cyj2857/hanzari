package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Employee;

public interface EmployeeService {
	public List<Employee> findAll(); // R

	public Employee findById(String employee_id) throws Exception; // R

	public void deleteById(String employee_id); // D

	public List<Employee> findByDepartmentId(String department_id);

	public Employee save(Employee employee); // C

	public void updateById(String employee_id); // U
}
