package com.initializers.services.apiservices.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.exception.RequiredValueMissingException;
import com.initializers.services.apiservices.exception.UserNotFoundException;
import com.initializers.services.apiservices.model.UserDetails;
import com.initializers.services.apiservices.repo.UserDetailsRepo;
import com.initializers.services.apiservices.service.TransientUserDetailsService;
import com.initializers.services.apiservices.service.UserDetailsService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private UserDetailsRepo userDetailsRepo;
	@Autowired
	private TransientUserDetailsService transientUserService;
	
	public List<UserDetails> getAllUsers() {
		List<UserDetails> userDetails = new ArrayList<>();
		userDetailsRepo.findAll().forEach(userDetails::add);
		System.out.println(userDetailsRepo.findFirstIdByEmail("sharath@mail.om"));
		return userDetails;
	}

	@Override
	public Long addUser(UserDetails userDetails) {
		userDetails.setStatus("Active");
		UserDetails userSavedData = userDetailsRepo.save(userDetails);
		return userSavedData.getId();
	}

	@Override
	public UserDetails getUser(Long userId) {
		UserDetails userDetails = userDetailsRepo.findFirstById(userId);
		if(userDetails == null) throw new UserNotFoundException();
		return userDetails;
	}

	@Override
	public Object deleteUser(Long userId) {
		Map<String,Object> returnVal = new HashMap<>();
		returnVal.put("Info", "User deleted");
		if(getUser(userId) != null) {
			UserDetails userDetails = getUser(userId);
			userDetails.setStatus("Inactive");
			userDetailsRepo.save(userDetails);
		}else if(transientUserService.getUser(userId) != null) {
			transientUserService.deleteUser(userId);
		}else {
			throw new UserNotFoundException();
		}
		returnVal.put("Info", "user deleted successfuly");
		return returnVal;
	}

	@Override
	public Map<String,Object> updateUserDetail(UserDetails userDetails) {
		Map<String,Object> returnVal = new HashMap<String, Object>();
		if(userDetails == null) {
			throw new RequiredValueMissingException();
		}else {
			UserDetails dbUserDetails = getUser(userDetails.getId());
			if(dbUserDetails == null) {
				throw new UserNotFoundException();
			}else {
				final String email = userDetails.getEmail();
				final String firstName = userDetails.getFirstName();
				final String lastName = userDetails.getLastName();
				final String password = userDetails.getPassword();
				
//				currently not supported (need to validate with otp before update)
//				if(email != dbUserDetails.getEmail() && email != null) {
//					dbUserDetails.setEmail(email);
//				}
				if(firstName != dbUserDetails.getFirstName() && firstName != null) {
					dbUserDetails.setFirstName(firstName);
				}
				if(lastName != dbUserDetails.getLastName() && lastName != null) {
					dbUserDetails.setLastName(lastName);
				}
				if(password != dbUserDetails.getPassword() && password != null) {
					dbUserDetails.setPassword(password);
				}
				
				userDetailsRepo.save(dbUserDetails);
				returnVal.put("Info", "User data updated successfully");
			}
		}
		return returnVal;
	}

	@Override
	public String getNameById(Long userId) {
		UserDetails userDetails = userDetailsRepo.findNameById(userId);
		if(userDetails != null) {
			return userDetails.getFirstName() + " " + userDetails.getLastName();
		}
		return null;
	}

	
}
