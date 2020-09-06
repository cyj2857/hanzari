package com.hancom.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    
	//view sample vue.js
	@RequestMapping("/hello")
    public String Hello() {
    	return "hello";
    }
	
	//view first vue.js
	@RequestMapping("/index")
    public String Index() {
    	return "index";
    }
	
	//view HanzariPrototype
    @RequestMapping("/welcome")
    public String Welcome() {
    	return "welcome";
    }
}