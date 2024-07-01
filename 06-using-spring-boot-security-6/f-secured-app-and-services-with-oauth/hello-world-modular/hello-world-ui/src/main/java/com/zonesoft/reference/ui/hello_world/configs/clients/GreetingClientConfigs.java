package com.zonesoft.reference.ui.hello_world.configs.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GreetingClientConfigs {
	
    @Value("${GREETING_SERVICE_PROTOCOL}")
    private String protocol;
    
    @Value("${GREETING_SERVICE_DOMAIN}")
    private String domain;
    
    @Value("${GREETING_SERVICE_PORT}")
    private String port;
    
    @Value("${GREETING_SERVICE_PATH}")
    private String path;
    
    @Value("${GREETING_SERVICE_CLIENT_NAME}")
    private String clientName;
    
    @Value("${GREETING_SERVICE_TYPE}")
    private String clientType ;  
     
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}    

}
