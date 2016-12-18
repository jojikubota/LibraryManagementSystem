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
import org.springframework.web.bind.annotation.ResponseBody;

import edu.sjsu.cmpe275.model.Book;
import edu.sjsu.cmpe275.model.Circulation;
import edu.sjsu.cmpe275.model.RenewBooks;
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
				temp.setNoOfCopies(book.getNoOfCopies() - 1);

				if (temp.getNoOfCopies() == 0) {
					temp.setStatus("unavailable");
				}

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

	@RequestMapping(value = "/return", method = RequestMethod.DELETE)
	public void returnBook(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

		List<Integer> returningBooks = new ArrayList<Integer>();

		String userEmailAddress = (String) request.getSession().getAttribute("userEmailAddress");
		User user = userService.getUser(userEmailAddress);

		List<Book> bookListForMessages = new ArrayList<Book>();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		JSONArray bookObj = jObj.getJSONArray("books");
		if (bookObj != null) {
			int len = bookObj.length();
			for (int i = 0; i < len; i++) {
				JSONObject obj = (JSONObject) bookObj.get(i);
				returningBooks.add(obj.getInt("bookId"));
			}

			if (returningBooks.size() <= 10) { // A patron must be able to
												// return up
												// to 10 books in one
												// transaction.

				// Check for fines while returning books.
				for (int i = 0; i < returningBooks.size(); i++) {
					Circulation circulation = circulationService.getCirculation(user.getId(), returningBooks.get(i));

					// java.sql.Date checkoutDate =
					// circulation.getCheckoutDate();
					// Calendar calendar = Calendar.getInstance();
					// calendar.setTime(checkoutDate);
					// calendar.add(Calendar.MONTH, 1);

					circulationService.deleteCirculation(circulation);

					// Increment Book number of copies
					Book book = bookService.findOneBook(returningBooks.get(i));

					bookListForMessages.add(book);
					Book temp = new Book();
					temp.setId(book.getId());
					temp.setNoOfCopies(book.getNoOfCopies() + 1);

					bookService.updateBook(temp);

				}

				// Generate message for books returned
				/*** can be optimized ***/
				String returnedBookMessage = "You returned following books.\n";
				for (int i = 0; i < bookListForMessages.size(); i++) {
					returnedBookMessage += "Title: " + bookListForMessages.get(i).getTitle() + "\n" + "Author: "
							+ bookListForMessages.get(i).getAuthor() + "\n\n";
				}
				Calendar calendar = Calendar.getInstance();
				returnedBookMessage += "Time: " + calendar.getTime();
				returnedBookMessage += "Thank you for returning the books.";

				// Send email
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
				try {
					email.setTo(user.getEmailAddress());
					email.setText(returnedBookMessage);
					mailSender.send(message);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				response.sendError(400);
			}
		}
	}

	@RequestMapping(value = "/renew", method = RequestMethod.POST)
	public @ResponseBody List<RenewBooks> extendBook(HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {

		String userEmailAddress = (String) request.getSession().getAttribute("userEmailAddress");
		User user = userService.getUser(userEmailAddress);
		List<RenewBooks> list = new ArrayList<RenewBooks>();
		// Separate renewalble vs not
		List<Book> renewableBooks = new ArrayList<Book>();
		List<Book> unrenewableBooks = new ArrayList<Book>();
		List<Book> renewedCoundExceedBooks = new ArrayList<Book>();

		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		JSONArray bookObj = jObj.getJSONArray("books");
		if (bookObj != null) {
			int len = bookObj.length();
			for (int i = 0; i < len; i++) {
				JSONObject obj = (JSONObject) bookObj.get(i);
				int bookId = obj.getInt("bookId");
				Book book = bookService.findOneBook(bookId);
				// No waitlist
				if (book.getWaitlist().size() == 0) {
					renewableBooks.add(book);
				} else { // other users waiting
					unrenewableBooks.add(book);
				}
			}

			// Reset dates
			for (int i = 0; i < renewableBooks.size(); i++) {
				Circulation circulation = circulationService.getCirculation(user.getId(),
						renewableBooks.get(i).getBookId());
				if (circulation.getCountOfRenewal() == 2) {
					renewableBooks.remove(i);
					Book book = bookService.findOneBook(circulation.getBookId());
					renewedCoundExceedBooks.add(book);

				} else {
					circulation.setCountOfRenewal(circulation.getCountOfRenewal() + 1);
					circulationService.resetCheckoutDate(circulation);
				}
			}

			// Generate message for books successfully renewed
			/*** can be optimized ***/
			String renewableMessage = "You are renewing following books.\n";
			for (int i = 0; i < renewableBooks.size(); i++) {
				renewableMessage += "Book Title: " + renewableBooks.get(i).getTitle() + "\n" + "Author: "
						+ renewableBooks.get(i).getAuthor() + "\n\n";
			}
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, 1);
			String dueDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			System.out.println(dueDate);
			renewableMessage += "Return by " + dueDate + "\n\n";

			// Generate message for books placed in waitlist
			/*** can be optimized ***/
			String unrenewableMessage = "Renewal failed for following books.\n";
			for (int i = 0; i < unrenewableBooks.size(); i++) {
				unrenewableMessage += "Book Title: " + unrenewableBooks.get(i).getTitle() + "\n" + "Author: "
						+ unrenewableBooks.get(i).getAuthor() + "\n\n";
			}
			unrenewableMessage += "We will notify you when the books are available.";

			RenewBooks checkout = new RenewBooks();
			checkout.setMessageString("checkoutMessage");
			checkout.setRenewableBooks(renewableBooks);

			RenewBooks waiting = new RenewBooks();
			waiting.setMessageString("waitlistMessage");
			waiting.setRenewableBooks(unrenewableBooks);

			RenewBooks renewcount = new RenewBooks();
			checkout.setMessageString("renewCountExceedMessage");
			checkout.setRenewableBooks(renewedCoundExceedBooks);

			list.add(checkout);
			list.add(waiting);
			list.add(renewcount);

			String renewableCountExceededMessage = "Renewal failed for following books as you already renewed it 2 times.\n";
			for (int i = 0; i < renewedCoundExceedBooks.size(); i++) {
				renewableCountExceededMessage += "Book Title: " + renewedCoundExceedBooks.get(i).getTitle() + "\n"
						+ "Author: " + renewedCoundExceedBooks.get(i).getAuthor() + "\n\n";
			}

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
			try {
				email.setTo(user.getEmailAddress());
				if (renewableBooks.size() > 0)
					email.setText(renewableMessage);
				if (unrenewableBooks.size() > 0)
					email.setText(unrenewableMessage);
				if (renewedCoundExceedBooks.size() > 0)
					email.setText(renewableCountExceededMessage);
				mailSender.send(message);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return list;
	}

}
