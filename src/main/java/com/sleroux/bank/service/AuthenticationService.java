package com.sleroux.bank.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.model.User;
import com.sleroux.bank.util.Encryption;

@Service
public class AuthenticationService {

	@Autowired
	UserService userService;

	@Transactional
	public User tryAuthentication(String _username, String _password) throws Exception {

		User user = userService.findUserByUsername(_username);

		if (user == null)
			throw new Exception("User not found");

		if (_password == null)
			throw new Exception("Empty password");

		String hash = Encryption.sha1(_password + "bank123");

		if (!hash.equals(user.getPasswordEnc())) {
			throw new Exception("Wrong password");
		}
		
		return user;

	}
}
