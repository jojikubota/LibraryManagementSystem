package edu.sjsu.cmpe275.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.dao.DAO;
import edu.sjsu.cmpe275.dao.DAOImpl;
import edu.sjsu.cmpe275.model.Book;
import edu.sjsu.cmpe275.model.Circulation;

/**
 * Created by joji on 12/6/16.
 */

@Service
public class CirculationService implements ICirculationService {

    DAO dao = new DAOImpl();

    @Override
    public boolean createCirculation(Circulation circulation) throws Exception {

        boolean result = dao.createCirculation(circulation);

        if (!result) {
            System.out.println("COULD NOT CREATE CIRCULATION");
        }

        return result;
    }

    @Override
    public Circulation getCirculation(int userId, int bookid) {

        return dao.getCirculation(userId, bookid);
    }

    public List<Circulation> getCirculationForUser(int userId) {
    	return dao.getCirculationForUser(userId);
    }
    
    @Override
    public boolean deleteCirculation(Circulation circulation) {
        return dao.deleteCirculation(circulation);
    }

    @Override
    public boolean resetCheckoutDate(Circulation circulation) {
        java.sql.Date renewedDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        return dao.resetCheckoutDate(circulation, renewedDate);
    }
}
