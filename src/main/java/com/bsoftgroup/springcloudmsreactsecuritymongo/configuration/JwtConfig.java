package com.bsoftgroup.springcloudmsreactsecuritymongo.configuration;

import org.springframework.beans.factory.annotation.Value;

public class JwtConfig {
	
	@Value("${security.jwt.uri:/mssecurity/**}")
	private String Uri;
	
	@Value("${security.jwt.header:Authorization}")
	private String header;
	
	@Value("${security.jwt.prefix:Bearer }")
	private String prefix;
	
	


	public String getUri() {
		return Uri;
	}

	public void setUri(String uri) {
		Uri = uri;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}





}
