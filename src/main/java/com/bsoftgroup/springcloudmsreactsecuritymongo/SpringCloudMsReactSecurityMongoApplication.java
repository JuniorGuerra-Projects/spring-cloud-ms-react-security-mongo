package com.bsoftgroup.springcloudmsreactsecuritymongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bsoftgroup.springcloudmsreactsecuritymongo.configuration.JwtConfig;



@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudMsReactSecurityMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudMsReactSecurityMongoApplication.class, args);
	}
	
	@Bean
	public JwtConfig jwtConfig() {
        	return new JwtConfig();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	

}
