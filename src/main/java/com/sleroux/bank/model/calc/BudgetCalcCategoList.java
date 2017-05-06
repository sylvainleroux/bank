package com.sleroux.bank.model.calc;

import java.math.BigDecimal;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class BudgetCalcCategoList extends TreeMap<String, BudgetCalcCatego> {

	public BigDecimal getOperations() {
		return this.entrySet().stream().map(e -> e.getValue().getOperation()).reduce(BigDecimal.ZERO,
				(a, b) -> a.add(b));
	}

	public BigDecimal getBudgets() {
		return this.entrySet().stream().map(e -> e.getValue().getOperation().max(e.getValue().getBudget()))
				.reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}

}
