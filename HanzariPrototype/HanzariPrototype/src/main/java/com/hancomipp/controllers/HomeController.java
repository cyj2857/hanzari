package com.hancomipp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("")
    public String hello(){
        return "hello";
    }
    
    @GetMapping("/world")
    public String World() {
    	return "world";
    }
}