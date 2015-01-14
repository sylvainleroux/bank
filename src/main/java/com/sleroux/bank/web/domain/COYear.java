package com.sleroux.bank.web.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.calc.MonthAdjusted;

@SuppressWarnings("serial")
public class COYear implements Serializable {

	private HashMap<Integer, COMonth>	months	= new LinkedHashMap<Integer, COMonth>();

	public COYear() {
		// Empty
	}

	public HashMap<Integer, COMonth> getMonths() {
		return months;
	}

	public void setMonths(HashMap<Integer, COMonth> _months) {
		months = _months;
	}

	public void addAdjustedMonth(MonthAdjusted _monthAdjusted) {

		int month = _monthAdjusted.getMonth();
		getMonth(month).setMonthAdjusted(_monthAdjusted);
	}

	private COMonth getMonth(int _month) {
		COMonth month = months.get(_month);
		if (month == null) {
			month = new COMonth();
			months.put(_month, month);
		}
		return month;
	}

	public void addMonthBudget(BugdetMonth _monthBudget) {
		getMonth(_monthBudget.getMonth()).setMonthBudget(_monthBudget);
	}

	public void addMonthBank(Month _month, int _i) {
		int month = _i;
		getMonth(month).setMonth(_month);
	}

}