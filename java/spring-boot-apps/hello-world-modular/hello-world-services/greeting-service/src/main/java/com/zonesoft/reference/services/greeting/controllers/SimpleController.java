package com.zonesoft.reference.services.greeting.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class SimpleController {
	@GetMapping(value={"/greeting"})
	@ResponseBody
	public ResponseEntity<String> greeting() {
		return ResponseEntity.ok("Hello from greeting service");
	}
}