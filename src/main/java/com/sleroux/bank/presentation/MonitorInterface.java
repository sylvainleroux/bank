package com.sleroux.bank.presentation;

import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BudgetMonth;
import com.sleroux.bank.model.calc.MonthAdjusted;

public interface MonitorInterface {

	public abstract void print(MonthAdjusted _monthAdjusted, BudgetMonth _budgetMonth, BudgetKeys _keys);

}