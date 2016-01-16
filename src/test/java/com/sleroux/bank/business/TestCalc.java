package com.sleroux.bank.business;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.business.Calc;
import com.sleroux.bank.dao.OperationDao;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.util.Config;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TestCalc {

	@Autowired
	OperationDao	operationDao;

	@Autowired
	Calc			calc;

	@Test
	@Transactional
	@Rollback(true)
	public void testCalc() throws Exception {

		Config.loadProperties();
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
		
		
		Config.loadProperties();
		{
		Operation operation = new Operation();
		operation.setCompte("BPO");
		operation.setDateOperation(new Date());
		operation.setDateOperation(new Date());
		operation.setDateValeur(new Date());
		operation.setLibelle("TEST OPERATION 1");
		operation.setCatego("CATEGO DEBIT");
		operation.setMontant(new BigDecimal("-10.50"));
		operation.setYear(Calendar.getInstance().get(Calendar.YEAR));
		operation.setMonthAdjusted(Calendar.getInstance().get(Calendar.MONTH) + 1);

		operationDao.create(operation);
		}

		calc.run();

	}
}
