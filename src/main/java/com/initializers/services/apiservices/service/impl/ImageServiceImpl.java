package com.initializers.services.apiservices.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.initializers.services.apiservices.exception.ImageUploadException;
import com.initializers.services.apiservices.exception.RequiredValueMissingException;
import com.initializers.services.apiservices.model.CloudinaryConfig;
import com.initializers.services.apiservices.model.item.ItemCategory;
import com.initializers.services.apiservices.model.item.ItemDetails;
import com.initializers.services.apiservices.model.item.ItemSubCategory;
import com.initializers.services.apiservices.repo.ItemCategoryRepo;
import com.initializers.services.apiservices.repo.ItemDetailsRepo;
import com.initializers.services.apiservices.repo.ItemSubCategoryRepo;
import com.initializers.services.apiservices.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	private ItemDetailsRepo itemDetailsRepo;
	@Autowired
	private ItemCategoryRepo itemCategoryRepo;
	@Autowired
	private ItemSubCategoryRepo itemSubCategoryRepo;
	@Autowired
	private CloudinaryConfig cloudinaryConfig;

	@Override
	public Object addNewImage(List<MultipartFile> images, String type, String id) {
		List<String> imageLinks = new ArrayList<>();

		switch (type) {
		case "itemDetails":
			ItemDetails itemDetails = itemDetailsRepo.findFirstId(Long.parseLong(id));
			imageLinks = itemDetails.getImageLinks() == null ? new ArrayList<>() : itemDetails.getImageLinks();
			for (MultipartFile image : images) {
				try {
					String imageUrl = cloudinaryConfig.addImage(image);
					if (imageUrl != null) {
						imageLinks.add(imageUrl);
					}
				} catch (IOException e1) {
					throw new ImageUploadException();
				}
			}
			itemDetails.setImageLinks(imageLinks);
			itemDetailsRepo.save(itemDetails);
			break;
		case "itemCategory":
			ItemCategory itemCategory = itemCategoryRepo.findFirstById(Long.parseLong(id));
			for(MultipartFile image : images) {
				try {
					String imageUrl = cloudinaryConfig.addImage(image);
					if (imageUrl != null) {
						itemCategory.setImageLink(imageUrl);
					}
				} catch (IOException e1) {
					throw new ImageUploadException();
				}
				itemCategoryRepo.save(itemCategory);
			}
		case "itemSubcategory": 
			ItemSubCategory itemSubCategory = itemSubCategoryRepo.findFirstById(Long.parseLong(id));
			for(MultipartFile image : images) {
				try {
					String imageUrl = cloudinaryConfig.addImage(image);
					if (imageUrl != null) {
						itemSubCategory.setImageLink(imageUrl);
					}
				} catch (IOException e1) {
					throw new ImageUploadException();
				}
				itemSubCategoryRepo.save(itemSubCategory);
			}
			break;
		default:
			break;
		}
		return "1";
	}

	@Override
	public Object updateImage(String images, String type, String id) {
		if (images == null) {
			throw new RequiredValueMissingException();
		}
		switch (type) {
		case "itemDetails":
			ItemDetails itemDetails = itemDetailsRepo.findFirstId(Long.parseLong(id));
			List<String> updatedImageLink = new ArrayList<>();
			String imageToDelete = images;
			String[] splitBySlash = images.split("/");
			try {
				String splitByPeriod = splitBySlash[splitBySlash.length - 1];
				String[] publicId = splitByPeriod.split("\\.");
				cloudinaryConfig.deleteImage(publicId[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new RequiredValueMissingException();
			}
			if (itemDetails.getImageLinks() != null) {
				for (String image : itemDetails.getImageLinks()) {
					if (!image.equals(imageToDelete)) {
						updatedImageLink.add(image);
					}
				}
			}
			itemDetails.setImageLinks(updatedImageLink);
			itemDetailsRepo.save(itemDetails);
			break;
		case "itemCategory" :
			ItemCategory itemCategory = itemCategoryRepo.findFirstById(Long.parseLong(id));
			itemCategory.setImageLink(null);
			itemCategoryRepo.save(itemCategory);
			splitBySlash = images.split("/");
			try {
				String splitByPeriod = splitBySlash[splitBySlash.length - 1];
				String[] publicId = splitByPeriod.split("\\.");
				cloudinaryConfig.deleteImage(publicId[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new RequiredValueMissingException();
			}
			break;
		case "itemSubcategory" :
			ItemSubCategory itemSubCategory = itemSubCategoryRepo.findFirstById(Long.parseLong(id));
			itemSubCategory.setImageLink(null);
			itemSubCategoryRepo.save(itemSubCategory);
			splitBySlash = images.split("/");
			try {
				String splitByPeriod = splitBySlash[splitBySlash.length - 1];
				String[] publicId = splitByPeriod.split("\\.");
				cloudinaryConfig.deleteImage(publicId[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new RequiredValueMissingException();
			}
			break;
		default:
			break;
		}
		return null;
	}

}
