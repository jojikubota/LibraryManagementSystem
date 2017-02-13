package edu.sjsu.cmpe275.service;

import java.util.List;

import edu.sjsu.cmpe275.model.User;

public interface IUserService {

	public boolean createUser(User user) throws Exception;
	public User getUser(String emailAddress) throws Exception;
	public User getUser(int userId) throws Exception;
	public void sendVerificationEmail(String emailAddress, String token);
	public boolean confirmUserRegistration(String userName, String token);
	public void sendConfirmationEmail(String emailAddress, String messageString);
	public List<User> getAllUsers();
	public boolean updateBooksOnHold(User user);
}
