package com.sleroux.bank.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IUserDao;
import com.sleroux.bank.model.User;
import com.sleroux.bank.util.Encryption;

@Service
public class UserService {

	@Autowired
	IUserDao userDao;

	public void listUsers() {

		List<User> users = userDao.findAll();
		if (users.size() == 0) {
			System.out.println("No Users");
		}
		users.forEach(user -> System.out.println(user.getUsername() + " (" + user.getId() + ")"));

	}

	@Transactional
	public void addUser(String _username, String _password, String _cmbLogin) throws Exception {

		User user = new User();
		user.setUsername(_username);
		user.setCmbLogin(_cmbLogin);
		user.setPasswordEnc(Encryption.sha1(_password + "bank123"));

		userDao.create(user);

	}

	public User findUserByUsername(String _username) {
		return userDao.getUserByUsername(_username);
	}

}
