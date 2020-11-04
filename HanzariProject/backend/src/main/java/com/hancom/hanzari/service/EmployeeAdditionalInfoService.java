package com.hancom.hanzari.service;

import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.EmployeeAdditionalInfo;

public interface EmployeeAdditionalInfoService {
	public EmployeeAdditionalInfo findByEmployee(Employee employee) throws Exception;
	
	public void deleteByEmployee(Employee employee);
	
	public EmployeeAdditionalInfo save(EmployeeAdditionalInfo employeeAInfo);

}
