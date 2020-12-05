package com.initializers.services.apiservices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.item.ItemAvailability;

@Service
public interface ItemAvailabilityService {
	
	List<ItemAvailability> getAvailabilityByItemId(Long itemId, char... availability);
	
	ItemAvailability addAvailability(ItemAvailability itemAvailability);
	
	ItemAvailability getMaxAvailabilityByItemId(Long itemId);
	
	ItemAvailability getAvailabilityById(Long id, Long itemId);
	
	ItemAvailability getAvailabilityByIdAdmin(Long id);
	
	ItemAvailability updateAvailability(ItemAvailability itemAvailability);
	
	Float getPriceByAvailabilityId(Long id);
	
	StringBuilder getValueUnitByAvailabilityId(Long id);
}
