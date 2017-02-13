package edu.sjsu.cmpe275.service;

public interface IReminderService {
	
	public void checkDueDate() throws Exception;
	public void checkBooksOnHold() throws Exception;
	public void checkFine() throws Exception;
}
