package com.hancom.hanzari.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> findAll() {
		List<Employee> employee = new ArrayList<>();
		employeeRepository.findAll().forEach(e -> employee.add(e));
		return employee;
	}

	@Override
	public Employee findById(String employee_id) throws Exception {
		return employeeRepository.findById(employee_id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "employee_id", employee_id));
	}

	@Override
	public Employee findByIdNullable(String employeeId) {
		Optional<Employee> employee = employeeRepository.findById(employeeId);
		if (!employee.isPresent()) {
			return null;
		} else {
			return employee.get();
		}
	}

	@Override
	public List<Employee> findByDepartmentId(String department_id) {
		List<Employee> employee = new ArrayList<Employee>();
		employeeRepository.findAll().forEach(e -> {
			if (e.getAdditionalInfo().getDepartmentId().toString().equals(department_id)) {
				employee.add(e);
			}
		});
		return employee;
	}

	@Override
	public void deleteById(String employee_id) {
		employeeRepository.deleteById(employee_id);
	}

	@Override
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> findByKeyword(String keyword) {
		List<Employee> employee = new ArrayList<Employee>();
		employeeRepository.findAll().forEach(e -> {
			if (e.getAdditionalInfo().getEmployeeName().toString().contains(keyword)
					|| e.getAdditionalInfo().getDepartmentName().toString().contains(keyword)) {
				employee.add(e);
			}
		});
		return employee;
	}
}
