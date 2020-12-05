package com.initializers.services.apiservices.service;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
	public void sendEmail(String emailId, int OTP) throws MessagingException, IOException;
}
