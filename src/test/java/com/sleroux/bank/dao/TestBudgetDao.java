package com.sleroux.bank.dao;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.testutils.BudgetHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestBudgetDao {
	@Autowired
	IBudgetDao	budgetDao;

	@Test
	@Transactional
	public void testGetYears() {

		{
			Budget b = BudgetHelper.createCredit();
			b.setYear(2015);
			budgetDao.create(b);
		}

		{
			Budget b = BudgetHelper.createCredit();
			b.setYear(2016);
			budgetDao.create(b);
		}

		List<Integer> list = budgetDao.getYears();
		Assert.assertEquals(2, list.size());
	}

	@Test
	@Transactional
	public void getGetCredits() {

		{
			Budget b = BudgetHelper.createCredit();
			b.setYear(2015);
			b.setCatego("SALAIRE");
			b.setCompte("COURANT");
			budgetDao.create(b);
		}

		{
			Budget b = BudgetHelper.createCredit();
			b.setYear(2016);
			b.setCatego("VIR.INTERNE");
			b.setCompte("COURANT");
			budgetDao.create(b);
		}

		{
			Budget b = BudgetHelper.createCredit();
			b.setYear(2016);
			b.setCatego("VIR.INTERNE");
			b.setCompte("OTHER");
			budgetDao.create(b);
		}

		List<String> list = budgetDao.getCredits();
		Assert.assertEquals(2, list.size());
	}

	@Test
	@Transactional
	public void getGetDebits() {

		{
			Budget b = BudgetHelper.createDebit();
			b.setYear(2015);
			b.setCatego("SALAIRE");
			b.setCompte("COURANT");
			budgetDao.create(b);
		}

		{
			Budget b = BudgetHelper.createDebit();
			b.setYear(2016);
			b.setCatego("VIR.INTERNE");
			b.setCompte("COURANT");
			budgetDao.create(b);
		}

		{
			Budget b = BudgetHelper.createDebit();
			b.setYear(2016);
			b.setCatego("VIR.INTERNE");
			b.setCompte("OTHER");
			budgetDao.create(b);
		}

		List<String> list = budgetDao.getDebits();
		Assert.assertEquals(2, list.size());
	}

	@Test
	@Transactional
	public void getGetMonthDebits() {

		{
			Budget b = BudgetHelper.createDebit();
			b.setMonth(2);
			b.setYear(2015);
			b.setCatego("SALAIRE");
			b.setCompte("COURANT");
			b.setDebit(new BigDecimal("1000"));
			budgetDao.create(b);
		}

		List<Budget> list = budgetDao.getMonthDebits(2015, 2);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(1000, list.get(0).getDebit().intValue());
	}

	@Test
	@Transactional
	public void getGetMonthCredit() {

		{
			Budget b = BudgetHelper.createCredit();
			b.setMonth(2);
			b.setYear(2015);
			b.setCatego("SALAIRE");
			b.setCompte("COURANT");
			b.setCredit(new BigDecimal("1000"));
			budgetDao.create(b);
		}

		List<Budget> list = budgetDao.getMonthCredits(2015, 2);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(1000, list.get(0).getCredit().intValue());
	}

	@Test
	@Transactional
	public void getGetBudgetForCompte() {

		{
			Budget b = BudgetHelper.createCredit();
			b.setMonth(2);
			b.setYear(2015);
			b.setCatego("VIR.CC");
			b.setCompte("PEL");
			b.setCredit(new BigDecimal("1000"));
			budgetDao.create(b);
		}

		Budget budget = budgetDao.getBudgetForCompte("PEL", 2015, 2);
		Assert.assertNotNull(budget);
		Assert.assertEquals(1000, budget.getCredit().intValue());
	}

	@Test
	@Transactional
	public void testFindBudget() {

		{
			Budget b = BudgetHelper.createCredit();
			b.setMonth(2);
			b.setYear(2015);
			b.setCatego("TEST_CATEGO");
			b.setCompte("COURANT");
			b.setCredit(new BigDecimal("1000"));
			budgetDao.create(b);
		}

		Budget b = budgetDao.findByYearMonthCatego(2015, 2, "TEST_CATEGO", "COURANT");
		Assert.assertNotNull(b);
		Assert.assertEquals(1000, b.getCredit().intValue());
	}
	
	@Test
	@Transactional
	public void testFindBudgetNoBudget() {

		{
			Budget b = BudgetHelper.createCredit();
			b.setMonth(2);
			b.setYear(2015);
			b.setCatego("TEST_CATEGO");
			b.setCompte("COURANT");
			b.setCredit(new BigDecimal("1000"));
			budgetDao.create(b);
		}

		Budget b = budgetDao.findByYearMonthCatego(2000, 2, "TEST_CATEGO", "COURANT");
		Assert.assertNull(b);
	}
}
