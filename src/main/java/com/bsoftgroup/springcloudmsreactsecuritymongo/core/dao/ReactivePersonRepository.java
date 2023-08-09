package com.bsoftgroup.springcloudmsreactsecuritymongo.core.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ReactivePersonRepository extends ReactiveCrudRepository<Usuario, String>{
	 
	  Mono<Usuario> findByUsername(String username);

	  @Query("{ 'username': ?admin}")
	  Flux<Usuario> findByUsernameQuery(String username);

}