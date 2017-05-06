package com.sleroux.bank.model.calc;

import java.math.BigDecimal;

import com.sleroux.bank.model.budget.Budget;
import com.sleroux.bank.model.operation.Operation;

public class BudgetCalcCatego {

	BigDecimal	operation	= BigDecimal.ZERO.setScale(2);
	BigDecimal	budget		= BigDecimal.ZERO.setScale(2);

	public void addOperationCredit(Operation _o) {
		operation = operation.add(_o.getCredit());
	}

	public void addOperationDebit(Operation _o) {
		operation = operation.add(_o.getDebit());
	}

	public void addBudgetCredit(Budget _b) {
		budget = budget.add(_b.getCredit());
	}

	public void addBudgetDebit(Budget _b) {
		budget = budget.add(_b.getDebit());
	}

	@Override
	public String toString() {
		return "Catego [operation=" + operation + ", budget=" + budget + "]";
	}

	public BigDecimal getOperation() {
		return operation;
	}

	public BigDecimal getBudget() {
		return budget;
	}

}