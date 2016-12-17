package edu.sjsu.cmpe275.model;

public class UserProfile {

	private String author;
	private String title;
	private java.sql.Date checkoutDate;
	private String dueDate;
	private int fine;
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public java.sql.Date getCheckoutDate() {
		return checkoutDate;
	}
	public void setCheckoutDate(java.sql.Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public int getFine() {
		return fine;
	}
	public void setFine(int fine) {
		this.fine = fine;
	}
}
