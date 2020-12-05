package com.initializers.services.apiservices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.Address;

@Service
public interface AddressService {
	
	Address addAddress(Address address);
	
	Address getAddressById(Long addressId);
	
	Address getAddressByIdByUserId(Long addressId, Long userID);
	
	List<Address> getAddressByUserId(Long userId);
	
	Address updateAddress(Address address);
}
