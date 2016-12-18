package edu.sjsu.cmpe275.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import edu.sjsu.cmpe275.model.Book;
import edu.sjsu.cmpe275.model.Circulation;
import edu.sjsu.cmpe275.model.Keywords;
import edu.sjsu.cmpe275.model.User;

public class DAOImpl implements DAO {

	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("jpa-example");

	 /** Hibernate Full Text Entity Manager. */
    private FullTextEntityManager ftem;
    
	/**
	 * creates a user in the database
	 */

	public boolean createUser(User user) {

		try {
			EntityManager entitymanager = emfactory.createEntityManager();
			entitymanager.getTransaction().begin();
			entitymanager.persist(user);
			entitymanager.getTransaction().commit();
			entitymanager.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public User getUser(String username) {

		EntityManager entitymanager = emfactory.createEntityManager();
		Query query = entitymanager.createQuery("SELECT u from User u where u.emailAddress='" + username +"'");
		User user = (User) query.getSingleResult();
		entitymanager.close();
		return user;

	}
	
	public boolean createBook(Book book)
	{
		try {
			EntityManager entitymanager = emfactory.createEntityManager();
			entitymanager.getTransaction().begin();
			entitymanager.persist(book);
			entitymanager.getTransaction().commit();
			entitymanager.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean updateBook(Book book)
	{
		EntityManager entitymanager = emfactory.createEntityManager();		
		entitymanager.getTransaction().begin();
		
		Book temp = entitymanager.find(Book.class, book.getBookId());	
		
		if(temp == null){
			return false;	
		}
			
		if(book.getAuthor()!=null)
			temp.setAuthor(book.getAuthor());
		if(book.getCallNumber()!=null)
			temp.setCallNumber(book.getCallNumber());
		
		if(book.getISBN()!=null)
			temp.setISBN(book.getISBN());
		
		if(book.getKeywords().size() > 0)
			temp.setKeywords(book.getKeywords());
	    
		if(book.getLocation()!=null)
			temp.setLocation(book.getLocation());
		
		if(book.getYearOfPublication()!=null)
			temp.setYearOfPublication(book.getYearOfPublication());
		
		if(book.getPublisher()!=null)
			temp.setPublisher(book.getPublisher());
		
		if(book.getStatus()!=null)
			temp.setStatus(book.getStatus());
		
		if(book.getTitle()!=null)
			temp.setTitle(book.getTitle());
		
		temp.setNoOfCopies(book.getNoOfCopies());
		
	    entitymanager.merge(temp);
		
		entitymanager.getTransaction().commit();
		entitymanager.close();
		return true;
	}
	
	public Keywords getKeyword(String keyword) {

		EntityManager entitymanager = emfactory.createEntityManager();
		Query query = entitymanager.createQuery("SELECT k from Keywords k where k.name='" + keyword +"'");
		Keywords keywordObj = (Keywords) query.getSingleResult();
		entitymanager.close();
		return keywordObj;

	}
	
	public boolean updateKeyword(Keywords keyword) {

		EntityManager entitymanager = emfactory.createEntityManager();		
		entitymanager.getTransaction().begin();
		
		Keywords temp = entitymanager.find(Keywords.class, keyword.getId());	
		
		if(temp == null){
			return false;	
		}
			
		temp.setId(keyword.getId());
		temp.setId(keyword.getId());
	    entitymanager.merge(temp);
		
		entitymanager.getTransaction().commit();
		entitymanager.close();
		return true;

	}

	@Override
	public Book getBook(int bookId) {

		EntityManager entitymanager = emfactory.createEntityManager();
		Query query = entitymanager.createQuery("SELECT b from Book b where b.id='" + bookId +"'");
		Book book = (Book) query.getSingleResult();
		entitymanager.close();

		return book;
	}

	@Override
	public boolean createCirculation(Circulation circulation) {
		try {
			EntityManager entitymanager = emfactory.createEntityManager();
			entitymanager.getTransaction().begin();
			entitymanager.persist(circulation);
			entitymanager.getTransaction().commit();
			entitymanager.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Circulation getCirculation(int userId, int bookId) {

		EntityManager entitymanager = emfactory.createEntityManager();
		Query query = entitymanager.createQuery("SELECT c from Circulation c where c.userId='" + userId + "' AND c.bookId='" + bookId + "'");
		Circulation circulation = (Circulation) query.getSingleResult();
		entitymanager.close();

		return circulation;
	}
	
	@Override
	public List<Circulation> getCirculationForUser(int userId) {

		EntityManager entitymanager = emfactory.createEntityManager();
		Query query = entitymanager.createQuery("SELECT c from Circulation c where c.userId=" + userId);
		List<Circulation> circulation =  (List<Circulation>) query.getResultList();
		entitymanager.close();

		return circulation;
	}

	@Override
	public boolean deleteCirculation(Circulation circulation) {
		EntityManager entitymanager = emfactory.createEntityManager();
		entitymanager.getTransaction().begin();

		Circulation temp = entitymanager.find(Circulation.class, circulation.getId());

		if (temp == null) {
			return false;
		}
		entitymanager.remove(temp);
		entitymanager.getTransaction().commit();
		entitymanager.close();
		return true;
	}

	@Override
	public boolean resetCheckoutDate(Circulation circulation, java.sql.Date renewedDate) {
		EntityManager entitymanager = emfactory.createEntityManager();
		entitymanager.getTransaction().begin();

		Circulation temp = entitymanager.find(Circulation.class, circulation.getId());

		if (temp == null) {
			return false;
		}
		temp.setCheckoutDate(renewedDate);
		temp.setCountOfRenewal(circulation.getCountOfRenewal());
		entitymanager.merge(temp);
		entitymanager.getTransaction().commit();
		entitymanager.close();

		return false;
	}


	public boolean updateUserStatus(User user) {
		EntityManager entitymanager = emfactory.createEntityManager();
		entitymanager.getTransaction().begin();

		User temp = entitymanager.find(User.class, user.getId());

		if (temp == null) {
			return false;
		}
		temp.setStatus(true);
		entitymanager.merge(temp);
		entitymanager.getTransaction().commit();
		entitymanager.close();
		return true;
	}
	
	protected FullTextEntityManager getFullTextEntityManager() {
	        if (ftem == null) {
	            ftem = Search.getFullTextEntityManager(emfactory.createEntityManager());
	        }
	        return ftem;
	    }
	
	@SuppressWarnings("unchecked")
    public List<Book> search(String searchString) {
 
		EntityManager em = emfactory.createEntityManager();
		FullTextEntityManager fullTextEntityManager =
		    org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
		QueryBuilder qb = getFullTextEntityManager().getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
		org.apache.lucene.search.Query luceneQuery = qb
		  .keyword()
		  .onFields("author", "title","keywords.name","createdBy.emailAddress")
		  .matching(searchString)
		  .createQuery();

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query jpaQuery =
		    fullTextEntityManager.createFullTextQuery(luceneQuery, Book.class);

		// execute search
		List result = jpaQuery.getResultList();

		em.getTransaction().commit();
		em.close();
		
        return result;
    }
	
	public List<Book> searchPatron(String searchString) {
		EntityManager em = emfactory.createEntityManager();
		FullTextEntityManager fullTextEntityManager =
		    org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
		QueryBuilder qb = getFullTextEntityManager().getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
		org.apache.lucene.search.Query luceneQuery = qb
		  .keyword()
		  .onFields("author", "title","keywords.name")
		  .matching(searchString)
		  .createQuery();

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query jpaQuery =
		    fullTextEntityManager.createFullTextQuery(luceneQuery, Book.class);

		// execute search
		List result = jpaQuery.getResultList();

		em.getTransaction().commit();
		em.close();
		
        return result;
	}
	public boolean deleteBooks(List<Integer> id) {		
		
		System.out.println("DELETING FROM DAO");
		EntityManager entitymanager = emfactory.createEntityManager();		
		entitymanager.getTransaction().begin();
		StringBuilder idString = new StringBuilder();
		for(int i : id)
		{
			idString.append(i+",");
		}
		String str = idString.substring(0, idString.length()-1);
		System.out.println(str);
		Query query = entitymanager.createQuery(
			      "DELETE FROM Book b WHERE b.id  in ("+str+")");
	    int res = query.executeUpdate();
			  
		
		entitymanager.getTransaction().commit();
		entitymanager.close();
		
		return true;
	}

}

