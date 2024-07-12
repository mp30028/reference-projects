package com.zonesoft.reference.get_token.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zonesoft.reference.get_token.entities.Credentials;

@RestController
public class HomeController {

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String baseUrl;

    @PostMapping("/get-token")
    public ResponseEntity<?> getToken(@RequestBody Credentials credentials) {

    	String tokenUrl = baseUrl + "/protocol/openid-connect/token";

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
}
