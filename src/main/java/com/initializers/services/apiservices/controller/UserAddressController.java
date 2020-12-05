package com.initializers.services.apiservices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.initializers.services.apiservices.model.Address;
import com.initializers.services.apiservices.service.AddressService;

@RestController()
@RequestMapping("/user/address")
@CrossOrigin
public class UserAddressController {

	@Autowired
	private AddressService addressService;
	
	@GetMapping("/specific/{addressId}")
	public Address getAddressById(@PathVariable Long addressId) {
		return addressService.getAddressById(addressId);
	}
	@GetMapping("/{userId}")
	public List<Address> getAddressByUserId(@PathVariable Long userId) {
		return addressService.getAddressByUserId(userId);
	}
	@PostMapping("/")
	public Address addAddressByUserId(@RequestBody Address address) {
		return addressService.addAddress(address);
	}
	@PutMapping("/")
	public Address updateAddressByUserId(@RequestBody Address address) {
		return addressService.updateAddress(address);
	}
	
}
