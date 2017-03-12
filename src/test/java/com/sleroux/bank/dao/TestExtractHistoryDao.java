package com.sleroux.bank.dao;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.model.ExtractHistory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestExtractHistoryDao {

	@Autowired
	private IExtractHistoryDao extractHistoryDao;

	@Test
	@Transactional
	public void getNoHistory() {
		int userID = 0;
		Date d = extractHistoryDao.getLastExtractDate(userID);
		Assert.assertNull(d);

	}

	@Test
	@Transactional
	public void getHistory() {

		int userID = 1;

		Calendar c = Calendar.getInstance();
		c.set(2015, 01, 01);
		ExtractHistory e1 = new ExtractHistory();
		e1.setExtractDate(c.getTime());
		e1.setUserID(userID);
		extractHistoryDao.create(e1);

		c.set(2015, 02, 01);
		ExtractHistory e2 = new ExtractHistory();
		e2.setExtractDate(c.getTime());
		e2.setUserID(userID);
		extractHistoryDao.create(e2);

		Date d = extractHistoryDao.getLastExtractDate(userID);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d);
		Assert.assertTrue(c.equals(c2));

	}
}
