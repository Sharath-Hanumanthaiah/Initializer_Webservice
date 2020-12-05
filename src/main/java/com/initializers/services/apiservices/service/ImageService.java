package com.initializers.services.apiservices.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {
	
	Object addNewImage(List<MultipartFile> images, String type, String id);
	
	//images should be the one that need to be deleted
	Object updateImage(String images, String type, String id);
}
