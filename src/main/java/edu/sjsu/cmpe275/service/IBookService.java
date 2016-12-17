package edu.sjsu.cmpe275.service;

import java.util.List;

import edu.sjsu.cmpe275.model.Book;
import edu.sjsu.cmpe275.model.Keywords;

public interface IBookService {
	public List<Book> searchForBooks(String keyword);
	public List<Book> searchPatronBooks(String keyword);
	public boolean createBook(Book book) throws Exception;
	public boolean updateKeyword(Keywords keyword) throws Exception;
	public Keywords getKeyword(String keyword) throws Exception;
	public boolean updateBook(Book book)  throws Exception;
	public boolean deleteBook(List<Integer> id)  throws Exception;
	public Book findOneBook(int bookId);
}
