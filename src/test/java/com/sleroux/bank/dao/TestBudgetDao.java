package com.sleroux.bank.dao;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.TestConfig;
import com.sleroux.bank.model.budget.Budget;
import com.sleroux.bank.testutils.BudgetHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class TestBudgetDao {
	@Autowired
	IBudgetDao budgetDao;

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

	

	@Test
	@Transactional
	public void testGetEstimatedEndOfMonthBalance2() {
		{
			Budget b = BudgetHelper.createCredit();
			b.setMonth(1);
			b.setYear(2016);
			b.setCatego("SOLDE.INIT");
			b.setCompte("COURANT");
			b.setCredit(new BigDecimal("500"));
			budgetDao.create(b);
		}

		{
			Budget b = BudgetHelper.createCredit();
			b.setMonth(2);
			b.setYear(2016);
			b.setCatego("AVION");
			b.setCompte("COURANT");
			b.setCredit(new BigDecimal("100"));
			budgetDao.create(b);
		}


	}

}
