package edu.sjsu.cmpe275.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.search.annotations.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "User", uniqueConstraints= @UniqueConstraint(columnNames={"emailAddress", "universityId"}))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	@Field
	private String emailAddress;


	private String userName;


	private String lastName;


	private String password;


	private String universityId;
	

	private boolean status;
	private String role;
	private String token;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "BOOKS_ONHOLD")
	@MapKeyColumn(name = "BOOK_ID")
	@Column(name = "DAYS_LEFT")
	
	private Map<Integer, java.sql.Date> booksOnHold;
	

	
	@ManyToMany(mappedBy="waitlist", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch=FetchType.EAGER)
	@JsonIgnoreProperties(value = { "waitlist"})
	private List<Book> books  = new ArrayList<Book>();
	
	
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUniversityId() {
		return universityId;
	}

	public void setUniversityId(String universityId) {
		this.universityId = universityId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return universityId;
	}

	public void setUserId(String userId) {
		this.universityId = userId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		return true;
	}
//
//	public List<Book> getBooks() {	
//		return books;
//	}
//
//	public void setBooks(List<Book> books) {
//		this.books = books;
//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User";
	}

	public User() {
		super();
		this.status = false;
	}
	
	public Map<Integer, java.sql.Date> getBooksOnHold() {
		return booksOnHold;
	}

	public void setBooksOnHold(Map<Integer, Date> booksOnHold) {
		this.booksOnHold = booksOnHold;
	}

}
