package com.initializers.services.apiservices.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.exception.AddressNotFoundException;
import com.initializers.services.apiservices.exception.CartItemNotFoundException;
import com.initializers.services.apiservices.exception.ItemAvailabilityException;
import com.initializers.services.apiservices.exception.ItemNotFoundException;
import com.initializers.services.apiservices.exception.UserNotFoundException;
import com.initializers.services.apiservices.model.Address;
import com.initializers.services.apiservices.model.OrderStatus;
import com.initializers.services.apiservices.model.UserCart;
import com.initializers.services.apiservices.model.UserOrder;
import com.initializers.services.apiservices.model.UserOrderSet;
import com.initializers.services.apiservices.model.item.ItemListTemp;
import com.initializers.services.apiservices.others.ApplicationProperties;
import com.initializers.services.apiservices.repo.UserCartRepo;
import com.initializers.services.apiservices.repo.UserOrderSetRepo;
import com.initializers.services.apiservices.service.AddressService;
import com.initializers.services.apiservices.service.ItemAvailabilityService;
import com.initializers.services.apiservices.service.ItemDetailsService;
import com.initializers.services.apiservices.service.OfferConfigurationDBService;
import com.initializers.services.apiservices.service.UserCartService;
import com.initializers.services.apiservices.service.UserDetailsService;

@Service
public class UserCartServiceImpl implements UserCartService {

	@Autowired
	private UserCartRepo userCartRepo;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private ItemDetailsService itemDetailsService;
	@Autowired
	private ItemAvailabilityService itemAvailabilityService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private UserOrderSetRepo userOrderSetRepo;
	@Autowired
	private OfferConfigurationDBService offerConfigurationDBService;
	
	@Override
	public UserCart addUserCart(UserCart userCart) {
		if (userCart.getQuantity() <= 0) {
			userCartRepo.deleteById(userCart.getId());
			return userCart;
		}
		if (userDetailsService.getUser(userCart.getId().getUserId()) == null) {
			throw new UserNotFoundException();
		}
		if (itemDetailsService.getItemDetails(userCart.getId().getItemId()) == null) {
			throw new ItemNotFoundException();
		}
		if (itemAvailabilityService.getAvailabilityById(userCart.getId().getAvailabilityId(),
				userCart.getId().getItemId()) == null) {
			throw new ItemAvailabilityException();
		}
		return userCartRepo.save(userCart);
	}

	@Override
	public List<Object> getUserCartByuserId(Long userId, Pageable pageable) {
		Iterator<UserCart> userCartItr = userCartRepo.findByIdUserId(userId, pageable).stream()
				.filter(userCart -> itemAvailabilityService.getAvailabilityById(userCart.getId().getAvailabilityId(),
						userCart.getId().getItemId()) != null)
				.iterator(); 
		List<Object> itemListTemp = new ArrayList<>();
		while (userCartItr.hasNext()) {
			Map<Object, Object> returnVal = new HashMap<>();
			UserCart userCart = userCartItr.next();
			ItemListTemp itemDetails = itemDetailsService.getItemListById(userCart.getId().getItemId(),
					userCart.getId().getAvailabilityId());

			if (itemDetails != null)
				returnVal.put("Item", itemDetails);
			returnVal.put("quantity", userCart.getQuantity());
			itemListTemp.add(returnVal);
		}

		return itemListTemp;
	}

	@Override
	public Object createOrder(Long userId, Long addressId, String coupenCode) {
		Float totalAmount = 0F;
		List<UserOrder> userOrderList = new ArrayList<>();
		UserOrderSet userOrderSet = new UserOrderSet();
		List<UserCart.CompositeKey> cartIdforDeletion = new ArrayList<>();
		Address address = addressService.getAddressById(addressId);
		if(address == null || address.getUserId() != userId) {
			throw new AddressNotFoundException();
		}
		
		Iterator<UserCart> userCartItr = userCartRepo.findByIdUserId(userId).stream()
				.filter(userCart -> itemAvailabilityService.getAvailabilityById(userCart.getId().getAvailabilityId(),
						userCart.getId().getItemId()) != null).
				iterator();
		
		
		while(userCartItr.hasNext()) {
			Float amount = 0F;
			UserOrder userOrder = new UserOrder();
			UserCart userCart = userCartItr.next();
			Float discountPrice = itemAvailabilityService.getPriceByAvailabilityId(userCart.getId().
					getAvailabilityId());
			if(discountPrice == null) continue;
			userOrder.setUserId(userCart.getId().getUserId());
			userOrder.setItemId(userCart.getId().getItemId());
			userOrder.setAvailabilityId(userCart.getId().getAvailabilityId());
			userOrder.setQuantity(userCart.getQuantity());
			amount += (userCart.getQuantity() * discountPrice);
			userOrder.setAmount(userCart.getQuantity() * discountPrice);
			totalAmount += amount;
			userOrderList.add(userOrder);
			cartIdforDeletion.add(userCart.getId());
		}
		if(userOrderList.size() > 0) {
			userOrderSet.setTotalAmount(totalAmount);
			userOrderSet.setOrderList(userOrderList);
			userOrderSet.setAddressId(addressId);
			userOrderSet.setOrderAt(new Date());
			userOrderSet.setStatus(new OrderStatus());
			userOrderSet.setCoupenCode(coupenCode);
			for(String type : ApplicationProperties.orderConfigType) {
				Object value = offerConfigurationDBService.configureOrderBeforeSend(userOrderSet,type);
				if(type.equals("DLRCRG") && value != null) {
					userOrderSet.setDeliveryCharge(Float.parseFloat(value.toString()));
				}
				if(type.equals("COUPEN") && value != null) {
					userOrderSet.setCoupenDiscount(Float.parseFloat(value.toString()));
				}
			}
			//create order
			userOrderSetRepo.save(userOrderSet);
			//delete from cart
			for(UserCart.CompositeKey key : cartIdforDeletion) {
				userCartRepo.deleteById(key);
			}
		}else {
			throw new CartItemNotFoundException();
		}
		return "order created successfully";
	}
}
