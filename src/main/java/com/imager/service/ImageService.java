package com.imager.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import com.imager.pojo.Image;
import com.imager.repository.ImageRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ImageService {

	private static final String UPLOAD_ROOT = "upload-dir";
	
	private ResourceLoader resourceLoader;
	
	private ImageRepository imageRepository;
	

	public ImageService(ResourceLoader resourceLoader, ImageRepository imageRepository) {
		super();
		this.resourceLoader = resourceLoader;
		this.imageRepository = imageRepository;
	}

	/*
	 * @Bean CommandLineRunner setUp() throws IOException{ return (args) -> {
	 * FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
	 * Files.createDirectory(Paths.get(UPLOAD_ROOT));
	 * FileCopyUtils.copy("Test file1", new FileWriter(UPLOAD_ROOT+"/swathy.jpg"));
	 * FileCopyUtils.copy("Test file2", new FileWriter(UPLOAD_ROOT+"/skanda.jpg"));
	 * }; }
	 */

	
	
	
	/*
	 * public Flux<Image> findAllImages() { try { return Flux.fromIterable(
	 * Files.newDirectoryStream(Paths.get(UPLOAD_ROOT))) .map(path -> new
	 * Image(path.hashCode(), path.getFileName().toString())); }catch (IOException
	 * e) { return Flux.empty(); } }
	 */
	
	public Mono<Resource> findOneImage(String fileName) {
		return Mono.fromSupplier(()->
		resourceLoader.getResource("file:"+UPLOAD_ROOT+"/"+fileName));
	}
	
	/*
	 * public Mono<Void> createImage(Flux<FilePart> files){ return
	 * files.flatMap(file -> { File tempFile = null; try { tempFile = new
	 * File(Paths.get(UPLOAD_ROOT, file.filename()).toString());
	 * tempFile.createNewFile(); }catch(IOException e) { e.printStackTrace(); }
	 * return file.transferTo(tempFile); }).then(); }
	 */
	
	/*
	 * public Mono<Void> deleteImage(String filename){ return
	 * Mono.fromRunnable(()->{ try {
	 * Files.deleteIfExists(Paths.get(UPLOAD_ROOT,filename)); }catch (IOException e)
	 * { throw new RuntimeException(e); } }); }
	 */
	
	public Flux<Image> findAllImages() {
		return imageRepository.findAll();
	}
	
	public Mono<Void> createImage(Flux<FilePart> files){
		return files.flatMap(file-> {
			Mono<Image> saveDatabaseImage = imageRepository.save(new Image(UUID.randomUUID().toString(), file.filename()));
			Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_ROOT, file.filename()).toFile())
					.log("createImage-pickTarget").map(destFile -> {
					try{
						destFile.createNewFile();
						return destFile;
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
			})
					.log("createImage-newFile")
					.flatMap(file::transferTo)
					.log("createImage-copy");
			return Mono.when(saveDatabaseImage,copyFile);
		})
	.then();
	}
	
	public Mono<Void> deleteImage(String imageName){
		Mono<Void> deleteDatabaseImage = imageRepository.findByName(imageName).flatMap(imageRepository::delete);
		Mono<Void> deleteFile = Mono.fromRunnable(()->{
			try {
				Files.deleteIfExists(Paths.get(UPLOAD_ROOT, imageName));
			} catch (Exception e) {
				// TODO: handle exception
			}
		});
		return Mono.when(deleteDatabaseImage,deleteFile).then();
	}
	
}
