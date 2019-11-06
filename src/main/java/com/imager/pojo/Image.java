package com.imager.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
public class Image {
	@Id
	private String id;
	private String name;
	
	public Image(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

}
