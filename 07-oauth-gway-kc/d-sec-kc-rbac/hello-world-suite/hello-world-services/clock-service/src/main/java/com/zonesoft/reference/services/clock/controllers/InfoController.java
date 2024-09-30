package com.zonesoft.reference.services.clock.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zonesoft.reference.services.clock.entities.AppInfo;

import java.net.InetAddress;

@RestController
@RequestMapping(value={"/hello-world/api/clock"})
public class InfoController {
	
	@GetMapping("/status")
	public String home() {
		return "Clock Services are up and running";
	}
	
	@GetMapping("/info")
	public AppInfo status() {
		String host = System.getenv("HOSTNAME");
		String operatingSystem = System.getProperty("os.name");

		if(host == null || host.isEmpty()) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				host = addr.getHostName();
			} catch (Exception e) {
				System.err.println(e);
				host = "Unknown";
			}
		}
		return new AppInfo("Clock-Services", host, operatingSystem);
	}
}
