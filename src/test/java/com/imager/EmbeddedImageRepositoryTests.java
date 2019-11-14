package com.imager;

	
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;


import com.imager.pojo.Image;
import com.imager.repository.ImageRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class EmbeddedImageRepositoryTests {
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	MongoOperations mongoOperations;
	
	@Before
	public void setUp() {
		mongoOperations.dropCollection(Image.class);
		mongoOperations.insert(new Image("bookId1", "bookName1"));
		mongoOperations.insert(new Image("bookId2", "bookName2"));
		mongoOperations.insert(new Image("bookId3", "bookName3"));
	}
	
	@Test
	public void findAllShouldWork() {
		Flux<Image> imageFlux = imageRepository.findAll();
		StepVerifier.create(imageFlux)
			.recordWith(ArrayList::new)
			.expectNextCount(3)
			.consumeRecordedWith(results -> {
					assertThat(results).hasSize(3);
					assertThat(results).extracting(Image::getName)
					.contains("bookName1","bookName2","bookName3");
				}
			).expectComplete()
			.verify();
	}


}
