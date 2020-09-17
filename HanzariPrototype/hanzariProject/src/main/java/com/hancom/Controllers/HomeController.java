package com.hancom.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
	private JdbcTemplate jdbcTemplate;

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
	
	@GetMapping("/querytest2")
	@ResponseBody
	public Iterable<Employee> getAllEmployee() {
		System.out.println("Employee");
	    List<Employee> result = jdbcTemplate.query(
	            "SELECT * FROM employee",
	            (rs, rowNum) -> new Employee(rs.getString("employee_id"), rs.getString("level"), rs.getString("name"), rs.getObject("department", Department.class), rs.getString("extension_number"))
	    );
	    result.forEach(e -> System.out.println(e.getName().toString()));
	    return result;
	}
	
	@GetMapping("/querytest3")
	@ResponseBody
	public int getAllEmployeeCnt() {
		System.out.println("Employee");
		int result = jdbcTemplate.queryForObject("SELECT COUNT(employee_id) FROM employee", int.class);
	    return result;
	}
}