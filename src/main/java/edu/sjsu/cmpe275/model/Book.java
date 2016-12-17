package edu.sjsu.cmpe275.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Book")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@keywords")
@Indexed
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Field
	private String ISBN;
	@Field
	private String author;

	@Field
	private String title;

	

	private String callNumber;

	private String publisher;

	private String yearOfPublication;

	private int noOfCopies;

	private String location;

	private String status;

//	@ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
//	@JsonIgnoreProperties(value = {"books"})
//	private List<User> waitingUserList = new LinkedList<User>();

//	public List<User> getUserList() {
//		return waitingUserList;
//	}
//
//	public void setUserList(List<User> userList) {
//		this.waitingUserList = userList;
//	}

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinTable(name = "Book_Keyword", joinColumns = {
			@JoinColumn(name = "bookId", referencedColumnName = "id") }, inverseJoinColumns = {
			@JoinColumn(name = "keyName", referencedColumnName = "id") })
	@JsonIgnoreProperties(value = { "books"})
	@IndexedEmbedded	
	private Set<Keywords> keywords = new HashSet<Keywords>();
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Book other = (Book) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@ManyToOne
	@IndexedEmbedded
	private User createdBy;
	
//	@OneToOne
//	private User updatdBy;
	
	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public int getBookId() {
		return id;
	}

	public void setBookId(int bookId) {
		this.id = bookId;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

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

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getYearOfPublication() {
		return yearOfPublication;
	}

	public void setYearOfPublication(String yearOfPublication) {
		this.yearOfPublication = yearOfPublication;
	}

	public int getNoOfCopies() {
		return noOfCopies;
	}

	public void setNoOfCopies(int noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Keywords> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<Keywords> keywords) {
		this.keywords = keywords;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Book() {
		super();
		this.status = "available";
	}
	
	@Override
	public String toString() {
		return "Book [title=" + title + "]";
	}
}