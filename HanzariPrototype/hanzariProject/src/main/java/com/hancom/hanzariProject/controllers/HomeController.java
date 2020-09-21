package com.hancom.hanzariProject.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdk.jshell.Diag;
import model.Department;
import model.Employee;
import model.Figure;

@Controller
public class HomeController {

	@Autowired
	private SessionFactory sessionFactory;

   //view first vue.js->vue.js logo 
   @RequestMapping("/")
    public String Index() {
       return "indexing";
    }
   
   //view HanzariPrototype->한컴 화면 
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
		
	@GetMapping("/testmain")
	@ResponseBody
	public void QueryTestMain() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Figure figure = new Figure();
		figure.setFigure_id("5");
		figure.setFigure_name("도형5");
		
		Session session = sessionFactory.openSession();
		
		try {
		Transaction tx = session.beginTransaction();
		// ...
		tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			sessionFactory.close();
		}
	}
}