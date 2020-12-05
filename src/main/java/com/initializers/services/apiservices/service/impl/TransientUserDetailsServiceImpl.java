package com.initializers.services.apiservices.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.exception.EmailExistException;
import com.initializers.services.apiservices.exception.EmailIDException;
import com.initializers.services.apiservices.exception.RequiredValueMissingException;
import com.initializers.services.apiservices.model.TransientUserDetails;
import com.initializers.services.apiservices.model.UserDetails;
import com.initializers.services.apiservices.model.UserOTP;
import com.initializers.services.apiservices.repo.TransientUserDetailsRepo;
import com.initializers.services.apiservices.repo.UserDetailsRepo;
import com.initializers.services.apiservices.service.MailService;
import com.initializers.services.apiservices.service.TransientUserDetailsService;
import com.initializers.services.apiservices.service.UserOTPService;

@Service
public class TransientUserDetailsServiceImpl implements TransientUserDetailsService {

	@Autowired
	private TransientUserDetailsRepo transientUserDetailsRepo;
	@Autowired
	private UserOTPService userOTPService;
	@Autowired
	private UserDetailsRepo userDetailsRepo;
	@Autowired
	private MailService mailservice;
	Logger logger = LoggerFactory.getLogger(TransientUserDetailsServiceImpl.class);
	
	@Override
	public Map<String,Object> addUser(TransientUserDetails transientUserDetails) {
		try { 
			Map<String,Object> returnValue = new HashMap<>();
			boolean noSuchEmail = checkEmail(transientUserDetails.getEmail());
			if(noSuchEmail) {
				TransientUserDetails userSavedData = transientUserDetailsRepo.save(transientUserDetails);
				UserOTP userOTP = new UserOTP();
				userOTP.setId(transientUserDetails.getId());
				userOTPService.addUserOTP(userOTP);
				try {
					mailservice.sendEmail(transientUserDetails.getEmail(),userOTP.getOTP());
				} catch (MessagingException | IOException e) {
					logger.error(e+ "unable to send email to " + transientUserDetails.getEmail());
					throw new EmailIDException();
				}
				returnValue.put("userId", userSavedData.getId());
				return returnValue;
			}else {
				throw new EmailExistException();
			}
			
		}catch(IllegalArgumentException e) {
			logger.error(e+ "transient user details data is incorrect" + transientUserDetails.getEmail());
			throw new RequiredValueMissingException();
		}
	}

	@Override
	public TransientUserDetails getUser(long userId) {
		// TODO Auto-generated method stub
		return transientUserDetailsRepo.findFirstById(userId);
	}

	@Override
	public Long deleteUser(long userId) {
		return transientUserDetailsRepo.deleteById(userId);
	}

	//check if email exist in transient and presistent user details
	public boolean checkEmail(String email) {
		List<UserDetails> userDetails = userDetailsRepo.findByEmail(email);
		
		//if email exist in transient user db return false
		if(transientUserDetailsRepo.findFirstIdByEmail(email) != null) {
			return false;
		//if email exist in persistance user db
		}else if(userDetails != null) {
			//iterate through every data and check if any active account
			for (UserDetails userDetail : userDetails) {
				if(userDetail.getStatus().equals("Active")) {
					return false;
				}
			}
			
		}
		return true;
		
	}
}
