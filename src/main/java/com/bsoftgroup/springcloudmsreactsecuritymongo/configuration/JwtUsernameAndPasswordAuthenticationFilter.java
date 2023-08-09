package com.bsoftgroup.springcloudmsreactsecuritymongo.configuration;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;



public class JwtUsernameAndPasswordAuthenticationFilter  extends AuthenticationWebFilter{
	
	private static final Log logger = LogFactory.getLog(JwtUsernameAndPasswordAuthenticationFilter.class);


	private ReactiveAuthenticationManager authenticationManager;
	private ServerAuthenticationFailureHandler authenticationFailureHandler = new ServerAuthenticationEntryPointFailureHandler(
			new HttpBasicServerAuthenticationEntryPoint());
	
		
	public JwtUsernameAndPasswordAuthenticationFilter(ReactiveAuthenticationManager authenticationManager) {
		super(authenticationManager);
		//super.setAuthenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/login"));
		// TODO Auto-generated constructor stub
       
		this.authenticationManager=authenticationManager;
	}
	

	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		if(CorsUtils.isPreFlightRequest(exchange.getRequest())) {
			exchange.getResponse().setStatusCode(exchange.getResponse().getStatusCode().OK);
			exchange.getResponse().setRawStatusCode(exchange.getResponse().getStatusCode().OK.value());
			//return new Authentication();
		}
	   return this.jsonBodyAuthenticationConverter().apply(exchange)
				.switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
				.flatMap((authentication) -> this.authenticationManager.authenticate(authentication))
				.switchIfEmpty(Mono.defer(
						() -> Mono.error(new IllegalStateException("No provider found for " +  this.authenticationManager.getClass()))))
				.flatMap((authentication) -> this.onAuthenticationSuccess(authentication,new WebFilterExchange(exchange, chain)))
				.doOnSuccess((authentication) -> logger.info(LogMessage.format("Authentication exito")))
				.onErrorResume(AuthenticationException.class, (ex) -> this.onAuthenticationFailed(new WebFilterExchange(exchange, chain), ex));
				/*.doOnError(AuthenticationException.class,
						(ex) -> {
							logger.info(LogMessage.format("My Authentication failed: %s", ex.getMessage()));
							this.onAuthenticationFailed(new WebFilterExchange(exchange, chain), ex);
							}
						);*/
				
				
		
	}
	

	public Mono<Void> onAuthenticationFailed(WebFilterExchange webFilterExchange, AuthenticationException ex) {
			return Mono.fromRunnable(() -> {
				ServerWebExchange exchange = webFilterExchange.getExchange();
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
			});
		}

		

	@Override
	protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
		logger.info("onAuthenticationSuccess personalizado");
		ServerWebExchange exchange = webFilterExchange.getExchange();
		ServerHttpResponse response =  exchange.getResponse();
		String secret = "kr1dT1FeLsWVL8Mnm9s4jM3qsRTYXPR3VIRrBF0gr94xBWM9bUBFswmVtSSFCz3dpoRjLgCmzTxpMvvsWU/21w==";
		Long now = System.currentTimeMillis();
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		String token = Jwts.builder()
				.setSubject(authentication.getName())	
				.setIssuedAt(new Date(now))//8:45+1
				.setExpiration(new Date(now + 3600*1000))  // in milliseconds
				.signWith(key)
				.compact();
		
		response.getHeaders().setContentType( new MediaType(MediaType.APPLICATION_JSON));
		response.getHeaders().add( HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
		response.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		response.setStatusCode(response.getStatusCode().OK);
		response.setRawStatusCode(response.getStatusCode().OK.value());
		
		
		
		return super.onAuthenticationSuccess(authentication, webFilterExchange);
		
	}

	
	
    private Function<ServerWebExchange, Mono<Authentication>> jsonBodyAuthenticationConverter() {
        return exchange -> exchange
                .getRequest()
                .getBody()
                .next()
                .flatMap(body -> {
                    try {
                    	UserCredentials creds = new ObjectMapper().readValue(body.asInputStream(), UserCredentials.class);

                        return Mono.just(
                                new UsernamePasswordAuthenticationToken(
                                        creds.getUsername(),
                                        creds.getPassword()
                                )
                        );
                    } catch (IOException e) {
                        return Mono.empty();
                    }
                });
    }
	
    
    
	
	// Clase temporal 
	private static class UserCredentials {
	    private String username, password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	    
	    
	}
}