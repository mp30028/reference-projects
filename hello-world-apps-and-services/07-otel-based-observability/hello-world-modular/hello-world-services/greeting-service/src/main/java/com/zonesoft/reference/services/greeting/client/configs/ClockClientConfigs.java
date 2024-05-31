package com.zonesoft.reference.services.greeting.client.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.zonesoft.reference.utils.webclient.builder.IClientConfigs;

@Configuration
public class ClockClientConfigs implements IClientConfigs {
	
    @Value("${CLOCK_SERVICE_PROTOCOL}")
    private String protocol;
    
    @Value("${CLOCK_SERVICE_DOMAIN}")
    private String domain;
    
    @Value("${CLOCK_SERVICE_PORT}")
    private String port;
    
    @Value("${CLOCK_SERVICE_PATH}")
    private String path;
    
    @Value("${CLOCK_SERVICE_CLIENT_NAME}")
    private String clientName;
    
    @Value("${CLOCK_SERVICE_TYPE}")
    private String clientType ;

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public String getDomain() {
		return domain;
	}

	@Override
	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String getPort() {
		return port;
	}

	@Override
	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getClientName() {
		return clientName;
	}

	@Override
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Override
	public String getClientType() {
		return clientType;
	}

	@Override
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}    
}
