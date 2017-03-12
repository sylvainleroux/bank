package com.sleroux.bank.testutils;

import java.math.BigDecimal;

import com.sleroux.bank.model.Budget;

public class BudgetHelper {

	public static Budget createCredit() {
		Budget b = new Budget();
		b.setCatego("TEST");
		b.setCompte("TEST_ACNT");
		b.setUserID(0);
		b.setCredit(new BigDecimal(Math.round(Math.random() * 100000)));

		return b;
	}

	public static Budget createDebit() {
		Budget b = new Budget();
		b.setCatego("TEST");
		b.setCompte("TEST_ACNT");
		b.setUserID(0);
		b.setDebit(new BigDecimal(Math.round(Math.random() * 100000)));

		return b;
	}

}
