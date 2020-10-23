package com.hancom.hanzari.controllers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.model.Employee;
import com.hancom.hanzari.model.EmployeeAdditionalInfo;
import com.hancom.hanzari.model.Shape;

/*
 * TEST에 필요한 메소드들을 모아놓은 Controller
*/
@Controller
public class TestController {

	@Autowired
	private static SessionFactory sessionFactory;

	// view first vue.js->vue.js logo
	@RequestMapping("/")
	public String Index() {
		return "indexing";
	}

	// view HanzariPrototype->한컴 화면
	@RequestMapping("/test")
	public String Welcome() {
		return "welcome";
	}

	@GetMapping("/texttest")
	@ResponseBody
	public String Blaa() {
		System.out.println("texttest");
		return "hello blaalaa";
	}

	// 여러 더미데이터를 넣어주는 테스트코드
	// hibernate.hbm2ddl.auto=create 로 시작해야함.
	@GetMapping("/inserttestdata")
	@ResponseBody
	public void InsertTestData() {

		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		List<Shape> shapes = new ArrayList<Shape>();
		shapes.add(new Shape("1", "네모", null));
		shapes.add(new Shape("2", "세모", null));
		shapes.add(new Shape("3", "원", null));
		shapes.add(new Shape("4", "타원", null));

		List<Building> buildings = new ArrayList<Building>();
		buildings.add(new Building("HANCOM01", "한글과컴퓨터본사", null));
		buildings.add(new Building("HANCOM02", "한글과컴퓨터사옥1", null));
		buildings.add(new Building("HANCOM03", "한글과컴퓨터사옥2", null));
		buildings.add(new Building("HANSUNG01", "한성대학교", null));

		List<EmployeeAdditionalInfo> additionalInfo = new ArrayList<EmployeeAdditionalInfo>();
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("10052204").employeeName("김인사").status("재직")
				.extensionNumber("111-0001").departmentId("1").departmentName("부서1").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101101").employeeName("심청일").status("재직")
				.extensionNumber("333-0002").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101102").employeeName("심청이").status("휴직")
				.extensionNumber("333-0003").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101103").employeeName("심청삼").status("재직")
				.extensionNumber("333-0004").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101104").employeeName("심청넷").status("재직")
				.extensionNumber("333-0005").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("19101199").employeeName("박팀장").status("재직")
				.extensionNumber("444-0003").departmentId("3").departmentName("부서3").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("20091101").employeeName("홍길일").status("재직")
				.extensionNumber("222-0002").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("20091102").employeeName("홍길둘").status("재직")
				.extensionNumber("222-0003").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("20091103").employeeName("홍길삼").status("재직")
				.extensionNumber("222-0004").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("20091104").employeeName("홍길넷").status("재직")
				.extensionNumber("222-0005").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("94111201").employeeName("이팀장").status("재직")
				.extensionNumber("222-0001").departmentId("2").departmentName("부서2").build());
		additionalInfo.add(EmployeeAdditionalInfo.builder().employeeId("95032205").employeeName("박팀장").status("재직")
				.extensionNumber("333-0001").departmentId("3").departmentName("부서3").build());

		List<Employee> employee = new ArrayList<Employee>();
		employee.add(Employee.builder().employeeId("10052204").authority("admin").additionalInfo(additionalInfo.get(0))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101101").authority("viewer").additionalInfo(additionalInfo.get(1))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101102").authority("viewer").additionalInfo(additionalInfo.get(2))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101103").authority("viewer").additionalInfo(additionalInfo.get(3))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101104").authority("viewer").additionalInfo(additionalInfo.get(4))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("19101199").authority("viewer").additionalInfo(additionalInfo.get(5))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("20091101").authority("viewer").additionalInfo(additionalInfo.get(6))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("20091102").authority("viewer").additionalInfo(additionalInfo.get(7))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("20091103").authority("viewer").additionalInfo(additionalInfo.get(8))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("20091104").authority("viewer").additionalInfo(additionalInfo.get(9))
				.seat(null).build());
		employee.add(Employee.builder().employeeId("94111201").authority("manager")
				.additionalInfo(additionalInfo.get(10)).seat(null).build());
		employee.add(Employee.builder().employeeId("95032205").authority("manager")
				.additionalInfo(additionalInfo.get(11)).seat(null).build());

		Session session = sessionFactory.openSession();
		try {
			Transaction tx = session.beginTransaction();

			// Query
			shapes.forEach(e -> session.save(e));
			buildings.forEach(e -> session.save(e));
			additionalInfo.forEach(e -> session.save(e));
			employee.forEach(e -> session.save(e)); // instead of SQL statement

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
			System.out.println("\n\n======= INSERT TESTDATA DONE =======\n\n\n");
		}

	}
}