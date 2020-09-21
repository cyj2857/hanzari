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
	
	@GetMapping("/testmain2")
	@ResponseBody
	public String QueryTestMain2() {
		
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
		return figures.toString();
		
	}
}