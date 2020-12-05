package com.initializers.services.apiservices.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.exception.InternalIssueException;
import com.initializers.services.apiservices.exception.OTPExpiredException;
import com.initializers.services.apiservices.exception.OTPNotValidException;
import com.initializers.services.apiservices.exception.UserNotFoundException;
import com.initializers.services.apiservices.model.TransientUserDetails;
import com.initializers.services.apiservices.model.UserDetails;
import com.initializers.services.apiservices.model.UserOTP;
import com.initializers.services.apiservices.repo.UserOTPRepo;
import com.initializers.services.apiservices.service.MailService;
import com.initializers.services.apiservices.service.TransientUserDetailsService;
import com.initializers.services.apiservices.service.UserDetailsService;
import com.initializers.services.apiservices.service.UserOTPService;

@Service
public class UserOTPServiceImpl implements UserOTPService {

	@Autowired
	private UserOTPRepo userOTPRepo;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private TransientUserDetailsService transientUserDetailsService;
	@Autowired
	private MailService mailservice;
	
	Logger logger = LoggerFactory.getLogger(UserOTPServiceImpl.class);

	@Override
	public void addUserOTP(UserOTP userOTP) {
		// TODO Auto-generated method stub
		userOTPRepo.save(userOTP);
	}

	@Override
	public Map<String,Object> validateUserOTP(UserOTP paramUserOTP) {
		// TODO Auto-generated method stub
		Map<String,Object> returnVal = new HashMap<String, Object>();
		UserOTP dbUserOTP = userOTPRepo.findFirstById(paramUserOTP.getId());
		Date currentTime = new Date();
		if(dbUserOTP == null) {
			throw new UserNotFoundException();
		}else {
			if(currentTime.after(dbUserOTP.getExpiryTime())) {
				throw new OTPExpiredException();
			}else {
				if(dbUserOTP.getOTP().equals(paramUserOTP.getOTP())) {
					int pushCode = pushUserDetails(paramUserOTP.getId());
					if(pushCode == 3) {
						throw new UserNotFoundException();
					}else if(pushCode == 2) {
						logger.error("unable to push data to persistent collection (UserDetails)");
						throw new InternalIssueException();
					}else {
						transientUserDetailsService.deleteUser(paramUserOTP.getId());
						deleteUserOTP(paramUserOTP.getId());
						returnVal.put("Info", "validation success user created");
					}
					
				}else {
					throw new OTPNotValidException();
				}
			}
		}
		return returnVal;
	}

	@Override
	public int pushUserDetails(long userId) {
		UserDetails userDetails = new UserDetails();
		
		 TransientUserDetails transientUserDetails = transientUserDetailsService.getUser(userId);
		 if(transientUserDetails == null) {
			 return 3;
		 }else {
			 userDetails.setId(transientUserDetails.getId());
			 userDetails.setFirstName(transientUserDetails.getFirstName());
			 userDetails.setLastName(transientUserDetails.getLastName());
			 userDetails.setEmail(transientUserDetails.getEmail());
			 userDetails.setPassword(transientUserDetails.getPassword());
			 
			 if(userDetailsService.addUser(userDetails) > 0) {
				 return 1;
			 }else {
				 return 2;
			 } 
		 }
	}

	@Override
	public Long deleteUserOTP(long userId) {
		return userOTPRepo.deleteById(userId);
	}

	@Override
	public Map<String,Object> regenerateUserOTP(long userId) {
		Map<String,Object> returnVal = new HashMap<String, Object>();
		UserOTP userOTP = new UserOTP();
		TransientUserDetails transientUserDetails = transientUserDetailsService.getUser(userId);
		if(transientUserDetails == null) {
			throw new UserNotFoundException();
		} else {
			userOTP.setId(userId);
			addUserOTP(userOTP);
			try {
				mailservice.sendEmail(transientUserDetails.getEmail(),userOTP.getOTP());
			} catch (MessagingException | IOException e) {
				e.printStackTrace();
			}
			returnVal.put("Info", "user OTP resent");
			return returnVal;
		}
	}
}
