package edu.sjsu.cmpe275;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.api.services.books.Books;

import edu.sjsu.cmpe275.model.Book;
import edu.sjsu.cmpe275.model.Circulation;
import edu.sjsu.cmpe275.model.Keywords;
import edu.sjsu.cmpe275.model.User;
import edu.sjsu.cmpe275.service.BookService;
import edu.sjsu.cmpe275.service.CirculationService;
import edu.sjsu.cmpe275.service.IBookService;
import edu.sjsu.cmpe275.service.ICirculationService;
import edu.sjsu.cmpe275.service.IUserService;
import edu.sjsu.cmpe275.service.UserService;

/**
 * Created by joji on 12/6/16.
 */

@Controller
public class CirculationController {

	// @Autowired
	IBookService bookService = new BookService();
	IUserService userService = new UserService();
	// @Autowired
	ICirculationService circulationService = new CirculationService();
	int maxBooksPerPatron = 10;
	// User user = new User();

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutBook(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

		// Separate immediately available vs wait list
		List<Book> availableBooks = new ArrayList<Book>();
		List<Book> unavailableBooks = new ArrayList<Book>();

		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}

		// Read in the request body (Json array of 'Book')
		JSONObject jObj = new JSONObject(sb.toString());
		JSONObject reqBooks1 = jObj.getJSONObject("checkout");
		JSONArray reqBooks = reqBooks1.getJSONArray("data");
		String userEmailAddress = (String) request.getSession().getAttribute("userEmailAddress");
		User user = userService.getUser(userEmailAddress);

		// return new ModelAndView("update", "books", res);
		// return "{\"books\":\"" + res + "\"}";

		// String username = reqBooks1.getString("username");

		int len = reqBooks.length();

		for (int i = 0; i < len; i++) {
			int bookId = reqBooks.getJSONObject(i).getInt("id");
			Book book = bookService.findOneBook(bookId);
			// Available
			if (book.getStatus().equals("available")) {
				availableBooks.add(book);
			} else { // Unavailable
				unavailableBooks.add(book);
			}
		}

