package edu.sjsu.cmpe275.service;

import java.sql.Date;

import edu.sjsu.cmpe275.model.TimeModel;

public interface IDateService {

	public void setTime(TimeModel date);
	public TimeModel getTime();
	public void updateTime(Date date);
}
