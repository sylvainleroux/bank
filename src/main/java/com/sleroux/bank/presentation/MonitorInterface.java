package com.sleroux.bank.presentation;

import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.calc.MonthAdjusted;

public interface MonitorInterface {

	public abstract void print(MonthAdjusted _monthAdjusted, Month _month, BugdetMonth _budgetMonth, BudgetKeys _keys);

}