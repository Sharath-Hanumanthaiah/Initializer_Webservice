package com.initializers.services.apiservices.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.exception.ItemNotFoundException;
import com.initializers.services.apiservices.exception.UserNotFoundException;
import com.initializers.services.apiservices.model.UserWishlist;
import com.initializers.services.apiservices.model.UserWishlistTemp;
import com.initializers.services.apiservices.model.item.ItemListTemp;
import com.initializers.services.apiservices.repo.UserWishlistRepo;
import com.initializers.services.apiservices.service.ItemDetailsService;
import com.initializers.services.apiservices.service.UserDetailsService;
import com.initializers.services.apiservices.service.UserWishlistService;

@Service
public class UserWishlistServiceImpl implements UserWishlistService {

	@Autowired
	private UserWishlistRepo userWishListRepo;
	@Autowired
	private ItemDetailsService itemDetailsService;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public UserWishlist addUserWishlist(UserWishlistTemp userWishlist) {
		UserWishlist dbUserWishlist = userWishListRepo.findFirstByUserID(userWishlist.getUserID());
		
		if(dbUserWishlist == null) {
			dbUserWishlist = new UserWishlist();
			if(userDetailsService.getUser(userWishlist.getUserID()) == null) {
				throw new UserNotFoundException();
			}
			dbUserWishlist.setUserID(userWishlist.getUserID());
		}
		if(itemDetailsService.getItemDetails(userWishlist.getItemsId()) == null) {
			throw new ItemNotFoundException();
		}
		dbUserWishlist.setItemsId(userWishlist.getItemsId());
		userWishListRepo.save(dbUserWishlist);
		return dbUserWishlist;
	}

	@Override
	public Map<String, Object> getUserWishlist(Long userID) {
		Map<String, Object> returnVal = new HashMap<>();
		UserWishlist userWishlist = userWishListRepo.findFirstByUserID(userID);
		if(userWishlist == null) {
			throw new UserNotFoundException();
		}
		returnVal.put("userID", userID);
		Set<Long> itemsId = userWishlist.getItemsId();
		List<ItemListTemp> itemDetails = new ArrayList<ItemListTemp>();
		for(Long item : itemsId) {
			ItemListTemp itemListTemp = itemDetailsService.getItemListById(item);
			if(itemListTemp != null) {
				itemDetails.add(itemListTemp);	
			}
		}
		returnVal.put("item", itemDetails);
		return returnVal;
	}

	@Override
	public Object deleteUserWishlist(Long userID, Long itemID) {
		UserWishlist userWishlist = userWishListRepo.findFirstByUserID(userID);
//		Set<Long> items = userWishlist.getItemsId();
//		items.remove(userWishlistTemp.getItemsId());
		if(userWishlist != null) {
			userWishlist.getItemsId().remove(itemID);
			userWishListRepo.save(userWishlist);
			return itemID;
		}
		return null;
	}
}
