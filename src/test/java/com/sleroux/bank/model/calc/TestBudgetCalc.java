package com.sleroux.bank.model.calc;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.sleroux.bank.model.budget.Budget;
import com.sleroux.bank.model.operation.Operation;

public class TestBudgetCalc {

	@Test
	public void testAggregation() {

		BudgetCalc calc = new BudgetCalc();

		calc.setOperations(Arrays.asList(createOperation("T_COMPTE", "10", null, "T_CATEGO"),
				createOperation("T_COMPTE", "10", null, "T_CATEGO")));
		calc.setBudget(Arrays.asList(createBudget("T_COMPTE", "10", null, "T_CATEGO"),
				createBudget("T_COMPTE", "10", null, "T_CATEGO")));

		BudgetCalcCatego c = calc.get("T_COMPTE").get(0).get("T_CATEGO");
		Assert.assertEquals(new BigDecimal("20.00"), c.getOperation());
		Assert.assertEquals(new BigDecimal("20.00"), c.getBudget());

	}

	@Test
	public void testDistinctLinesForCreditNDebit() {

		BudgetCalc calc = new BudgetCalc();

		calc.setOperations(Arrays.asList(createOperation("T_COMPTE", "10", "10", "T_CATEGO"),
				createOperation("T_COMPTE", "10", "10", "T_CATEGO")));
		calc.setBudget(Arrays.asList(createBudget("T_COMPTE", "10", "10", "T_CATEGO"),
				createBudget("T_COMPTE", "10", "10", "T_CATEGO")));

		System.out.println(calc);

		BudgetCalcCatego c = calc.get("T_COMPTE").get(0).get("T_CATEGO");
		Assert.assertEquals(new BigDecimal("20.00"), c.getOperation());
		Assert.assertEquals(new BigDecimal("20.00"), c.getBudget());

	}

	private Operation createOperation(String _compte, String credit, String debit, String catego) {
		Operation o = new Operation();
		o.setCompte(_compte);
		if (credit != null) {
			o.setCredit(new BigDecimal(credit));
		}
		if (debit != null) {
			o.setDebit(new BigDecimal(debit));
		}
		o.setCatego(catego);
		return o;
	}

	private Budget createBudget(String _compte, String credit, String debit, String catego) {
		Budget o = new Budget();
		o.setCompte(_compte);
		if (credit != null) {
			o.setCredit(new BigDecimal(credit));
		}
		if (debit != null) {
			o.setDebit(new BigDecimal(debit));
		}
		o.setCatego(catego);
		return o;
	}

}
