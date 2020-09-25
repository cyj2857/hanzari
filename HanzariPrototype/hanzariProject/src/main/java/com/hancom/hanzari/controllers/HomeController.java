package com.hancom.hanzari.controllers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import model.Building;
import model.Department;
import model.Employee;
import model.Figure;
import model.Seat;
import model.Shape;

@Controller
public class HomeController {

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
		shapes.add(new Shape("1", "네모"));
		shapes.add(new Shape("2", "세모"));
		shapes.add(new Shape("3", "원"));
		shapes.add(new Shape("4", "타원"));

		List<Figure> figures = new ArrayList<Figure>();
		figures.add(new Figure("1", shapes.get(0), 50, 50, 0));
		figures.add(new Figure("2", shapes.get(0), 50, 50, 0));
		figures.add(new Figure("3", shapes.get(0), 50, 50, 0));
		figures.add(new Figure("4", shapes.get(0), 50, 50, 0));
		figures.add(new Figure("5", shapes.get(1), 50, 50, 0));
		figures.add(new Figure("6", shapes.get(1), 50, 50, 0));
		figures.add(new Figure("7", shapes.get(2), 50, 50, 0));
		figures.add(new Figure("8", shapes.get(2), 50, 50, 0));

		List<Department> departments = new ArrayList<Department>();
		departments.add(new Department("1", "부서1"));
		departments.add(new Department("2", "부서2"));
		departments.add(new Department("3", "부서3"));
		departments.add(new Department("4", "부서4"));
		departments.add(new Department("0", "부서없음"));

		Building building = new Building("HANCOM01", "한글과컴퓨터본사");
		Session session = sessionFactory.openSession();

		List<Employee> employee = new ArrayList<Employee>();
		employee.add(new Employee("10052204", "admin1", "김인사", departments.get(0), "111-0001", null));
		employee.add(new Employee("94111201", "manager", "이팀장", departments.get(1), "222-0001", null));
		employee.add(new Employee("95032205", "manager", "박팀장", departments.get(2), "333-0001", null));
		employee.add(new Employee("20091101", "viewer", "홍길일", departments.get(1), "222-0002", null));
		employee.add(new Employee("20091102", "viewer", "홍길둘", departments.get(1), "222-0003", null));
		employee.add(new Employee("20091103", "viewer", "홍길삼", departments.get(1), "222-0004", null));
		employee.add(new Employee("20091104", "viewer", "홍길넷", departments.get(1), "222-0005", null));
		employee.add(new Employee("19101101", "viewer", "심청일", departments.get(2), "333-0002", null));
		employee.add(new Employee("19101102", "viewer", "심청이", departments.get(2), "333-0003", null));
		employee.add(new Employee("19101103", "viewer", "심청삼", departments.get(2), "333-0004", null));
		employee.add(new Employee("19101104", "viewer", "심청넷", departments.get(2), "333-0005", null));
		employee.add(new Employee("19101199", "viewer", "박팀장", departments.get(2), "444-0003", null));

		List<Seat> seats = new ArrayList<Seat>();
		seats.add(new Seat("0001", building, "10", false, null, 100.5, 100.5, figures.get(0), employee.get(0)));
		seats.add(new Seat("0002", building, "10", false, null, 500.5, 100.5, figures.get(1), employee.get(0)));
		seats.add(new Seat("0003", building, "10", true, "A", 700.5, 200.5, figures.get(2), null));
		seats.add(new Seat("0004", building, "10", true, "A", 750.5, 200.5, figures.get(3), null));
		seats.add(new Seat("0005", building, "6", false, null, 100.5, 100.5, figures.get(4), null));
		seats.add(new Seat("0006", building, "6", false, null, 200.5, 500.5, figures.get(5), null));
		seats.add(new Seat("0007", building, "6", true, "A", 600.5, 600.5, figures.get(6), null));
		seats.add(new Seat("0008", building, "6", true, "A", 650.5, 600.5, figures.get(7), null));

