package com.initializers.services.apiservices.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.exception.ItemAvailabilityExistException;
import com.initializers.services.apiservices.exception.ItemNotFoundException;
import com.initializers.services.apiservices.exception.RequiredValueMissingException;
import com.initializers.services.apiservices.model.item.ItemAvailability;
import com.initializers.services.apiservices.repo.ItemAvailabilityRepo;
import com.initializers.services.apiservices.service.ItemAvailabilityService;
import com.initializers.services.apiservices.service.ItemDetailsService;

@Service
public class ItemAvailabilityServiceImpl implements ItemAvailabilityService {

	@Autowired
	private ItemAvailabilityRepo itemAvailabilityRepo;
	@Autowired
	private ItemDetailsService itemDetailsService;

	@Override
	public List<ItemAvailability> getAvailabilityByItemId(Long itemId, char... availability) {
		if (availability.length > 0) {
			return itemAvailabilityRepo.findByItemId(itemId);
		}
		return itemAvailabilityRepo.findByItem(itemId);
	}

	@Override
	public ItemAvailability addAvailability(ItemAvailability itemAvailability) {

		if ((itemAvailability.getDiscount() == null && itemAvailability.getDiscountPrice() == null)
				|| itemAvailability.getActualPrice() == null || itemAvailability.getValue() == null
				|| itemAvailability.getUnit() == null) {
			throw new RequiredValueMissingException();
		}
		if (itemDetailsService.getItemDetails(itemAvailability.getItemId()) == null) {
			throw new ItemNotFoundException();
		}
		List<ItemAvailability> dbitemAvailability = getAvailabilityByItemId(itemAvailability.getItemId(), 'N');
		// check if item availability exist
		for (ItemAvailability item : dbitemAvailability) {
			if (itemAvailability.getValue().equals(item.getValue())
					&& itemAvailability.getUnit().toLowerCase().equals(item.getUnit().toLowerCase())) {
//				itemAvailability.setId(item.getId());
				throw new ItemAvailabilityExistException();
			}
		}
		if (itemAvailability.getAvailable().equals("")) {
			itemAvailability.setAvailable("Active");
		}
		itemAvailability.setState(itemAvailability.getAvailable().equals("Active") ? "Success" : "Error");

		return itemAvailabilityRepo.save(itemAvailability);
	}

	@Override
	public ItemAvailability getMaxAvailabilityByItemId(Long itemId) {
		return itemAvailabilityRepo.findTopByItemIdOrderByDiscountDesc(itemId);
	}

	@Override
	public ItemAvailability getAvailabilityById(Long id, Long itemId) {
		ItemAvailability itemAvailability = itemAvailabilityRepo.findFisrtById(id);
		if (itemAvailability != null && !itemAvailability.getItemId().equals(itemId)) {
			return null;
		} else {
			return itemAvailability;
		}
	}

	@Override
	public Float getPriceByAvailabilityId(Long id) {
		ItemAvailability itemAvailability = itemAvailabilityRepo.findFirstDiscountPriceById(id);
		if (itemAvailability != null) {
			return itemAvailability.getDiscountPrice();
		}
		return null;
	}

	@Override
	public ItemAvailability getAvailabilityByIdAdmin(Long id) {
		return itemAvailabilityRepo.findFirstId(id);
	}

	@Override
	public ItemAvailability updateAvailability(ItemAvailability itemAvailability) {
		if(itemAvailability.getId() == null || itemAvailability.getItemId() == null) {
			throw new RequiredValueMissingException();
		}
		ItemAvailability dbItemAvailability = itemAvailabilityRepo.findFirstId(itemAvailability.getId());
		
		
		if(!itemAvailability.getAvailable().equals("")) {
			dbItemAvailability.setAvailable(itemAvailability.getAvailable());
			dbItemAvailability.setState(itemAvailability.getAvailable().equals("Active")?"Success":"Error");
		}
		if(itemAvailability.getDiscountPrice() != null && itemAvailability.getDiscount() != null) {
			dbItemAvailability.setDiscountPrice(itemAvailability.getDiscountPrice());
			dbItemAvailability.setDiscount(itemAvailability.getDiscount());
		}else if(itemAvailability.getDiscountPrice() != null) {
			dbItemAvailability.setDiscountPrice(itemAvailability.getDiscountPrice());
			dbItemAvailability.setDiscount(null);
		}else if(itemAvailability.getDiscount() != null) {
			dbItemAvailability.setDiscount(itemAvailability.getDiscount());
			dbItemAvailability.setDiscountPrice(null);
		}
		
		if(itemAvailability.getActualPrice() != null) {
			dbItemAvailability.setActualPrice(itemAvailability.getActualPrice());
		}
		if(itemAvailability.getValue() != null || !itemAvailability.getUnit().equals("")) {
			dbItemAvailability.setValue(itemAvailability.getValue() != null? 
					itemAvailability.getValue():
						dbItemAvailability.getValue());
			dbItemAvailability.setUnit(!itemAvailability.getUnit().equals("")? 
					itemAvailability.getUnit():
						dbItemAvailability.getUnit());
			// check if item availability exist
			for (ItemAvailability item : getAvailabilityByItemId(dbItemAvailability.getItemId(), 'N')) {
				if (dbItemAvailability.getValue().equals(item.getValue())
						&& dbItemAvailability.getUnit().toLowerCase().equals(item.getUnit().toLowerCase())) {
//					itemAvailability.setId(item.getId());
					throw new ItemAvailabilityExistException();
				}
			}
		}
		
		return itemAvailabilityRepo.save(dbItemAvailability);
	}

	@Override
	public StringBuilder getValueUnitByAvailabilityId(Long id) {
		ItemAvailability itemAvailability = itemAvailabilityRepo.findFirstValueUnitId(id);
		StringBuilder valueUnit = new StringBuilder();
		if (itemAvailability != null) {
			valueUnit.append(itemAvailability.getValue());
			valueUnit.append(" ");
			valueUnit.append(itemAvailability.getUnit());
		}
		return valueUnit;
	}

}
