package com.sleroux.bank.model.calc;

import java.math.BigDecimal;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class BudgetCalcCompte extends TreeMap<Integer, BudgetCalcCategoList> {

	private BigDecimal	soldeBudget;
	private BigDecimal	solde;

	public void setBalances(BigDecimal _solde) {
		solde = _solde;

		soldeBudget = solde;

		this.forEach((index, categoList) -> {
			if (index == 0) {
				// Credits
				soldeBudget = soldeBudget.subtract(categoList.getOperations()).add(categoList.getBudgets());
			} else {
				soldeBudget = soldeBudget.add(categoList.getOperations()).subtract(categoList.getBudgets());
			}
		});
	}

	public BigDecimal getSolde() {
		return solde;
	}

	public BigDecimal getSoldeBudget() {
		return soldeBudget;
	}

}