package com.zonesoft.reference_projects.hello_world_modular.ui.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@GetMapping(value={"","/","/greeting","/hello"})
	@ResponseBody
	public String greeting( ) {


			String username = "mp30028";
			String fullname = "test user";
			String info = "This is some info";
			StringBuilder htmlResponse = new StringBuilder();
			
				htmlResponse.append("<h3>");
					htmlResponse.append("Greetings ");htmlResponse.append(fullname);
					htmlResponse.append("<h4>");
						htmlResponse.append("You have successfully logged in to Hello-World-Modular as ");htmlResponse.append(username);
					htmlResponse.append("</h4>");
				htmlResponse.append("</h3>");
				htmlResponse.append("<br/><br/>");
				htmlResponse.append("<b>");
					htmlResponse.append("Other info available");
				htmlResponse.append("</b>");
				
				htmlResponse.append("<div style=\"width:800px; overflow:auto\">");
					htmlResponse.append("<pre style=\"white-space: pre-wrap\">");
						htmlResponse.append(info);
					htmlResponse.append("</pre>");
				htmlResponse.append("<div/>");

			return htmlResponse.toString();

	}

}