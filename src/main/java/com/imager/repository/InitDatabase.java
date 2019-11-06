package com.imager.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.imager.pojo.Image;


@Component
public class InitDatabase {

	@Bean
	CommandLineRunner init(MongoOperations mongoOperations) {
		return args -> {
			mongoOperations.aggregateAndReturn(Image.class);
			mongoOperations.insert(new Image("1","learning-spring-boot-cover.jpg"));
			mongoOperations.insert(new Image("2","learning-spring-boot-cover2.jpg"));
			mongoOperations.insert(new Image("3","learning-spring-boot-cover3.jpg"));
			mongoOperations.findAll(Image.class).forEach(image -> System.out.println(image.toString()));
			};
		}
}
