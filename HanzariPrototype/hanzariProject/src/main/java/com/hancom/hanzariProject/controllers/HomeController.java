package com.hancom.hanzariProject.controllers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@GetMapping("/querytest")
	@ResponseBody
	public Iterable<Figure> getAllFigures() {
		
		System.out.println("Figure");
	    List<Figure> result = jdbcTemplate.query(
	            "SELECT * FROM temp_figures",
	            (rs, rowNum) -> new Figure(rs.getString("figure_id"), rs.getString("figure_name"))
	    );
	    result.forEach(e -> System.out.println(e.getFigure_name().toString()));
	    return result;
	}
	
	@GetMapping("/testmain")
	@ResponseBody
	public void QueryTestMain() {
		
		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		Figure figure = new Figure();
		figure.setFigure_id("5");
		figure.setFigure_name("도형5");
		
		System.out.println("1");

		Session session = sessionFactory.openSession();
		System.out.println("2");
		
		try {
			Transaction tx = session.beginTransaction();
			System.out.println("3");

			session.save(figure); // instead of SQL statement
			System.out.println("4");

			tx.commit();
			System.out.println("5");

		} catch (Exception e) {
			System.out.println("6");
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
		
	}
}