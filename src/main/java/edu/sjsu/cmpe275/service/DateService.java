package edu.sjsu.cmpe275.service;

import java.sql.Date;

import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.dao.DAO;
import edu.sjsu.cmpe275.dao.DAOImpl;
import edu.sjsu.cmpe275.model.TimeModel;

@Service
public class DateService implements IDateService {
	DAO dao = new DAOImpl();

	@Override
	public void setTime(TimeModel date) {
		dao.setDateTime(date);
	}

	@Override
	public TimeModel getTime() {
		return dao.getDateTime();
	}

	@Override
	public void updateTime(Date date) {
		//dao.updateDateTime(date);
	}

}
