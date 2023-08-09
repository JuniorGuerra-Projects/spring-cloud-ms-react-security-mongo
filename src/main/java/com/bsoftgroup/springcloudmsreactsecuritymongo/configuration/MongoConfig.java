package com.bsoftgroup.springcloudmsreactsecuritymongo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@EnableReactiveMongoRepositories
public class MongoConfig extends AbstractReactiveMongoConfiguration {
	
	@Value("${mongo.uri}")
    String mongoUri;

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return "dbseguridad";
	}

	@Bean
	public  MongoClient reactiveMongoClient() {
		 return MongoClients.create(mongoUri);
		
	}

}
