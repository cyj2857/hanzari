package service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.model.Employee;

import exception.ResourceNotFoundException;
import repository.EmployeeRepository;

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
	public void deleteById(String employee_id) {
		employeeRepository.deleteById(employee_id);
	}

	@Override
	public Employee save(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateById(String employee_id) {
		// TODO Auto-generated method stub
	}

}
