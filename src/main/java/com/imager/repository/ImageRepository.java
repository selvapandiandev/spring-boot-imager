package com.imager.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.imager.pojo.Image;

import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, String>{
	
	Mono<Image> findByName(String name);
	
}
