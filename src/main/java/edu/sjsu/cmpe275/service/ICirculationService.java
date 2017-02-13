package edu.sjsu.cmpe275.service;

import java.util.List;

import edu.sjsu.cmpe275.model.Circulation;

/**
 * Created by joji on 12/6/16.
 */
public interface ICirculationService {

    public boolean createCirculation(Circulation circulation) throws Exception;

    public Circulation getCirculation(int userId, int bookid);

    public boolean deleteCirculation(Circulation circulation);

    public boolean resetCheckoutDate(Circulation circulation,java.sql.Timestamp date);
    
    public List<Circulation> getCirculationForUser(int userId);
    
    public List<Circulation> getAllCirculations();
    
    public boolean updateCirculation(Circulation circulation);
    
    public boolean setEmailTag();
}
