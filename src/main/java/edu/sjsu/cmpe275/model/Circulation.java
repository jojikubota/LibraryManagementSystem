package edu.sjsu.cmpe275.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by joji on 12/6/16.
 */

@Entity
@Table(name = "Circulation")
public class Circulation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int userId;
    private int bookId;
    private java.sql.Date checkoutDate;
    private int countOfRenewal;
    private int fine;

    @Override
	public String toString() {
		return "Circulation [id=" + id + "]";
	}

	// Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public int getCountOfRenewal() {
        return countOfRenewal;
    }

    public void setCountOfRenewal(int countOfRenewal) {
        this.countOfRenewal = countOfRenewal;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }
}


