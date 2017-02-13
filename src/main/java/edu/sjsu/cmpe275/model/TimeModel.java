package edu.sjsu.cmpe275.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AppTime")
public class TimeModel {
	@Id
	private int id=1;
	
	@Column(name="date")
	private Timestamp date;
	
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date2) {
		this.date = date2;
	}
}
