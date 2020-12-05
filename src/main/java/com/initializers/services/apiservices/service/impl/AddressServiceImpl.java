package com.initializers.services.apiservices.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.exception.AddressNotFoundException;
import com.initializers.services.apiservices.exception.RequiredValueMissingException;
import com.initializers.services.apiservices.exception.UserNotFoundException;
import com.initializers.services.apiservices.model.Address;
import com.initializers.services.apiservices.repo.AddressRepo;
import com.initializers.services.apiservices.service.AddressService;
import com.initializers.services.apiservices.service.UserDetailsService;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AddressRepo addressRepo;
	
	@Override
	public Address addAddress(Address address) {
		if(address == null) {
			throw new RequiredValueMissingException();
		}else if(address.getUserId() == null) {
			throw new UserNotFoundException();
		}else if(userDetailsService.getUser(address.getUserId()) == null) {
			throw new UserNotFoundException();
		}else {
			return addressRepo.save(address);
		}
	}

	@Override
	public Address getAddressById(Long addressId) {
		if(addressId == null) {
			throw new RequiredValueMissingException();
		}else {
			return addressRepo.findFirstById(addressId);
		}
	}

	@Override
	public List<Address> getAddressByUserId(Long userId) {
		if(userId == null) {
			throw new RequiredValueMissingException();
		}else {
			return addressRepo.findByUserId(userId);
		}
	}

	@Override
	public Address updateAddress(Address address) {
		if(address.getId() != null) {
			Address dbAddress = getAddressById(address.getId());
			if(dbAddress == null) {
				throw new AddressNotFoundException();
			}else {
				if(address.getFirstLine() != null) {
					dbAddress.setFirstLine(address.getFirstLine());
				}
				if(address.getSecondLine() != null) {
					dbAddress.setSecondLine(address.getSecondLine());
				}
				if(address.getName() != null) {
					dbAddress.setName(address.getName());
				}
				if(address.getPhoneNumber() != null) {
					dbAddress.setPhoneNumber(address.getPhoneNumber());
				}
				if(address.getLandMark() != null) {
					dbAddress.setLandMark(address.getLandMark());
				}
				if(address.getPinCode() != null) {
					dbAddress.setPinCode(address.getPinCode());
				}
				return addressRepo.save(dbAddress);
			}
		}else {
			throw new RequiredValueMissingException();
		}
	}

	@Override
	//dummy method
	public Address getAddressByIdByUserId(Long addressId, Long userID) {
		if(addressId == null || userID == null) {
			throw new RequiredValueMissingException();
		}else {
			return null;
//			return addressRepo.findByIdByUserID(addressId, userID);
		}
	}

}
