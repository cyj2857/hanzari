package com.hancom.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
	
	//view first vue.js->vue.js logo 
	@RequestMapping("/")
    public String Index() {
    	return "indexing";
    }
	
	//view HanzariPrototype->한컴 화면 
    @RequestMapping("/welcome")
    public String Welcome() {
    	return "welcome";
    }
}