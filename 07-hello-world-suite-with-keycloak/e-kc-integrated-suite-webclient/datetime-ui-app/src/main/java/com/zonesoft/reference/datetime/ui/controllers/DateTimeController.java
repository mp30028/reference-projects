package com.zonesoft.reference.datetime.ui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class DateTimeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeController.class);
	
    @Value("${date-time-url}")
    String dateTimeUrl = "http://localhost:8078";
    
    private final WebClient webClient;

    public DateTimeController(WebClient webClient) {
        this.webClient = webClient;
    }	    
	
	@GetMapping(value={"/show-timestamp"})
	@ResponseBody
	public String showTimestamp( @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
		LOGGER.debug("Request to show-timestamp received");
		StringBuilder htmlResponse = new StringBuilder();			
			htmlResponse.append("<h3>");
				htmlResponse.append("Showing Date-Time in UI");
				htmlResponse.append("<h4>");
					htmlResponse.append(getTimestamp(authorizedClient));
				htmlResponse.append("</h4>");
				htmlResponse.append("<a href=\"/home\">Home</a><br/>");
		return htmlResponse.toString();
	}		

	private String getTimestamp( OAuth2AuthorizedClient authorizedClient) {
		String dateText = this.invoke("/get-date",authorizedClient);
		String timeText = this.invoke("/get-time",authorizedClient);
		return dateText + " - " + timeText;
	}
	
	private String invoke(String url, OAuth2AuthorizedClient authorizedClient) {
		String result = this.webClient
				.get()
				.uri(dateTimeUrl, uriBuilder -> uriBuilder.path(url).build())
				.attributes(oauth2AuthorizedClient(authorizedClient))
		          .retrieve()
		          .bodyToMono(String.class)				
				.block();
		return result;
	}	
}
