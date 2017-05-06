package com.sleroux.bank.model.calc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.sleroux.bank.model.budget.Budget;
import com.sleroux.bank.model.operation.Operation;

@SuppressWarnings("serial")
public class BudgetCalc extends HashMap<String, BudgetCalcCompte> {

	public void setOperations(List<Operation> _operations) {
		for (Operation o : _operations) {
			BudgetCalcCompte compte = this.get(o.getCompte());
			if (compte == null) {
				compte = new BudgetCalcCompte();
				this.put(o.getCompte(), compte);
			}

			if (o.getCredit().compareTo(BigDecimal.ZERO) > 0) {
				CategoList list = compte.get(true);
				if (list == null) {
					list = new CategoList();
					compte.put(true, list);
				}

				BudgetCalcCatego c = list.get(o.getCatego());
				if (c == null) {
					c = new BudgetCalcCatego();
					list.put(o.getCatego(), c);
				}

				c.addOperationCredit(o);
			}

			if (o.getDebit().compareTo(BigDecimal.ZERO) > 0) {
				CategoList list = compte.get(false);
				if (list == null) {
					list = new CategoList();
					compte.put(false, list);
				}

				BudgetCalcCatego c = list.get(o.getCatego());
				if (c == null) {
					c = new BudgetCalcCatego();
					list.put(o.getCatego(), c);
				}

				c.addOperationDebit(o);
			}

		}
	}

	public void setBudget(List<Budget> _budget) {

		for (Budget b : _budget) {
			BudgetCalcCompte compte = this.get(b.getCompte());
			if (compte == null) {
				compte = new BudgetCalcCompte();
				this.put(b.getCompte(), compte);
			}
			if (b.getCredit().compareTo(BigDecimal.ZERO) > 0) {
				CategoList list = compte.get(true);
				if (list == null) {
					list = new CategoList();
					compte.put(true, list);
				}

				BudgetCalcCatego c = list.get(b.getCatego());
				if (c == null) {
					c = new BudgetCalcCatego();
					list.put(b.getCatego(), c);
				}

				c.addBudgetCredit(b);

			}

			if (b.getDebit().compareTo(BigDecimal.ZERO) > 0) {
				CategoList list = compte.get(false);
				if (list == null) {
					list = new CategoList();
					compte.put(false, list);
				}

				BudgetCalcCatego c = list.get(b.getCatego());
				if (c == null) {
					c = new BudgetCalcCatego();
					list.put(b.getCatego(), c);
				}

				c.addBudgetDebit(b);

			}

		}
	}

}
