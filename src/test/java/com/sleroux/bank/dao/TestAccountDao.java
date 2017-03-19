package com.sleroux.bank.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.model.Account;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestAccountDao {
	@Autowired
	IAccountDao dao;

	@Test
	@Transactional
	public void createUser() {

		Account a = new Account();
		a.setType("avoin");

		dao.create(a);

		List<Account> u = dao.findAll();
		Assert.assertTrue(u.size() > 0);

	}

}