		try {
			Transaction tx = session.beginTransaction();

			// Query
			shapes.forEach(e -> session.save(e));
			figures.forEach(e -> session.save(e)); // instead of SQL statement
			departments.forEach(e -> session.save(e)); // instead of SQL statement
			session.save(building); // instead of SQL statement
			employee.forEach(e -> session.save(e)); // instead of SQL statement
			seats.forEach(e -> session.save(e)); // instead of SQL statement

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
			System.out.println("\n\n\n\n\n\n\n\n");
		}

	}

	// 특정 employee에 할당된 department의 이름을 리턴하는 테스트
	// hibernate.hbm2ddl.auto=update로 두고 진행해야되는 테스트코드
	@GetMapping("/jointest")
	@ResponseBody
	public String JoinTest() {

		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		Session session = sessionFactory.openSession();
		List<Employee> employee = null;
		try {
			Transaction tx = session.beginTransaction();

			// Query
			Query<Employee> theQuery = session.createQuery("from Employee", Employee.class);
			employee = theQuery.getResultList();

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		StringBuilder result = new StringBuilder();
		employee.forEach(it -> result.append(it.toString()).append("	id: ").append(it.getEmployee_id()).append("\n")
				.append("			name: ").append(it.getEmployee_name()).append("\n").append("<br><br>").append("\n")
				.append("dempartment id: ").append(it.getDepartment().getDepartment_id()).append("\n")
				.append("<br><br>").append("\n").append("department name: ")
				.append(it.getDepartment().getDepartment_name()).append("\n").append("<br><br>").append("\n"));
		System.out.println("========================result======================");
		System.out.println(result);
		System.out.println("========================result======================");
		System.out.println("\n\n\n\n\n\n\n\n");
		return result.toString();
	}

	// 특정 employee에 할당된 여러 seat을 리턴하는 테스트
	// hibernate.hbm2ddl.auto=update로 두고 진행해야되는 테스트코드
	@GetMapping("/jointest2")
	@ResponseBody
	public String JoinTest2() {

		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		Session session = sessionFactory.openSession();
		Employee employee = null;
		try {
			Transaction tx = session.beginTransaction();

			// Query
			Query<Employee> theQuery = session.createQuery("from Employee where employee_id=:id", Employee.class);
			theQuery.setParameter("id", "94111201");
			employee = theQuery.getSingleResult();

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		StringBuilder result = new StringBuilder();
		employee.getSeat()
				.forEach(it -> result.append("seat_id: ").append(it.getSeat_id()).append("\n")
						.append("mapped empl_id: ").append(it.getEmployee().getEmployee_id()).append("\n")
						.append("mapped empl_name: ").append(it.getEmployee().getEmployee_name()).append("\n"));
		System.out.println("========================result======================");
		System.out.println(result);
		System.out.println("========================result======================");
		System.out.println("\n\n\n\n\n\n\n\n");
		return result.toString();
	}

	// 단순 SELECT 테스트
	@GetMapping("/selecttest")
	@ResponseBody
	public String SelectTest() {

		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		Session session = sessionFactory.openSession();
		List<Figure> figures = null;
		try {
			Transaction tx = session.beginTransaction();

			// Query
			Query<Figure> theQuery = session.createQuery("from Figure order by figure_name desc", Figure.class);
			figures = theQuery.getResultList();

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		StringBuilder result = new StringBuilder();
		figures.forEach(it -> result.append(it.toString()).append("	id: ").append(it.getFigure_id()).append("\n")
				.append("			name: ").append(it.getShape().getShape_name()).append("\n").append("<br><br>")
				.append("\n"));
		System.out.println("========================result======================");
		System.out.println(result);
		System.out.println("========================result======================");
		return result.toString();
	}

	// 단순 SELECT 테스트2
	@GetMapping("/selecttest2")
	@ResponseBody
	public String SelectTest2() {

		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		Session session = sessionFactory.openSession();
		List<Department> departments = null;
		try {
			Transaction tx = session.beginTransaction();

			// Query
			Query<Department> theQuery = session.createQuery("from Department order by department_name desc",
					Department.class);
			departments = theQuery.getResultList();

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		StringBuilder result = new StringBuilder();
		departments.forEach(it -> result.append(it.toString()).append("	id: ").append(it.getDepartment_id())
				.append("\n").append("			name: ").append(it.getDepartment_name()).append("\n").append("<br><br>")
				.append("\n"));
		System.out.println("========================result======================");
		System.out.println(result);
		System.out.println("========================result======================");
		return result.toString();
	}

	// 단순 SELECT 테스트3
	@GetMapping("/selecttest3")
	@ResponseBody
	public String SelectTest3() {

		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		Session session = sessionFactory.openSession();
		List<Employee> employee = null;
		try {
			Transaction tx = session.beginTransaction();

			// Query
			Query<Employee> theQuery = session.createQuery("from Employee order by employee_name desc", Employee.class);
			employee = theQuery.getResultList();

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		StringBuilder result = new StringBuilder();
		employee.forEach(it -> result.append(it.toString()).append("	id: ").append(it.getEmployee_id()).append("\n")
				.append("			name: ").append(it.getEmployee_name()).append("\n")
				.append("			departmentName: ").append(it.getDepartment().getDepartment_name()).append("\n")
				.append("<br><br>").append("\n"));
		System.out.println("========================result======================");
		System.out.println(result);
		System.out.println("========================result======================");
		return result.toString();
	}
}