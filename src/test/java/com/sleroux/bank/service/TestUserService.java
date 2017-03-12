package com.sleroux.bank.service;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sleroux.bank.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)

public class TestUserService {

	@Autowired
	UserService userService;

	@Test
	@Transactional
	public void testUserServiceListUsers() throws Exception {
		
		userService.addUser("sleroux","pomme","avom");
		
		userService.addUser("sleroux2","pomme2","khjkj");
		
		userService.listUsers();
	}

}
