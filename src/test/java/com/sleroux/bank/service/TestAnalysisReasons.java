package com.sleroux.bank.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Assert;
import org.junit.Test;

import com.sleroux.bank.domain.AlertType;
import com.sleroux.bank.model.AnalysisFact;

public class TestAnalysisReasons {

	AnalysisService	as	= new AnalysisService();

	@Test
	public void testIsPositiveNotNull() {
		Assert.assertTrue(as.isPositiveNotNull(new BigDecimal("10.20")));
		Assert.assertFalse(as.isPositiveNotNull(new BigDecimal("-10.20")));
		Assert.assertFalse(as.isPositiveNotNull(null));
		Assert.assertFalse(as.isPositiveNotNull(new BigDecimal("0.00")));

	}

	@Test
	public void testIsUnset() {
		Assert.assertTrue(as.isUnset(null));
		Assert.assertTrue(as.isUnset(new BigDecimal("00.00")));
		Assert.assertFalse(as.isUnset(new BigDecimal("00.20")));

	}

	@Test
	public void testNotBudgetedDebit() {
		AnalysisFact fact = new AnalysisFact();
		fact.setDebit_ops(m(10.2));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.DEBIT_NOT_BUDGETED, fact.getReason());
	}

	@Test
	public void testCategoConflict() {
		AnalysisFact fact = new AnalysisFact();
		fact.setDebit_ops(m(10.2));
		fact.setCredit_ops(m(10.2));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.CATEGO_CONFLICT, fact.getReason());
	}

	@Test
	public void testBurnedDebitBudget() {

		AnalysisFact fact = new AnalysisFact();
		fact.setDebit_ops(m(10.2));
		fact.setDebit_bud(m(1));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.DEBIT_BURNED, fact.getReason());
	}

	@Test
	public void testCoolDebitBudget() {

		AnalysisFact fact = new AnalysisFact();
		fact.setDebit_ops(m(10.2));
		fact.setDebit_bud(m(100));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.DEBIT_OVER_ESTIMATE, fact.getReason());
	}

	@Test
	public void testBurnedCreditBudget() {

		AnalysisFact fact = new AnalysisFact();
		fact.setCredit_ops(m(10.2));
		fact.setCredit_bud(m(1));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.CREDIT_BURNED, fact.getReason());
	}

	@Test
	public void testCoolCreditBudget() {

		AnalysisFact fact = new AnalysisFact();
		fact.setCredit_ops(m(10.2));
		fact.setCredit_bud(m(100));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.CREDIT_OVER_ESTIMATE, fact.getReason());
	}

	@Test
	public void testNotBudgetedCredit() {
		AnalysisFact fact = new AnalysisFact();
		fact.setCredit_ops(m(10.2));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.CREDIT_NOT_BUDGETED, fact.getReason());
	}

	@Test
	public void testConflict() {
		AnalysisFact fact = new AnalysisFact();
		fact.setCredit_ops(m(10.2));
		fact.setDebit_ops(m(10.2));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.CATEGO_CONFLICT, fact.getReason());
	}

	@Test
	public void testConflict2() {
		AnalysisFact fact = new AnalysisFact();
		fact.setDebit_bud(m(10.2));
		fact.setCredit_bud(m(10.2));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.CATEGO_CONFLICT, fact.getReason());
	}

	@Test
	public void testConflict3() {
		AnalysisFact fact = new AnalysisFact();
		fact.setDebit_ops(m(10.2));
		fact.setCredit_bud(m(10.2));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.CATEGO_CONFLICT, fact.getReason());
	}

	@Test
	public void testConflict4() {
		AnalysisFact fact = new AnalysisFact();
		fact.setDebit_bud(m(10.2));
		fact.setCredit_ops(m(10.2));
		as.doAnalysis(fact, 2015, 12);
		Assert.assertEquals(AlertType.CATEGO_CONFLICT, fact.getReason());
	}

	private BigDecimal m(double _s) {
		return new BigDecimal(_s).setScale(2, RoundingMode.CEILING);
	}
}
