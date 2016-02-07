package com.sleroux.bank.business;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.dao.OperationDao;
import com.sleroux.bank.model.Operation;

// http://docs.spring.io/spring/docs/current/spring-framework-reference/html/integration-testing.html

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestCalc {

	@Autowired
	OperationDao	operationDao;

	@Test
	@Transactional
	public void testCalc() throws Exception {

		{
			Operation operation = new Operation();
			operation.setCompte("BPO");
			operation.setDateOperation(new Date());
			operation.setDateOperation(new Date());
			operation.setDateValeur(new Date());
			operation.setLibelle("TEST OPERATION 1");
			operation.setCatego("CATEGO CREDIT");
			operation.setMontant(new BigDecimal("10.50"));
			operation.setYear(Calendar.getInstance().get(Calendar.YEAR));
			operation.setMonthAdjusted(Calendar.getInstance().get(Calendar.MONTH) + 1);

			operationDao.create(operation);
		}
		

	}
}
