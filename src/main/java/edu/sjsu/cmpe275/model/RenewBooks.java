package edu.sjsu.cmpe275.model;

import java.util.ArrayList;
import java.util.List;

public class RenewBooks {

	private String messageString;
	List<Book> renewableBooks = new ArrayList<Book>();

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}

	public List<Book> getRenewableBooks() {
		return renewableBooks;
	}

	public void setRenewableBooks(List<Book> renewableBooks) {
		this.renewableBooks = renewableBooks;
	}

}
