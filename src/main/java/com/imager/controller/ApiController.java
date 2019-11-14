package com.imager.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.imager.pojo.Image;
import com.imager.service.ImageService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
	
	private static final String BASE_PATH = "/api";
	
	@Autowired
	ImageService imageService;
	
	@GetMapping(BASE_PATH+"/images")
	Flux<Image> images(){
		return Flux.just(
				new Image("1","Scott photo"),
				new Image("2","Selva photo"),
				new Image("3","Swathy photo")
				);
	}
	
	@GetMapping(BASE_PATH+"/images/find/{imageName:.+}")
	Mono<Image> findOneApiImage(@PathVariable String imageName){
		return imageService.findOneApiImage(imageName);
	}
	
	
}
