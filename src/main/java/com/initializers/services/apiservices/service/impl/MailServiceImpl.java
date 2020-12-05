package com.initializers.services.apiservices.service.impl;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.service.MailService;

@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	@Async
	public void sendEmail(String emailId, int OTP) throws MessagingException, IOException {
		MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		
        helper.setTo(emailId);

        helper.setSubject("Thavare Daily");

        // default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText("<h1>Welcome To Thavare Daily</h1>"
        		+ "<h3>Thank you for registering with us</h3>"
        		+ "<h3>please use the below OTP and start shopping with us</h3>"
        		+ OTP, true);
//        helper.setText("<h3>OTP : "+OTP+"</h3>");
        

		// hard coded a file path
        //FileSystemResource file = new FileSystemResource(new File("path/android.png"));

        javaMailSender.send(msg);
	}

}
