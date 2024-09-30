package com.zonesoft.reference.services.calendar.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zonesoft.reference.services.calendar.entities.AppInfo;

import java.net.InetAddress;

@RestController
@RequestMapping(value={"/hello-world/api/calendar"})
public class InfoController {
	
	@GetMapping("/status")
	public String home() {
		return "Calendar Services are up and running";
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
		return new AppInfo("Calendar-Services", host, operatingSystem);
	}
}
