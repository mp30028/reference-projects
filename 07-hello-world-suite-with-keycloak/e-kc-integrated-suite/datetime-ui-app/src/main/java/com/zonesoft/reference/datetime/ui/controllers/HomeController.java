package com.zonesoft.reference.datetime.ui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zonesoft.reference.datetime.ui.entities.Credentials;

@RestController
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String baseUrl;

    @PostMapping("/get-token")
    public ResponseEntity<?> getToken(@RequestBody Credentials credentials) {    	
    	String tokenUrl = baseUrl + "/protocol/openid-connect/token";
    	LOGGER.debug("credentials = {}, tokenUrl = {}", credentials, tokenUrl);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("username", credentials.username());
        requestBody.add("password", credentials.password());
        requestBody.add("grant_type", "password");
        requestBody.add("client_secret", clientSecret);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, requestBody, String.class);
        return ResponseEntity.ok(response.getBody());
    }
    
    @GetMapping("/favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }
    
	@GetMapping(value={"/show-greeting"})
	@ResponseBody
	public String showGreeting() {
		LOGGER.debug("Request to show-local-greeting received");
		StringBuilder htmlResponse = new StringBuilder();			
			htmlResponse.append("<h3>");
				htmlResponse.append("Hello from Date-Time UI Application");
				htmlResponse.append("<h4>");
					htmlResponse.append("You have successfully logged in to the Date-Time UI Application");
				htmlResponse.append("</h4>");
				htmlResponse.append("<a href=\"/home\">Home</a><br/>");
		return htmlResponse.toString();
	}	    
    
    
	@GetMapping(value={"","/", "/home"})
	@ResponseBody
	public String home( ) {
		LOGGER.debug("Request for home received");
		StringBuilder htmlResponse = new StringBuilder();
		htmlResponse.append("<h3>");
			htmlResponse.append("Welcome to Date/Time App");
			htmlResponse.append("<h4>");
				htmlResponse.append("Select what you would like to do next ");
			htmlResponse.append("</h4>");
			htmlResponse.append("<a href=\"/show-greeting\">show-local-greeting</a><br/>");
		htmlResponse.append("</h3>");
		return htmlResponse.toString();
	}    
}