		List<Circulation> entry = circulationService.getCirculationForUser(user.getId());
		
	
		// Check the max checkout number
		if (availableBooks.size() > (maxBooksPerPatron - entry.size())) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			model.addAttribute("message", "Cannot keep more than 10 books at a time");
			return "bad request";
		} else { // Checkout
			for (int i = 0; i < availableBooks.size(); i++) {
				// Persist the transaction
				Circulation circulation = new Circulation();
				circulation.setUserId(user.getId());
				circulation.setBookId(availableBooks.get(i).getId());
				java.sql.Date checkoutDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
				circulation.setCheckoutDate(checkoutDate);
				circulationService.createCirculation(circulation);
				
				// decrement no of copies of book
				Book book = bookService.findOneBook(availableBooks.get(i).getId());

				
				Book temp = new Book();
				temp.setId(book.getId());
				temp.setNoOfCopies(book.getNoOfCopies() -1);
				
				bookService.updateBook(temp);

			}
		}    

		// Add to waitlist

		// ------------ check later on----------------
		// for (int i = 0; i < unavailableBooks.size(); i++) {
		//
		// unavailableBooks.get(i).getWaitList().add(user);
		// }

		// Generate message for books successfully checked out
		/*** can be optimized ***/
		String checkoutMessage = checkoutMessage = "You are checking out " + availableBooks.size() + " books.\n";
		for (int i = 0; i < availableBooks.size(); i++) {
			checkoutMessage += "Book " + i + 1 + ":\n" + "Title: " + availableBooks.get(i).getTitle() + "\n"
					+ "Author: " + availableBooks.get(i).getAuthor() + "\n\n";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		String dueDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		System.out.println(dueDate);
		checkoutMessage += "Return by " + dueDate + "\n\n";

		// Generate message for books placed in waitlist
		/*** can be optimized ***/
		// String waitlistMessage = "You are placed in wait list for " +
		// availableBooks.size() + " books.\n";
		// for (int i = 0; i < unavailableBooks.size(); i++) {
		// waitlistMessage += "Book " + i + ":\n"
		// + "Title: " + availableBooks.get(i).getTitle() + "\n"
		// + "Author: " + availableBooks.get(i).getAuthor() + "\n\n";
		// }
		// waitlistMessage += "We will notify you when the books are
		// available.";

		// Send the messge to the front end
		model.addAttribute("checkoutMessage", checkoutMessage);
		// model.addAttribute("waitlistMessage", waitlistMessage);

		// Send the message via email
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("cmpe275final@gmail.com");
		mailSender.setPassword("finalproject");

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		mailSender.setJavaMailProperties(props);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper email = new MimeMessageHelper(message);
		if (availableBooks.size() > 0) {

			try {
				email.setTo(userEmailAddress);
				email.setText(checkoutMessage);
				mailSender.send(message);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

	// @RequestMapping(value = "/return", method = RequestMethod.DELETE)
	// public void returnBook(HttpServletRequest request, Model model) throws
	// Exception {
	//
	// List<Book> returningBooks = new ArrayList<Book>();
	//
	// // Read in the request body (Json array of 'Book')
	// JSONArray reqBooks = new JSONArray(request.getReader());
	// for (int i = 0; i < reqBooks.length(); i++) {
	// JSONObject requestedBook = reqBooks.getJSONObject(i);
	// int bookId = requestedBook.getInt("id");
	// Book book = bookService.findOneBook(bookId);
	// returningBooks.add(book);
	// }
	//
	// // Check for fines while returning books.
	// for (int i = 0; i < returningBooks.size(); i++) {
	// Circulation circulation =
	// circulationService.getCirculation(user.getUserId(),
	// returningBooks.get(i).getBookId());
	//
	//// java.sql.Date checkoutDate = circulation.getCheckoutDate();
	//// Calendar calendar = Calendar.getInstance();
	//// calendar.setTime(checkoutDate);
	//// calendar.add(Calendar.MONTH, 1);
	//
	// circulationService.deleteCirculation(circulation);
	// }
	//
	// // Generate message for books returned
	// /*** can be optimized ***/
	// String returnedBookMessage = "You returned " + returningBooks.size() + "
	// books.\n";
	// for (int i = 0; i < returningBooks.size(); i++) {
	// returnedBookMessage += "Book " + i + ":\n"
	// + "Title: " + returningBooks.get(i).getTitle() + "\n"
	// + "Author: " + returningBooks.get(i).getAuthor() + "\n\n";
	// }
	// returnedBookMessage += "Thank you for returning the books.";
	//
	// // Send email
	// JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	// mailSender.setHost("smtp.gmail.com");
	// mailSender.setPort(587);
	// mailSender.setUsername("cmpe275final@gmail.com");
	// mailSender.setPassword("finalproject");
	//
	// Properties props = new Properties();
	// props.put("mail.smtp.starttls.enable", "true");
	// mailSender.setJavaMailProperties(props);
	//
	// MimeMessage message = mailSender.createMimeMessage();
	// MimeMessageHelper email = new MimeMessageHelper(message);
	// try {
	// email.setTo(user.getEmailAddress());
	// email.setText(returnedBookMessage);
	// mailSender.send(message);
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	//
	// // Read in the request body (Json array of 'Book')
	//
	// // Check for overdue fine
	//
	// // Remove each book from Circulation
	//
	// // Send confirmation email
	// }

	// @RequestMapping(value = "/renew", method = RequestMethod.PUT)
	// public void extendBook(HttpServletRequest request,
	// HttpServletResponse response,
	// Model model) throws Exception {
	//
	// // Separate renewalble vs not
	// List<Book> renewableBooks = new ArrayList<Book>();
	// List<Book> unrenewableBooks = new ArrayList<Book>();
	//
	// // Read in the request body (Json array of 'Book')
	// JSONArray reqBooks = new JSONArray(request.getReader());
	// for (int i = 0; i < reqBooks.length(); i++) {
	// JSONObject requestedBook = reqBooks.getJSONObject(i);
	// int bookId = requestedBook.getInt("id");
	// Book book = bookService.findOneBook(bookId);
	// // No waitlist
	// if (book.getWaitList().size() == 0) {
	// renewableBooks.add(book);
	// } else { // other users waiting
	// unrenewableBooks.add(book);
	// }
	// }
	//
	// // Reset dates
	// for (int i = 0; i < renewableBooks.size(); i++) {
	// Circulation circulation =
	// circulationService.getCirculation(user.getUserId(),
	// renewableBooks.get(i).getBookId());
	// circulationService.resetCheckoutDate(circulation);
	// }
	//
	// // Generate message for books successfully renewed
	// /*** can be optimized ***/
	// String renewableMessage = "You are renewing " + renewableBooks.size() + "
	// books.\n";
	// for (int i = 0; i < renewableBooks.size(); i++) {
	// renewableMessage += "Book " + i + ":\n"
	// + "Title: " + renewableBooks.get(i).getTitle() + "\n"
	// + "Author: " + renewableBooks.get(i).getAuthor() + "\n\n";
	// }
	// Calendar calendar = Calendar.getInstance();
	// calendar.add(Calendar.MONTH, 1);
	// String dueDate = new
	// SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	// System.out.println(dueDate);
	// renewableMessage += "Return by " + dueDate + "\n\n";
	//
	// // Generate message for books placed in waitlist
	// /*** can be optimized ***/
	// String unrenewableMessage = "Renewal failed for " +
	// unrenewableBooks.size() + " books.\n";
	// for (int i = 0; i < unrenewableBooks.size(); i++) {
	// unrenewableMessage += "Book " + i + ":\n"
	// + "Title: " + unrenewableBooks.get(i).getTitle() + "\n"
	// + "Author: " + unrenewableBooks.get(i).getAuthor() + "\n\n";
	// }
	// unrenewableMessage += "We will notify you when the books are available.";
	//
	// // Send the messge to the front end
	// model.addAttribute("checkoutMessage", renewableBooks);
	// model.addAttribute("waitlistMessage", unrenewableMessage);
	//
	// // Send the message via email
	// JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	// mailSender.setHost("smtp.gmail.com");
	// mailSender.setPort(587);
	// mailSender.setUsername("cmpe275final@gmail.com");
	// mailSender.setPassword("finalproject");
	//
	// Properties props = new Properties();
	// props.put("mail.smtp.starttls.enable", "true");
	// mailSender.setJavaMailProperties(props);
	//
	// MimeMessage message = mailSender.createMimeMessage();
	// MimeMessageHelper email = new MimeMessageHelper(message);
	// try {
	// email.setTo(user.getEmailAddress());
	// if (renewableBooks.size() != 0 && unrenewableBooks.size() != 0) {
	// email.setText(renewableMessage + unrenewableMessage);
	// } else if (renewableBooks.size() != 0 && unrenewableBooks.size() == 0) {
	// email.setText(renewableMessage);
	// } else if (renewableBooks.size() == 0 && unrenewableBooks.size() != 0) {
	// email.setText(unrenewableMessage);
	// }
	// mailSender.send(message);
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	// }

}
