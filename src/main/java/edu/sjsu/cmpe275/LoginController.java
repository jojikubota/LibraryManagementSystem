package edu.sjsu.cmpe275;

import java.io.BufferedReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import edu.sjsu.cmpe275.model.User;
import edu.sjsu.cmpe275.service.IUserService;
import edu.sjsu.cmpe275.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	IUserService userService = new UserService();

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public void registerUser(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		JSONObject userJsonObject = jObj.getJSONObject("user");
		String emailAddress = userJsonObject.getString("username");
		String token = UUID.randomUUID().toString();
		User user = new User();
		user.setEmailAddress(userJsonObject.getString("username"));
		user.setPassword(userJsonObject.getString("password"));
		user.setUserId(userJsonObject.getString("universityId"));
		user.setUserName(userJsonObject.getString("firstName"));
		user.setLastName(userJsonObject.getString("lastName"));
		user.setToken(token);

		if (emailAddress.contains("@sjsu.edu")) {
			user.setRole("librarian");

		} else {
			user.setRole("patron");
		}
		userService.createUser(user);
		userService.sendVerificationEmail(emailAddress, token);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "confirm", method = RequestMethod.POST)
	public void confirmRegistration(HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		String token = jObj.getString("token");
		String username = jObj.getString("username");
		boolean res = userService.confirmUserRegistration(username, token);
		if(res)
		{
			userService.sendConfirmationEmail(username,"Your Account verification is complete");
		}
		System.out.println(res);

	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
	
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		JSONObject jObj = new JSONObject(sb.toString());
		String email = jObj.getString("email");
		String password = jObj.getString("password");
		User user = userService.getUser(email);
		if(user == null || !user.getPassword().equals(password) || !user.isStatus())
		{
			 response.sendError(500);
		}
		else
		{
			HttpSession session = request.getSession();
			session.setAttribute("userName", user.getUserName());
			session.setAttribute("userEmailAddress", user.getEmailAddress());
		}
	}

//	@ResponseStatus(HttpStatus.OK)
//	@RequestMapping(value = "user", method = RequestMethod.POST)
//	public void user(HttpServletRequest request, HttpServletResponse response, Model model)
//			throws Exception {
//		StringBuilder sb = new StringBuilder();
//		BufferedReader br = request.getReader();
//		String str = null;
//		while ((str = br.readLine()) != null) {
//			sb.append(str);
//		}
//		JSONObject jObj = new JSONObject(sb.toString());
//		String email = jObj.getString("email");
//		User user = userService.getUser(email);
//		
//		
//	}

}
