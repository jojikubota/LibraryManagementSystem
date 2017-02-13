package edu.sjsu.cmpe275.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import edu.sjsu.cmpe275.model.Book;
import edu.sjsu.cmpe275.model.Circulation;
import edu.sjsu.cmpe275.model.Keywords;
import edu.sjsu.cmpe275.model.TimeModel;
import edu.sjsu.cmpe275.model.User;

public interface DAO {

	public boolean createUser(User user);	
	public boolean createBook(Book book);
	public User getUser(String username);
	public boolean updateUserStatus(User user);
	public List<Book> search(String searchString);
	public List<Book> searchPatron(String searchString);
	public Keywords getKeyword(String keyword);
	public boolean updateKeyword(Keywords keyword);
	public boolean updateBook(Book book);
	public boolean deleteBooks(List<Integer> id);
	public Book getBook(int bookId);
	public boolean createCirculation(Circulation circulation);
	public Circulation getCirculation(int userId, int bookId);
	public List<Circulation> getCirculationForUser(int userId);
	public boolean deleteCirculation(Circulation circulation);
	public boolean resetCheckoutDate(Circulation circulation, java.sql.Timestamp renewedDate);
	public void setDateTime(TimeModel date);
	public TimeModel getDateTime();
	public void updateDateTime(Timestamp date);
	public List<Circulation> getAllCirculations();
	public User getUser(int userId);
	public List<User> getAllUsers();
	public boolean updateBooksOnHold(User user);
	public boolean updateCirculation(Circulation circulation);
	public boolean updateCirculationEmailTag();

}
