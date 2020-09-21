package com.hancom.hanzariProject.controllers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import model.Department;
import model.Employee;
import model.Figure;

@Controller
public class HomeController {

	private static SessionFactory sessionFactory;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
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
	
	@GetMapping("/inserttest")
	@ResponseBody
	public void InsertTest() {
		
		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		Figure figure = new Figure();
		figure.setFigure_id("6");
		figure.setFigure_name("도형6");

		Session session = sessionFactory.openSession();
		
		try {
			Transaction tx = session.beginTransaction();

			// Query
			session.save(figure); // instead of SQL statement
			
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		
	}
	
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
		figures.forEach(
				it -> 
				result.append(it.toString())
				.append("	id: ").append(it.getFigure_id()).append("\n")
				.append( "			name: ").append(it.getFigure_name()).append("\n")
				.append("<br><br>").append("\n")
				);
		System.out.println("========================result======================");
		System.out.println(result);
		System.out.println("========================result======================");
		return result.toString();		
	}
	
	@GetMapping("/selecttest2")
	@ResponseBody
	public String SelectTest2() {
		
		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		Session session = sessionFactory.openSession();
		List<Department> departments = null;
		try {
			Transaction tx = session.beginTransaction();

			// Query
			Query<Department> theQuery = session.createQuery("from Department order by department_name desc", Department.class);
			departments = theQuery.getResultList();
			
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		StringBuilder result = new StringBuilder();
		departments.forEach(
				it -> 
				result.append(it.toString())
				.append("	id: ").append(it.getDepartment_id()).append("\n")
				.append( "			name: ").append(it.getDepartment_name()).append("\n")
				.append("<br><br>").append("\n")
				);
		System.out.println("========================result======================");
		System.out.println(result);
		System.out.println("========================result======================");
		return result.toString();		
	}
	
	@GetMapping("/selecttest3")
	@ResponseBody
	public String SelectTest3() {
		
		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		Session session = sessionFactory.openSession();
		List<Employee> employee = null;
		try {
			Transaction tx = session.beginTransaction();

			// Query
			Query<Employee> theQuery = session.createQuery("from Employee order by name desc", Employee.class);
			employee = theQuery.getResultList();
			
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		StringBuilder result = new StringBuilder();
		employee.forEach(
				it -> 
				result.append(it.toString())
				.append("	id: ").append(it.getEmployee_id()).append("\n")
				.append( "			name: ").append(it.getName()).append("\n")
				.append( "			departmentName: ").append(it.getDepartment().getDepartment_name()).append("\n")
				.append("<br><br>").append("\n")
				);
		System.out.println("========================result======================");
		System.out.println(result);
		System.out.println("========================result======================");
		return result.toString();		
	}
}