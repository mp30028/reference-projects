package com.zonesoft.reference_projects.hello_world_barebones.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@GetMapping(value={"","/","/home"})
	@ResponseBody
	public String greeting( ) {
			StringBuilder htmlResponse = new StringBuilder();			
			htmlResponse.append("<!DOCTYPE html>");
			htmlResponse.append("<html>");
			htmlResponse.append("<head>");
			htmlResponse.append("<title>Spring Boot Hello World Barebones</title>");
			htmlResponse.append("</head>");
			htmlResponse.append("<body>");
			htmlResponse.append("<h1>Hello World</h1>");
			htmlResponse.append("</body>");
			htmlResponse.append("</html>");
			return htmlResponse.toString();

	}

}