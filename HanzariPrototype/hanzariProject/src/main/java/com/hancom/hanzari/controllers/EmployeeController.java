package com.hancom.hanzari.controllers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import model.Department;
import model.Employee;
import model.Seat;

@Api(tags = { "Employee" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class EmployeeController {

	@Autowired
	private SessionFactory sessionFactory;

	@ApiOperation(value = "사원 조회", notes = "모든 사원을 조회한다")
	@GetMapping(value = "/employee")
	public List<Employee> findAllEmployee() {
		Session session = sessionFactory.openSession();
		List<Employee> employee = null;
		try {
			Transaction tx = session.beginTransaction();

			Query<Employee> theQuery = session.createQuery("from Employee", Employee.class);
			employee = theQuery.getResultList();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}

		return employee;
	}

	@ApiOperation(value = "특정 사원 조회", notes = "특정 사원을 조회한다")
	@GetMapping(value = "/employee/{employee_id}")
	public Employee findEmployeeById(@PathVariable(value = "employee_id") String employee_id) {
		Session session = sessionFactory.openSession();
		Employee employee = null;
		try {
			Transaction tx = session.beginTransaction();

			Query<Employee> theQuery = session.createQuery("from Employee where employee_id=:id", Employee.class);
			theQuery.setParameter("id", employee_id);
			employee = theQuery.getSingleResult();
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}

		return employee;
	}

	@ApiOperation(value = "특정 사원 조회", notes = "특정 사원을 조회한다")
	@GetMapping(value = "/employee/{employee_id}/seats")
	public List<Seat> findSeatsByEmployeeId(@PathVariable(value = "employee_id") String employee_id) {
		Session session = sessionFactory.openSession();
		List<Seat> seats = null;
		try {
			Transaction tx = session.beginTransaction();

			Query<Employee> theQuery = session.createQuery("from Employee where employee_id=:id", Employee.class);
			theQuery.setParameter("id", employee_id);
			seats = theQuery.getSingleResult().getSeat();
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}

		return seats;
	}

	@ApiOperation(value = "사원 입력", notes = "사원을 입력한다.")
	@PostMapping(value = "/employee")
	public Employee save(@ApiParam(value = "사원아이디", required = true) @RequestParam String employee_id,
			@ApiParam(value = "권한", required = true) @RequestParam String authority,
			@ApiParam(value = "사원이름", required = true) @RequestParam String employee_name,
			@ApiParam(value = "부서", required = true) @RequestParam Department department,
			@ApiParam(value = "내선번호", required = false) @RequestParam String extension_number,
			@ApiParam(value = "좌석", required = false) @RequestParam Seat seat) {
		Session session = sessionFactory.openSession();
		Employee employee = Employee.builder().employee_id(employee_id).authority(authority)
				.employee_name(employee_name).department(department).extension_number(extension_number).build();

		try {
			Transaction tx = session.beginTransaction();

			session.save(employee);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		return employee;
	}

}