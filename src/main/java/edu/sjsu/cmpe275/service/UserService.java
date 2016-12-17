package edu.sjsu.cmpe275.service;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.sjsu.cmpe275.dao.DAO;
import edu.sjsu.cmpe275.dao.DAOImpl;
import edu.sjsu.cmpe275.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

	DAO dao = new DAOImpl();

	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

	/**
	 * creates new user with the specified details
	 */
	@Override
	public boolean createUser(User user) throws Exception {

		boolean result = dao.createUser(user);

		if (!result) {
			System.out.println("COULD NOT CREATE USER");

		}

		return result;
	}
	
	@Override
	public User getUser(String emailAddress) throws Exception {

		User result = dao.getUser(emailAddress);

		if (result == null) {
			System.out.println("COULD NOT FIND USER");

		}

		return result;
	}
	
	
	public boolean confirmUserRegistration(String userName, String token) {
		User user = dao.getUser(userName);
		if(user.getToken().equals(token))
		{
			return dao.updateUserStatus(user);
		}
		else
		{
			return false;
		}
		
	}
	
	public void sendConfirmationEmail(String emailAddress, String messageString) {
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("cmpe275final@gmail.com");
		mailSender.setPassword("finalproject");

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		mailSender.setJavaMailProperties(props);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper email = new MimeMessageHelper(message);
		try {
			email.setTo(emailAddress);
			email.setText(messageString);
			mailSender.send(message);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void sendVerificationEmail(String emailAddress, String token) {
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("cmpe275final@gmail.com");
		mailSender.setPassword("finalproject");

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		mailSender.setJavaMailProperties(props);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper email = new MimeMessageHelper(message);
		try {
			email.setTo(emailAddress);
			email.setText("Please use this token for registration confirmation "+ token);
			mailSender.send(message);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
