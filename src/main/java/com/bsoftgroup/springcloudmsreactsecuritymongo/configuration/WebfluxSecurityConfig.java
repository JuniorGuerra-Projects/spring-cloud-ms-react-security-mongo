package com.bsoftgroup.springcloudmsreactsecuritymongo.configuration;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

import reactor.core.publisher.Mono;


@EnableWebFluxSecurity
@Configuration
public class WebfluxSecurityConfig {
	
	/*
	@Autowired
	private UserDetailsService userDetailService;
	*/
	
	@Autowired
    private ReactiveUserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	@Autowired
	private JwtConfig jwtConfig;

	@Bean
 	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
 	
 		/*http.cors().and().csrf().disable()
 	         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
 	       // .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
 	        .authorizeRequests().antMatchers(HttpMethod.POST,jwtConfig.getUri()).permitAll()
 	        .anyRequest().authenticated();
 		 http.addFilterBefore(new JwtUsernameAndPasswordAuthenticationFilter(new ProviderManager(authenticationProvider())), UsernamePasswordAuthenticationFilter.class);
 		 */
  /*
   * 	   return http.cors().and().csrf().disable()
   * */
           /*  return http.cors().and().csrf().disable()
            *   .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
           */
		
		
	//		// http.cors().and().csrf(ServerHttpSecurity.CsrfSpec::disable)	

		           http.cors().and().csrf().disable()
		                .exceptionHandling()
		                .accessDeniedHandler((swe, e) -> {
		                    return Mono.fromRunnable(() -> {
		                        swe.getResponse().setStatusCode(HttpStatus.OK);
		                    });
		                })
		                .and()
	                    .httpBasic().disable()
	                    .formLogin().disable()
	                    .logout().disable()
                		.authorizeExchange()
                		.pathMatchers(HttpMethod.POST,jwtConfig.getUri())
                		.permitAll()
                		.and()              		
                        .addFilterBefore(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()), SecurityWebFiltersOrder.AUTHENTICATION);                    
                        
                        	
                        return http.build();
                        
        
   
	
 	
 	}
 	
	 @Bean
	    public ReactiveAuthenticationManager authenticationManager() {
	        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
	                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

	        authenticationManager.setPasswordEncoder(encoder);

	        return authenticationManager;
	    }

	
/*
	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		// @formatter:off
		http.csrf().disable()
			.authorizeExchange((authorize) -> authorize.pathMatchers("/login").permitAll()
				.anyExchange().authenticated()
			)
			.httpBasic(withDefaults())
			.formLogin((form) -> form
				.loginPage("/login")
			);
		// @formatter:on
		return http.build();
	}*/
	
/*
	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
		// @formatter:off
		http.csrf().disable()
			// Demonstrate that method security works
			// Best practice to use both for defense in depth
			.authorizeExchange((authorize) -> authorize
				.anyExchange().permitAll()
			)
			.httpBasic(withDefaults());
		// @formatter:on
		return http.build();
	}
	*/
/*
	@Bean
	MapReactiveUserDetailsService userDetailsService() {
		// @formatter:off
		UserDetails user = User.builder()
				.username("user")
				.password(this.encoder.encode("password"))
				.roles("USER")
			    .build();
		UserDetails admin = User.builder()
			.username("admin")
			.password(this.encoder.encode("password"))
			.roles("ADMIN", "USER")
			.build();
		// @formatter:on
		return new MapReactiveUserDetailsService(user, admin);
	}
*/

	
}
