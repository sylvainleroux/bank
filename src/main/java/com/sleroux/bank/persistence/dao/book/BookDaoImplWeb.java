package com.sleroux.bank.persistence.dao.book;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.calc.MonthAdjusted;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.web.domain.COYear;

public class BookDaoImplWeb extends BookDaoImpl {

	private HashMap<Integer, COYear>	years	= new LinkedHashMap<Integer, COYear>();

	@Override
	public void saveBook(Book _book) {
		// Do nothing
	}

	@Override
	public void saveBookMonth(Year _year, Operation _op, Month _month) {
		int year = _year.getYear();
		COYear y = years.get(year);
		if (y == null) {
			y = new COYear();
			years.put(year, y);
		}
		y.addMonthBank(_month, _op.getMonth());
	}

	@Override
	public void saveBookMonthAdjusted(Year _year, Operation _op, MonthAdjusted _monthAdjusted) {
		int year = _year.getYear();
		COYear y = years.get(year);
		if (y == null) {
			y = new COYear();
			years.put(year, y);
		}
		y.addAdjustedMonth(_monthAdjusted);
	}

	@Override
	public void saveBudget(BugdetMonth _monthBudget) {
		int year = _monthBudget.getYear();
		COYear y = years.get(year);
		if (y == null) {
			y = new COYear();
			years.put(year, y);
		}
		y.addMonthBudget(_monthBudget);
	}

	public HashMap<Integer, COYear> getYears() {
		return years;
	}

	public void setYears(HashMap<Integer, COYear> _years) {
		years = _years;
	}

}
