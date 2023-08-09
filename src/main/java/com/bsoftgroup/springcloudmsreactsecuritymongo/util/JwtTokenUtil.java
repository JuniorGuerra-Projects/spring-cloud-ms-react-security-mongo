package com.bsoftgroup.springcloudmsreactsecuritymongo.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil implements Serializable {
	
	private static Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
	
	private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private static int ACCESS_TOKEN_VALIDITY_SECONDS = 60;
 
	
	public String generateToken(String subjec) {
		//SecretKey key = Keys.hmacShaKeyFor(encodedKeyBytes);
	System.out.println("successfulAuthentication");
	
	Long now = System.currentTimeMillis();
	String token = Jwts.builder()
		.setSubject(subjec)	
		.setIssuedAt(new Date(now))//8:45+1
		.setExpiration(new Date(now + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))  // in milliseconds
		.signWith(key)
		.compact();
	

    return token;
	}

	public boolean tokenValidate(String token) {
		try {

		    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

		    //OK, we can trust this JWT
		    log.info("validación conforme " );
		    return true;

		} catch (JwtException e) {

		   log.info("ESTAMOS VALIDANDO EL TOKEN ACA JWTUTIL= ", e.getMessage());
		   return false;
		}
		
	}

}