package com.initializers.services.apiservices.model;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;

@Component
public class CloudinaryConfig {

	private Cloudinary cloudinary;
	
	CloudinaryConfig() {
		cloudinary = new Cloudinary(ObjectUtils.asMap(
				  "cloud_name", "dsywyhhdl",
				  "api_key", "128818145556257",
				  "api_secret", "AXceLRI5c5B0QilhHoQzhAjSQ30"));
//		try {
//			usageLimits();
//			deleteImage();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public Cloudinary getCloudinary() {
		return cloudinary;
	}

	public void setCloudinary(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}
	
	public String addImage(MultipartFile image) throws IOException {
		if(image!= null && !image.isEmpty()) {
			File imageFile;
			Map uploadResult;
			imageFile = Files.createTempFile("temp", image.getOriginalFilename()).toFile();
			image.transferTo(imageFile);
			 uploadResult = cloudinary.uploader().upload(imageFile,
					ObjectUtils.emptyMap());
			 if(uploadResult != null) {
				 return uploadResult.get("url").toString();
			 }
		}
		
		return null;			
	}
	
	public void usageLimits() throws Exception {
		ApiResponse result = this.cloudinary.api().resources(ObjectUtils.emptyMap());
		System.out.println(result.apiRateLimit().getLimit());
		System.out.println(result.apiRateLimit().getRemaining());
		System.out.println(result.apiRateLimit().getReset());
	}
	@Async
	public void deleteImage(String publicId) throws Exception {
		Map result = this.cloudinary.api().deleteResources(Arrays.asList(publicId), ObjectUtils.emptyMap());
		System.out.println("delete image"+ result.toString());
	}
	
	
}
