package com.sleroux.bank.web.domain;

import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.calc.MonthAdjusted;

public class COBudget {

	private MonthAdjusted	monthAdjusted;
	private Month			month;
	private BugdetMonth		monthBudget;
	private BudgetKeys		keyList;

	public MonthAdjusted getMonthAdjusted() {
		return monthAdjusted;
	}

	public void setMonthAdjusted(MonthAdjusted _monthAdjusted) {
		monthAdjusted = _monthAdjusted;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month _month) {
		month = _month;
	}

	public BugdetMonth getMonthBudget() {
		return monthBudget;
	}

	public void setMonthBudget(BugdetMonth _monthBudget) {
		monthBudget = _monthBudget;
	}

	public BudgetKeys getKeyList() {
		return keyList;
	}

	public void setKeyList(BudgetKeys _keyList) {
		keyList = _keyList;
	}

}
