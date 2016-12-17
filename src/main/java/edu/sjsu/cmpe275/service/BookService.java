package edu.sjsu.cmpe275.service;

import java.util.List;

import edu.sjsu.cmpe275.dao.DAO;
import edu.sjsu.cmpe275.dao.DAOImpl;
import edu.sjsu.cmpe275.model.Book;
import edu.sjsu.cmpe275.model.Keywords;
import edu.sjsu.cmpe275.model.User;
import org.springframework.stereotype.Service;

@Service
public class BookService implements IBookService{
	
	DAO dao = new DAOImpl();
	
	@Override
	public boolean createBook(Book book) throws Exception {

		boolean result = dao.createBook(book);

		if (!result) {
			System.out.println("COULD NOT CREATE USER");
		}
		return result;
	}
	
	@Override
	public boolean updateKeyword(Keywords keyword) throws Exception {

		boolean result = dao.updateKeyword(keyword); 
		
		if(!result){
			System.out.println("Keyword DOES NOT EXIST");
		}
				
		return true;
	}
	
	@Override
	public Keywords getKeyword(String keyword) throws Exception {

		Keywords result = dao.getKeyword(keyword);

		if (result == null) {
			System.out.println("COULD NOT FIND USER");

		}

		return result;
	}
	
	public List<Book> searchForBooks(String keyword){
		return dao.search(keyword);
	}
	
	@Override
	public Book findOneBook(int bookId) {

		return dao.getBook(bookId);
		}
	
	@Override
	public boolean updateBook(Book book)  throws Exception {
	
		boolean result = dao.updateBook(book); 
		
		if(!result){
			System.out.println("Keyword DOES NOT EXIST");
		}
				
		return true;
	}
	
	@Override
	public boolean deleteBook(List<Integer> id)  throws Exception {
	
		boolean result = dao.deleteBooks(id); 
		
		if(!result){
			System.out.println("Books cannot be deleted");
		}
				
		return true;
	}
	
	public List<Book> searchPatronBooks(String keyword) {
		return dao.searchPatron(keyword);
	}

}
