package com.sleroux.bank.persistence.dao.book;

import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.calc.MonthAdjusted;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;

public interface BookDao {

	Book getBook();

	void saveBook(Book _book);

	void saveBookMonthAdjusted(Year _year, Operation _op, MonthAdjusted _monthAdjusted);

	void saveBookMonth(Year _year, Operation _op, Month _month);

	public BudgetKeys getBudgetKeys();

	BugdetMonth getBudgetByYearMonth(int _year, int _month);

	void saveBudget(BugdetMonth _budget);

}
