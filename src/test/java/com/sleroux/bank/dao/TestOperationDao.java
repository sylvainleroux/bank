package com.sleroux.bank.dao;

import java.math.BigDecimal;
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
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.AccountBalance;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.service.CategoService;
import com.sleroux.bank.testutils.OperationHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestOperationDao {

	private final static int	TEST_USER_ID	= 0;

	@Autowired
	IOperationDao				operationDao;

	@Autowired
	CategoService				categoService;

	@Test
	@Transactional
	public void testGetUncategorized() {

		Operation o = OperationHelper.createCreditOperation();
		operationDao.create(o);

		List<Operation> list = operationDao.findAll();
		Assert.assertEquals(1, list.size());

		List<Operation> uncategorized = operationDao.findUncategorized(TEST_USER_ID);
		Assert.assertEquals(1, uncategorized.size());

		o.setCatego("TEST_CATEGO");
		operationDao.update(o);

		List<Operation> uncategorized2 = operationDao.findUncategorized(TEST_USER_ID);
		Assert.assertEquals(0, uncategorized2.size());

	}

	@Test
	@Transactional
	public void testGetCategoriesCredit() {

		Operation o = OperationHelper.createCreditOperation();
		operationDao.create(o);

		List<String> credits = operationDao.getCategoriesCredit(TEST_USER_ID);
		Assert.assertEquals(1, credits.size());
		List<String> debits = operationDao.getCategoriesDebit(TEST_USER_ID);
		Assert.assertEquals(0, debits.size());
	}

	@Test
	@Transactional
	public void testGetCategoriesDebit() {

		Operation o = OperationHelper.createDebitOperation();
		operationDao.create(o);

		List<String> credits = operationDao.getCategoriesCredit(TEST_USER_ID);
		Assert.assertEquals(0, credits.size());
		List<String> debits = operationDao.getCategoriesDebit(TEST_USER_ID);
		Assert.assertEquals(1, debits.size());
	}

	@Test
	@Transactional
	public void testSoldes() {

		{
			Operation o = OperationHelper.createDebitOperation();
			o.setCompte("CMB");
			o.setDebit(new BigDecimal("10.50"));
			operationDao.create(o);
		}

		{
			Operation o = OperationHelper.createDebitOperation();
			o.setCompte("CMB");
			o.setDebit(new BigDecimal("9.50"));
			operationDao.create(o);
		}

		{
			Operation o = OperationHelper.createCreditOperation();
			o.setCompte("CMB");
			o.setCredit(new BigDecimal("45.00"));
			operationDao.create(o);
		}

		{
			Operation o = OperationHelper.createCreditOperation();
			o.setCompte("BPO");
			o.setCredit(new BigDecimal("25.00"));
			operationDao.create(o);
		}

		List<AccountBalance> list = operationDao.getSoldes(TEST_USER_ID);
		for (AccountBalance a : list) {
			Assert.assertEquals(new BigDecimal("25").setScale(2), a.getSolde());
		}

	}

	@Test
	@Transactional
	public void testInsertIgnore() throws CloneNotSupportedException {
		Operation o1 = OperationHelper.createCreditOperation();
		operationDao.insertIgnore(o1);
		operationDao.insertIgnore(o1);
		Assert.assertEquals(1, operationDao.findAll().size());
	}

	@Test
	@Transactional
	public void testRetrieveAggregatedOperationsForMonth() {

		{
			Operation o = OperationHelper.createCreditOperation();
			o.setYear(2016);
			o.setMonthAdjusted(2);
			o.setCatego("AVION");
			o.setCredit(new BigDecimal("100.00"));
			operationDao.create(o);
		}

		{
			Operation o = OperationHelper.createCreditOperation();
			o.setYear(2016);
			o.setMonthAdjusted(2);
			o.setCatego("AVION");
			o.setCredit(new BigDecimal("100.00"));
			operationDao.create(o);
		}
		List<AggregatedOperations> list = operationDao.findAggregatedYearMonth(2016, 2, TEST_USER_ID);

		Assert.assertEquals(1, list.size());
		Assert.assertEquals(200, list.get(0).getCredit().intValue());

	}

	@Test
	@Transactional
	public void testFindOperationByCategoYearMonth() {

		{
			Operation o = OperationHelper.createCreditOperation();
			o.setYear(2016);
			o.setLibelle("Operation 1");
			o.setMonthAdjusted(2);
			o.setCatego("AVION");
			o.setCredit(new BigDecimal("100.00"));
			operationDao.create(o);
		}

		{
			Operation o = OperationHelper.createCreditOperation();
			o.setYear(2016);
			o.setMonthAdjusted(2);
			o.setLibelle("Operation 2");
			o.setCatego("AVION");
			o.setCredit(new BigDecimal("100.00"));
			operationDao.create(o);
		}

		{
			Operation o = OperationHelper.createCreditOperation();
			o.setYear(2016);
			o.setMonthAdjusted(2);
			o.setCatego("POMME");
			o.setCredit(new BigDecimal("100.00"));
			operationDao.create(o);
		}
		List<Operation> list = operationDao.findByCategoYearMonth(2016, 2, "AVION", TEST_USER_ID);
		Assert.assertEquals(2, list.size());

	}

}
