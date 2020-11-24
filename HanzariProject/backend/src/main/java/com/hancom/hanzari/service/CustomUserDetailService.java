package com.hancom.hanzari.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final EmployeeRepository employeeRepository;

	public UserDetails loadUserByUsername(String userPk) {
		return employeeRepository.findById(userPk).orElseThrow(() -> new ResourceNotFoundException("Employee", "employee_id", userPk));
	}
}