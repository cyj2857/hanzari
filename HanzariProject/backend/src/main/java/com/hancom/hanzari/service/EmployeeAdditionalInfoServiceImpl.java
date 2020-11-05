package com.hancom.hanzari.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.EmployeeAdditionalInfo;
import com.hancom.hanzari.repository.EmployeeAdditionalInfoRepository;

@Service
public class EmployeeAdditionalInfoServiceImpl implements EmployeeAdditionalInfoService {

	@Autowired
	private EmployeeAdditionalInfoRepository employeeAdditionalInfoRepository;

	@Override
	public EmployeeAdditionalInfo findByEmployee(Employee employee) throws Exception {
		return employeeAdditionalInfoRepository.findById(employee.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("EmployeeAdditionalInfo", "employee", employee));
	}

	@Override
	public void deleteByEmployee(Employee employee) {
		employeeAdditionalInfoRepository.deleteById(employee.getEmployeeId());
	}

	@Override
	public EmployeeAdditionalInfo save(EmployeeAdditionalInfo employeeAInfo) {
		return employeeAdditionalInfoRepository.save(employeeAInfo);
	}

}
