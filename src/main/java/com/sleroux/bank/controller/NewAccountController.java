package com.sleroux.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.domain.SessionData;
import com.sleroux.bank.model.User;
import com.sleroux.bank.service.UserService;

@Lazy
@Controller
public class NewAccountController extends AbstractController {

	@Autowired
	UserService	userService;

	@Autowired
	SessionData	sessionData;

	@Override
	public void run() throws Exception {
		System.out.println("Creating new account. Please choose a username and a password.");

		String username = null;
		String password = null;

		try {
			username = prompt("Username: ");
			password = prompt("Password: ");

		} catch (Exception e) {
			e.printStackTrace();
		}

		User user = userService.addUser(username, password);

		if (user != null) {
			System.out.println("Account created 🐼 !");

			sessionData.setUserID(user.getId());
			sessionData.setPassword(password);
			sessionData.setUsername(user.getUsername());
		}

		run(LoginController.class);

	}

}
