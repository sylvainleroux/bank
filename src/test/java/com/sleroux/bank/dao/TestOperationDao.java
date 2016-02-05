package com.sleroux.bank.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.model.AccountBalance;
import com.sleroux.bank.model.CalcResult;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.service.CategoService;
import com.sleroux.bank.testutils.OperationHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestOperationDao {

	@Autowired
	IOperationDao	operationDao;

	@Autowired
	CategoService	categoService;

	@Test
	@Transactional
	public void testGetUncategorized() {

		Operation o = OperationHelper.createCreditOperation();
		operationDao.create(o);

		List<Operation> list = operationDao.findAll();
		Assert.assertEquals(1, list.size());

		List<Operation> uncategorized = operationDao.findUncategorized();
		Assert.assertEquals(1, uncategorized.size());

		o.setCatego("TEST_CATEGO");
		operationDao.update(o);

		List<Operation> uncategorized2 = operationDao.findUncategorized();
		Assert.assertEquals(0, uncategorized2.size());

	}

	@Test
	@Transactional
	public void testGetCategoriesCredit() {

		Operation o = OperationHelper.createCreditOperation();
		operationDao.create(o);

		List<String> credits = operationDao.getCategoriesCredit();
		Assert.assertEquals(1, credits.size());
		List<String> debits = operationDao.getCategoriesDebit();
		Assert.assertEquals(0, debits.size());
	}

	@Test
	@Transactional
	public void testGetCategoriesDebit() {

		Operation o = OperationHelper.createDebitOperation();
		operationDao.create(o);

		List<String> credits = operationDao.getCategoriesCredit();
		Assert.assertEquals(0, credits.size());
		List<String> debits = operationDao.getCategoriesDebit();
		Assert.assertEquals(1, debits.size());
	}

	@Test
	@Transactional
	public void testCalcResult() {

		Calendar c = Calendar.getInstance();
		c.set(2016, 2, 4);

		Operation o = OperationHelper.createDebitOperation();
		o.setDateOperation(c.getTime());
		o.setDateValeur(c.getTime());
		o.setCatego("TEST_CATEGO");
		o.setYear(2016);
		o.setMonthAdjusted(2);

		operationDao.create(o);

		{
			List<CalcResult> list = operationDao.getCalcForMonth(2016, 2);
			Assert.assertEquals(1, list.size());
		}

		{
			List<CalcResult> list = operationDao.getCalcForMonth(2016, 1);
			Assert.assertEquals(0, list.size());
		}
	}

	@Test
	@Transactional
	public void testSoldes() {

		{
			Operation o = OperationHelper.createDebitOperation();
			o.setCompte("CMB");
			o.setMontant(new BigDecimal("-10.50"));
			operationDao.create(o);
		}

		{
			Operation o = OperationHelper.createDebitOperation();
			o.setCompte("CMB");
			o.setMontant(new BigDecimal("-9.50"));
			operationDao.create(o);
		}

		{
			Operation o = OperationHelper.createCreditOperation();
			o.setCompte("CMB");
			o.setMontant(new BigDecimal("45.00"));
			operationDao.create(o);
		}

		{
			Operation o = OperationHelper.createCreditOperation();
			o.setCompte("BPO");
			o.setMontant(new BigDecimal("25.00"));
			operationDao.create(o);
		}

		List<AccountBalance> list = operationDao.getSoldes();
		for (AccountBalance a : list) {
			Assert.assertEquals(new BigDecimal("25").setScale(2), a.getSolde());
		}

	}


}
