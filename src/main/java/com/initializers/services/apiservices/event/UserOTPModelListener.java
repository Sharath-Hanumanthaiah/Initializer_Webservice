package com.initializers.services.apiservices.event;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.initializers.services.apiservices.model.UserOTP;

@Component
public class UserOTPModelListener extends AbstractMongoEventListener<UserOTP>{

	@Override
    public void onBeforeConvert(BeforeConvertEvent<UserOTP> event) {
		//to generate random OTP
		Random rand = new Random();
		//to set expiry time
		Date date = new Date();
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
	    calender.add(Calendar.MINUTE, 20);
		event.getSource().setOTP(rand.nextInt((9998 - 1000) + 1) + 1000);
		event.getSource().setExpiryTime(calender.getTime());
	}
}
