package com.hancom.hanzari.controllers;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hancom.hanzari.exception.SigninFailedException;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.service.EmployeeService;
import com.hancom.hanzari.util.JwtTokenProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = { "1. Sign" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

	private final EmployeeService employeeService;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	@ApiOperation(value = "로그인", notes = "사원번호 회원 로그인을 한다.")
	@PostMapping(value = "/signin")
	public String signin(@ApiParam(value = "회원ID : 사원번호", required = true) @RequestParam String id,
			@ApiParam(value = "비밀번호", required = true) @RequestParam String password) throws Exception {
		Employee employee = employeeService.findById(id);
		if (!passwordEncoder.matches(password, employee.getPassword()))
			throw new SigninFailedException();

		return jwtTokenProvider.createToken(String.valueOf(employee.getEmployeeId()), employee.getRoles());
	}

	@ApiOperation(value = "가입", notes = "회원가입을 한다.")
	@PostMapping(value = "/signup")
	public ResponseEntity<Void> signup(@ApiParam(value = "회원ID : 사원번호", required = true) @RequestParam String id,
			@ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
		System.out.println("############## >> SignController.signup called");
		HttpStatus status = null;
		Employee employee = employeeService.findByIdNullable(id);
		if (employee != null) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.CREATED;
		}

		employee.setPassword(passwordEncoder.encode(password));
		employee.setRoles(Collections.singletonList("ROLE_USER"));
		employeeService.save(employee);

		return new ResponseEntity<Void>(status);
	}
}