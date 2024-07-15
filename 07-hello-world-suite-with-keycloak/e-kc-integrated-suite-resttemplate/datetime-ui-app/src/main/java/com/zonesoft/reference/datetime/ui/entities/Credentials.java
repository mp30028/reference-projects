package com.zonesoft.reference.datetime.ui.entities;

public record Credentials(String username, String password) {
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("username:"); sb.append(username); 
		sb.append(", password:"); sb.append("*".repeat(password.length()));
		return sb.toString();		
	}
}
