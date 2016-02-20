package com.sleroux.bank.service;

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
import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.testutils.BudgetHelper;

// http://docs.spring.io/spring/docs/current/spring-framework-reference/html/integration-testing.html

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestCalcService {

	@Autowired
	IOperationDao	operationDao;

	@Autowired
	CalcService		calcService;

	@Autowired
	IBudgetDao		budgetDao;

	@Test
	@Transactional
	public void testCalcCreditOnly() throws Exception {

		{
			Operation operation = new Operation();
			operation.setCompte("BPO");
			operation.setDateOperation(new Date());
			operation.setDateOperation(new Date());
			operation.setDateValeur(new Date());
			operation.setLibelle("TEST OPERATION 1");
			operation.setCatego("CATEGO CREDIT");
			operation.setCredit(new BigDecimal("10.50"));
			operation.setYear(Calendar.getInstance().get(Calendar.YEAR));
			operation.setMonthAdjusted(Calendar.getInstance().get(Calendar.MONTH) + 1);

			operationDao.create(operation);

			calcService.run();
		}

	}

	@Test
	@Transactional
	public void testDebitOnly() throws Exception {

		{
			Operation operation = new Operation();
			operation.setCompte("BPO");
			operation.setDateOperation(new Date());
			operation.setDateOperation(new Date());
			operation.setDateValeur(new Date());
			operation.setLibelle("TEST OPERATION 1");
			operation.setCatego("CATEGO DEBIT");
			operation.setDebit(new BigDecimal("10.50"));
			operation.setYear(Calendar.getInstance().get(Calendar.YEAR));
			operation.setMonthAdjusted(Calendar.getInstance().get(Calendar.MONTH) + 1);

			operationDao.create(operation);

			calcService.run();
		}

	}

	@Test
	@Transactional
	public void testBudgetedCredit() throws Exception {

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

		{
			Operation operation = new Operation();
			operation.setCompte("BPO");
			operation.setDateOperation(new Date());
			operation.setDateOperation(new Date());
			operation.setDateValeur(new Date());
			operation.setLibelle("TEST OPERATION 1");
			operation.setCatego("CATEGO CREDIT");
			operation.setCredit(new BigDecimal("10.50"));
			operation.setYear(year);
			operation.setMonthAdjusted(month);
			operationDao.create(operation);

			Budget b = BudgetHelper.createCredit();
			b.setYear(year);
			b.setMonth(month);
			b.setCredit(new BigDecimal("10.50"));
			b.setCompte("COURANT");
			b.setCatego("CATEGO CREDIT");
			budgetDao.create(b);

			calcService.run();
		}
	}

	@Test
	@Transactional
	public void testMultiAccount() throws Exception {

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

		{
			Operation operation = new Operation();
			operation.setCompte("BPO");
			operation.setDateOperation(new Date());
			operation.setDateOperation(new Date());
			operation.setDateValeur(new Date());
			operation.setLibelle("TEST OPERATION 1");
			operation.setCatego("CATEGO CREDIT");
			operation.setCredit(new BigDecimal("10.50"));
			operation.setYear(year);
			operation.setMonthAdjusted(month);
			operationDao.create(operation);
		}

		{
			Operation operation = new Operation();
			operation.setCompte("LIVRET");
			operation.setDateOperation(new Date());
			operation.setDateOperation(new Date());
			operation.setDateValeur(new Date());
			operation.setLibelle("TEST OPERATION 1");
			operation.setCatego("VIREMENT");
			operation.setCredit(new BigDecimal("100"));
			operation.setYear(year);
			operation.setMonthAdjusted(month);
			operationDao.create(operation);
		}
		{
			Budget b = BudgetHelper.createCredit();
			b.setYear(year);
			b.setMonth(month);
			b.setCredit(new BigDecimal("10.50"));
			b.setCompte("COURANT");
			b.setCatego("CATEGO CREDIT");
			budgetDao.create(b);
		}
		
		{
			Budget b = BudgetHelper.createCredit();
			b.setYear(year);
			b.setMonth(month);
			b.setCredit(new BigDecimal("100"));
			b.setCompte("LIVRET");
			b.setCatego("VIREMENT");
			budgetDao.create(b);
		}


		calcService.run();

	}

}
