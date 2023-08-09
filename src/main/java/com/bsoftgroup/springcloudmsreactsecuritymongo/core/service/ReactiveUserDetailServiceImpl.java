package com.bsoftgroup.springcloudmsreactsecuritymongo.core.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bsoftgroup.springcloudmsreactsecuritymongo.configuration.JwtUsernameAndPasswordAuthenticationFilter;
import com.bsoftgroup.springcloudmsreactsecuritymongo.core.dao.ReactivePersonRepository;
import com.bsoftgroup.springcloudmsreactsecuritymongo.integration.UserToken;

import reactor.core.publisher.Mono;


@Service
public class ReactiveUserDetailServiceImpl implements ReactiveUserDetailsService {
	
	private static final Log logger = LogFactory.getLog(ReactiveUserDetailServiceImpl.class);
	
	private final ReactivePersonRepository daoreactive;

	private final BCryptPasswordEncoder encoder;
	 
	public ReactiveUserDetailServiceImpl(ReactivePersonRepository daoreactive, BCryptPasswordEncoder encoder) {
		//super();
		this.daoreactive = daoreactive;
		this.encoder = encoder;
	}



	@Override
	public Mono<UserDetails> findByUsername(String username) {
		// TODO Auto-generated method stub
		
		
		
	    return daoreactive.findByUsername(username)
	    		.flatMap(
	    		usuario -> {
	    			
	    			if(usuario.getUsername()!=null) {
	    				List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
	    				User user = new User(usuario.getUsername(), encoder.encode(usuario.getPassword()), grantedAuthorities);
	    			
	    				logger.info("existe");
	    				 return Mono.just(user);
	    			}else {
	    				logger.info("no existe");
	    				
	    				 return Mono.just(null);
	    			}
	    		});
	}

}
