package com.sleroux.bank.web.domain;

import java.io.Serializable;

import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.calc.MonthAdjusted;

@SuppressWarnings("serial")
public class COMonth implements Serializable {

	private Month		month;
	private BugdetMonth		monthBudget;
	private MonthAdjusted	monthAdjusted;

	public COMonth() {
		// Empty
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

	public MonthAdjusted getMonthAdjusted() {
		return monthAdjusted;
	}

	public void setMonthAdjusted(MonthAdjusted _monthAdjusted) {
		monthAdjusted = _monthAdjusted;
	}

}
