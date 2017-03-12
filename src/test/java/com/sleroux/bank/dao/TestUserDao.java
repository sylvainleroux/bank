package com.sleroux.bank.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestUserDao {
	@Autowired
	IUserDao userDao;

	@Test
	@Transactional
	public void createUser() {

		User user = new User();
		user.setUsername("sleroux");
		user.setPasswordEnc("avion");

		userDao.create(user);
		
		List<User> u = userDao.findAll();
		System.out.println(u.size());
		

	}

}
