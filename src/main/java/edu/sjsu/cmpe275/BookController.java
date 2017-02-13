package edu.sjsu.cmpe275;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import edu.sjsu.cmpe275.model.Book;
import edu.sjsu.cmpe275.model.Circulation;
import edu.sjsu.cmpe275.model.ClientCredentials;
import edu.sjsu.cmpe275.model.Keywords;
import edu.sjsu.cmpe275.model.User;
import edu.sjsu.cmpe275.model.UserProfile;
import edu.sjsu.cmpe275.service.BookService;
import edu.sjsu.cmpe275.service.CirculationService;
import edu.sjsu.cmpe275.service.IBookService;
import edu.sjsu.cmpe275.service.ICirculationService;
import edu.sjsu.cmpe275.service.IUserService;
import edu.sjsu.cmpe275.service.UserService;

@Controller
public class BookController {
	IBookService bookService = new BookService();
	IUserService userService = new UserService();
	ICirculationService circulationService = new CirculationService();
	
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "addbook", method = RequestMethod.POST)
	public void addBook(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		JSONObject bookObj1  = jObj.getJSONObject("book");
		JSONObject bookObj  = bookObj1.getJSONObject("data");
		Book book = new Book();
		book.setAuthor(bookObj.getString("author"));
		book.setCallNumber(bookObj.getString("callNo"));
		if(!bookObj.isNull("isbn"))
		{
		book.setISBN(bookObj.getString("isbn"));
		}
	    String keywordsString = bookObj.getString("keywords");
	    String[] arr = keywordsString.split(",");
	    Set<Keywords> keywords = new HashSet<Keywords>();
	    for(String key : arr)
	    {
	    	Keywords newkeyword = new Keywords();
	    	newkeyword.setName(key);
	    	keywords.add(newkeyword);
	    }
	    User user = userService.getUser(bookObj1.getString("username"));
	    book.setCreatedBy(user);
	    book.setKeywords(keywords);
		book.setLocation(bookObj.getString("location"));
		book.setYearOfPublication(bookObj.getString("yop"));
		book.setPublisher(bookObj.getString("publisher"));
		//book.setStatus(bookObj.getString("cs"));
		book.setTitle(bookObj.getString("title"));
		book.setNoOfCopies(Integer.parseInt(bookObj.getString("noc")));
		bookService.createBook(book);
		
	}
	
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search", method = RequestMethod.POST, produces={"application/json"})
	public @ResponseBody List<Book> searchForBooks(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception
	{  
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		String keyword  = jObj.getString("keyword");
		
		 List<Book> res = bookService.searchForBooks(keyword);
		 
	   		
		    //return new ModelAndView("update", "books", res);
		    //return "{\"books\":\"" + res + "\"}";
			int i=0;
			for (Book temp : res) {
				for (Keywords keyw : temp.getKeywords()) {
					keyw.setBooks(null);
				}
				res.get(i).setKeywords(temp.getKeywords());
			//	temp.setUserList(null);
				i++;
			}
		    return res;
	        
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "update", method = RequestMethod.POST, produces={"application/json"})
	public @ResponseBody void updateBooks(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception
	{  
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		JSONObject bookObj  = jObj.getJSONObject("book");
		Book book = new Book();
		book.setBookId(bookObj.getInt("bookId"));
		book.setAuthor(bookObj.getString("author"));
		book.setCallNumber(bookObj.getString("callNumber")); 
		book.setLocation(bookObj.getString("location"));
		book.setYearOfPublication(bookObj.getString("yearOfPublication"));
		book.setPublisher(bookObj.getString("publisher"));
		book.setTitle(bookObj.getString("title"));
		book.setNoOfCopies(Integer.parseInt(bookObj.getString("noOfCopies")));
		bookService.updateBook(book);
		
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "delete", method = RequestMethod.POST, produces={"application/json"})
	public @ResponseBody void deleteBooks(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception
	{  
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		JSONArray bookObj  = jObj.getJSONArray("bookIds");
		List<Integer> list = new ArrayList<Integer>();
		if (bookObj != null) { 
			   int len = bookObj.length();
			   for (int i=0;i<len;i++){ 
			    list.add(bookObj.getInt(i));
			   } 
			} 
		

		bookService.deleteBook(list);
	
	}
	
	private static Book queryGoogleBooks(com.google.api.client.json.JsonFactory jsonFactory, String query, String isbn) throws Exception {
		ClientCredentials.errorIfNotSpecified();
		Book book = new Book();
		// Set up Books client.
		final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
				.setApplicationName("CMPE275Books")
				.setGoogleClientRequestInitializer(new BooksRequestInitializer(ClientCredentials.API_KEY))
				.build();
		// Set query string and filter only Google eBooks.
		System.out.println("Query: [" + query + "]");
		Books.Volumes.List volumesList = books.volumes().list(query);
		//volumesList.setFilter("ebooks");

		// Execute the query.
		Volumes volumes = volumesList.execute();
		if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
			System.out.println("No matches found.");
			return null;
		}

		//GET FIRST BOOK DETAILS ONLY
		Volume volume = volumes.getItems().get(0);
		// Output results.
		Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
		// Title.
		book.setTitle(volumeInfo.getTitle());

		// Author(s).
		java.util.List<String> authors = volumeInfo.getAuthors();


		if (authors != null && !authors.isEmpty()) {
			book.setAuthor(volumeInfo.getAuthors().get(0));
		}
		if (volumeInfo.getPublisher() != null) {
			book.setPublisher(volumeInfo.getPublisher());
		}

		book.setYearOfPublication(volumeInfo.getPublishedDate());
		book.setISBN(isbn);


		return book;
	}
	
	@RequestMapping(value = "isbnbook", method = RequestMethod.POST)
	public @ResponseBody
	Book isbnBook(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		String bookObj  = jObj.getString("isbn");

		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

		try {
			// Success!
			return queryGoogleBooks(jsonFactory, "isbn:"+bookObj, bookObj);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		return null;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "searchPatron", method = RequestMethod.POST, produces={"application/json"})
	public @ResponseBody List<Book> searchPatronBooks(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception
	{  
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		String keyword  = jObj.getString("keyword");
		
		 List<Book> res = bookService.searchPatronBooks(keyword);
		    List<Book> list = new ArrayList<Book>();
		   	for(Book book1 : res)
		   	{
		   		Book temp = new Book();
		   		temp.setAuthor(book1.getAuthor());
		   		temp.setBookId(book1.getBookId());
		   		temp.setCallNumber(book1.getCallNumber());
		   		temp.setLocation(book1.getLocation());
		   		temp.setNoOfCopies(book1.getNoOfCopies());
		   		temp.setPublisher(book1.getPublisher());
		   		temp.setYearOfPublication(book1.getYearOfPublication());
		   		temp.setTitle(book1.getTitle());
		   		list.add(temp);
		   	}
			
		    //return new ModelAndView("update", "books", res);
		    //return "{\"books\":\"" + res + "\"}";
		    return list;
	        
	}
	

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "getBooksForUser", method = RequestMethod.GET, produces={"application/json"})
	public @ResponseBody Set<UserProfile> getBooksForUser(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception
	{  
		HttpSession session = request.getSession();
		String userEmailAddress = (String)session.getAttribute("userEmailAddress");
		List<Circulation> circulation = null;
		Set<UserProfile> userProfile = new HashSet<UserProfile>();
		
		if(userEmailAddress!=null)
		{
			User user = userService.getUser(userEmailAddress);
			circulation = (List<Circulation>)circulationService.getCirculationForUser(user.getId());
			
			for(Circulation entry : circulation)
			{
				Book book = bookService.findOneBook(entry.getBookId());
				
				UserProfile profile = new UserProfile();
				profile.setAuthor(book.getAuthor());
				profile.setTitle(book.getTitle());
				profile.setCheckoutDate(entry.getCheckoutDate());
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(entry.getCheckoutDate());
				calendar.add(Calendar.MONTH, 1);
				String dueDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
				
				profile.setDueDate(dueDate);
				profile.setFine(entry.getFine());
				profile.setBookId(book.getId());
				userProfile.add(profile);
			}

		}
		return userProfile;
	}
	
	
	
	
	
	

}