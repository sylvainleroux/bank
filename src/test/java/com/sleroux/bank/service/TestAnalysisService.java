package com.sleroux.bank.service;

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
import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.domain.AlertType;
import com.sleroux.bank.model.AnalysisFact;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.testutils.BudgetHelper;
import com.sleroux.bank.testutils.OperationHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestAnalysisService {

	@Autowired
	AnalysisService	as;

	@Autowired
	IOperationDao	operationDao;

	@Autowired
	IBudgetDao		budgetDao;

	@Transactional
	@Test
	public void testUnbudgetedOperation() {

		Operation o1 = OperationHelper.createDebitOperation(this.getClass());
		o1.setMonthAdjusted(12);
		o1.setYear(2015);
		operationDao.create(o1);

		List<AnalysisFact> list = as.getFacts(2015, 12);
		Assert.assertEquals(1, list.size());

		Assert.assertEquals(AlertType.DEBIT_NOT_BUDGETED, list.get(0).getReason());

	}

	@Transactional
	@Test
	public void testMatchingOperation() {

		Operation o1 = OperationHelper.createDebitOperation(this.getClass());
		o1.setMonthAdjusted(12);
		o1.setYear(2015);
		o1.setDebit(new BigDecimal(100));
		o1.setCatego("TEST_CATEGO");
		operationDao.create(o1);

		Budget b1 = BudgetHelper.createDebit();
		b1.setCompte("CMB");
		b1.setYear(2015);
		b1.setMonth(12);
		b1.setCatego("TEST_CATEGO");
		b1.setDebit(new BigDecimal(100));
		budgetDao.create(b1);

		List<AnalysisFact> list = as.getFacts(2015, 12);
		Assert.assertEquals(0, list.size());

	}

	@Transactional
	@Test
	public void testBurnedBudget() {

		Operation o1 = OperationHelper.createDebitOperation(this.getClass());
		o1.setMonthAdjusted(12);
		o1.setYear(2015);
		o1.setDebit(new BigDecimal(120));
		o1.setCatego("TEST_CATEGO");
		operationDao.create(o1);

		Budget b1 = BudgetHelper.createDebit();
		b1.setCompte("CMB");
		b1.setYear(2015);
		b1.setMonth(12);
		b1.setCatego("TEST_CATEGO");
		b1.setDebit(new BigDecimal(100));
		budgetDao.create(b1);

		List<AnalysisFact> list = as.getFacts(2015, 12);
		Assert.assertEquals(1, list.size());

	}

	@Transactional
	@Test
	public void testCoolBudget() {

		Operation o1 = OperationHelper.createDebitOperation(this.getClass());
		o1.setMonthAdjusted(12);
		o1.setYear(2015);
		o1.setDebit(new BigDecimal(20));
		o1.setCatego("TEST_CATEGO");
		operationDao.create(o1);

		Budget b1 = BudgetHelper.createDebit();
		b1.setCompte("CMB");
		b1.setYear(2015);
		b1.setMonth(12);
		b1.setCatego("TEST_CATEGO");
		b1.setDebit(new BigDecimal(100));
		budgetDao.create(b1);

		List<AnalysisFact> list = as.getFacts(2015, 12);
		Assert.assertEquals(0, list.size());

	}

	@Transactional
	@Test
	public void testUncategorized() {

		Operation o1 = OperationHelper.createDebitOperation(this.getClass());
		o1.setMonthAdjusted(12);
		o1.setYear(2015);
		o1.setDebit(new BigDecimal(20));
		o1.setCatego(null);
		operationDao.create(o1);

		Budget b1 = BudgetHelper.createDebit();
		b1.setCompte("CMB");
		b1.setYear(2015);
		b1.setMonth(12);
		b1.setCatego("TEST_CATEGO");
		b1.setDebit(new BigDecimal(100));
		budgetDao.create(b1);

		List<AnalysisFact> list = as.getFacts(2015, 12);
		Assert.assertEquals(1, list.size());

	}

	@Transactional
	@Test
	public void testBudgetWithNoOps() {

		Budget b1 = BudgetHelper.createDebit();
		b1.setCompte("CMB");
		b1.setYear(2015);
		b1.setMonth(12);
		b1.setCatego("TEST_CATEGO");
		b1.setDebit(new BigDecimal(100));
		budgetDao.create(b1);

		List<AnalysisFact> list = as.getFacts(2015, 12);
		Assert.assertEquals(0, list.size());

	}

	@Transactional
	@Test
	public void testCoolBudget2() {

		Operation o1 = OperationHelper.createDebitOperation(this.getClass());
		o1.setMonthAdjusted(12);
		o1.setYear(2015);
		o1.setDebit(new BigDecimal(20));
		o1.setCatego("TEST_CATEGO");
		operationDao.create(o1);

		Budget b1 = BudgetHelper.createDebit();
		b1.setCompte("CMB");
		b1.setYear(2015);
		b1.setMonth(12);
		b1.setCatego("TEST_CATEGO");
		b1.setDebit(new BigDecimal(100));
		budgetDao.create(b1);

		List<AnalysisFact> list = as.getFacts(2015, 11);
		Assert.assertEquals(1, list.size());

	}

	@Transactional
	@Test
	public void testBudgetFromLastMonthNotCompleted() {

		Operation o1 = OperationHelper.createDebitOperation(this.getClass());
		o1.setMonthAdjusted(4);
		o1.setYear(2016);
		o1.setDebit(new BigDecimal(98.58));
		o1.setCatego("CARBURANT");
		operationDao.create(o1);

		Budget b1 = BudgetHelper.createDebit();
		b1.setCompte("CMB.COMPTE_CHEQUE");
		b1.setYear(2016);
		b1.setMonth(4);
		b1.setCatego("CARBURANT");
		b1.setDebit(new BigDecimal(100));
		budgetDao.create(b1);

		List<AnalysisFact> list = as.getFacts(2016, 6);
		Assert.assertEquals(1, list.size());

		Assert.assertEquals(AlertType.DEBIT_OVER_ESTIMATE, list.get(0).getReason());

	}

}
