package edu.sjsu.cmpe275.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.hibernate.Hibernate;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.model.Book;
import edu.sjsu.cmpe275.model.Circulation;
import edu.sjsu.cmpe275.model.TimeModel;
import edu.sjsu.cmpe275.model.User;

@Service
public class ReminderService implements IReminderService{


	private ICirculationService circulationService = new CirculationService();
	
	private IUserService userService = new UserService();
	
	private IBookService bookService = new BookService();

	private IDateService dateService = new DateService();

	private List<Circulation> circulations = new ArrayList<Circulation>();
	private List<User> users = new ArrayList<User>();
	
	public void checkFine() throws Exception {
		
		circulations = circulationService.getAllCirculations();

		for (Circulation circulation : circulations) {
			// Check due date
			java.sql.Timestamp checkoutDate = circulation.getCheckoutDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(checkoutDate);
			calendar.add(Calendar.MONTH, 1);
			java.sql.Timestamp dueDate = new java.sql.Timestamp(calendar.getTimeInMillis());

		
		TimeModel timeModel = (TimeModel) dateService.getTime();
		java.sql.Timestamp appTime;
		if (timeModel != null) {
			java.util.Date utilDate = timeModel.getDate();
			appTime = new java.sql.Timestamp(utilDate.getTime());
		} else {
			appTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
		}

		long diff = appTime.getTime() - dueDate.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		long remainder = diff % (24 * 60 * 60 * 1000);
		if (remainder > 0)
			diffDays = diffDays + 1;
		if(diffDays > 0)
		{
		int fine =  (int)diffDays;
		circulation.setFine(Math.abs(fine));
		circulationService.updateCirculation(circulation);
		}
		
		}
		
	}

//	@Scheduled(fixedRate = 60000) // 1 minute
	public void checkDueDate() throws Exception {

		circulations = circulationService.getAllCirculations();

		for (Circulation circulation : circulations) {
			// Check due date
			java.sql.Timestamp checkoutDate = circulation.getCheckoutDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(checkoutDate);
			calendar.add(Calendar.MONTH, 1);
			java.sql.Timestamp dueDate = new java.sql.Timestamp(calendar.getTimeInMillis());

			// update circulation table with fine
			TimeModel timeModel = (TimeModel) dateService.getTime();
			java.sql.Timestamp appTime;
			if (timeModel != null) {
				java.util.Date utilDate = timeModel.getDate();
				appTime = new java.sql.Timestamp(utilDate.getTime());
			} else {
				appTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
			}

			long diff = appTime.getTime() - dueDate.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			long remainder = diff % (24 * 60 * 60 * 1000);
			if (remainder > 0)
				diffDays = diffDays + 1;
			int fine = (int) diffDays;
//			circulation.setFine(Math.abs(fine));
//			circulationService.updateCirculation(circulation);
			// Due in less than five days (= 432000000 milliseconds)

			long diffTime =  dueDate.getTime() - appTime.getTime();
			long dueDays = diffTime / (24 * 60 * 60 * 1000);

			if (dueDays >= 0 && dueDays <= 5) {
				if (!circulation.getEmailSent()) {

					// Set emailSent flag to true
					circulation.setEmailSent(true);
					circulationService.updateCirculation(circulation);
					
					// Send email
					String messege = "Your book, " + bookService.findOneBook(circulation.getBookId()).getTitle()
							+ " is due in " + Math.abs(fine) + " days.";
					userService.sendConfirmationEmail(userService.getUser(circulation.getUserId()).getEmailAddress(),
							messege);

					
				}
			}
		}
	}

//	@Scheduled(fixedRate = 86400000) // 24 hours
//	public void resetEmailSentFlag() {
//
//		circulations = circulationService.getAllCirculations();
//
//		for (Circulation circulation : circulations) {
//			circulation.setEmailSent(false);
//		}
//	}
//
//	@Scheduled(fixedRate = 86400000) // 24 hours
	public void checkBooksOnHold() throws Exception {
		/** NOT THE MOST EFFICIENT **/

		// get all users
		users = userService.getAllUsers();

		for (User user : users) {
			// check if user has any books on hold to checkout
			if (user.getBooksOnHold().size() > 0) {
				for (Map.Entry<Integer, java.sql.Date> entry : user.getBooksOnHold().entrySet()) {

					TimeModel timeModel = (TimeModel)dateService.getTime();
					final java.sql.Date notifiedDate;
					if(timeModel != null)
					{
						 java.util.Date utilDate = timeModel.getDate();
						 notifiedDate = new java.sql.Date(utilDate.getTime());
					}
					else
					{
						notifiedDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());	
					}
					
					long diff = notifiedDate.getTime() - entry.getValue().getTime();
					long diffDays = diff / (24 * 60 * 60 * 1000);
					
					/** SHOUNAK > BELOW NEED UPDATE TOO **/
					if (diffDays >= 3) { // Hold time
																	// expired
						// remove the user
						System.out.println("entry key-----"+entry.getKey());
						user.getBooksOnHold().remove(entry.getKey());
						// save
						userService.updateBooksOnHold(user);

						// process next user in the waitlist if any
						final Book book = bookService.findOneBook(entry.getKey());

						Book temp = new Book();
						temp.setId(book.getId());

						// Check if there's a waitlist.
						if (book.getWaitlist().size() > 0) {

							// update book status
							temp.setStatus("hold");

							// Update waitlist
							List<User> tempUsers = book.getWaitlist();
							User firstInWaitlist = tempUsers.iterator().next();
							tempUsers.remove(firstInWaitlist);
							temp.setWaitlist(tempUsers);

							// Update booksOnHold
							/*** SHOUNAK > CHANGE THIS PART **/
//							final java.sql.Date notifiedDate = new java.sql.Date(
//									Calendar.getInstance().getTime().getTime());
							firstInWaitlist.setBooksOnHold(new HashMap<Integer, java.sql.Date>() {
								{
									put(book.getBookId(), notifiedDate);
								}
							});
							userService.updateBooksOnHold(firstInWaitlist);

							// Generate message
							String holdMessage = "Your book, " + book.getTitle() + " is available now. We will hold "
									+ "the book for 3 days. Please come and " + "pick it up.\n";

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
								email.setTo(firstInWaitlist.getEmailAddress());
								email.setText(holdMessage);
								mailSender.send(message);
							} catch (Exception e1) {
								e1.printStackTrace();
							}

						} else { // No waitlist

							// update status
							temp.setStatus("available");

						}

						// Save
						bookService.updateBook(temp);

					}
					// else { // hold not expired yet, decrement the daysLeft
					// user.getBooksOnHold().put(entry.getKey(),
					// entry.getValue() - 1);
					// }
				}

				// userService.updateBooksOnHold(user);
			}

		}

	}

}
