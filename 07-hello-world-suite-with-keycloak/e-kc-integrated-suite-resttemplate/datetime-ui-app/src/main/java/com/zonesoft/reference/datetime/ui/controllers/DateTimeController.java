package com.zonesoft.reference.datetime.ui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DateTimeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeController.class);
	
    @Value("${date-time-url}")
    String dateTimeUrl;
    
    private final RestTemplate restTemplate;

    public DateTimeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }	
	
	
	@GetMapping(value={"/show-timestamp"})
	@ResponseBody
	public String showTimestamp() {
		LOGGER.debug("Request to show-timestamp received");
		StringBuilder htmlResponse = new StringBuilder();			
			htmlResponse.append("<h3>");
				htmlResponse.append("Showing Date-Time in UI");
				htmlResponse.append("<h4>");
					htmlResponse.append(getTimestamp());
				htmlResponse.append("</h4>");
				htmlResponse.append("<a href=\"/home\">Home</a><br/>");
		return htmlResponse.toString();
	}		

	
    private String getTimestamp() {
        String dateText = restTemplate
                .getForEntity(dateTimeUrl + "/get-date", String.class)
                .getBody();
        String timeText = restTemplate
                .getForEntity(dateTimeUrl + "/get-time", String.class)
                .getBody();        
        return dateText + " - " + timeText;
    }	
	
}
