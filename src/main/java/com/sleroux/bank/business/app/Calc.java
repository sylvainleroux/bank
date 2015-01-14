package com.sleroux.bank.business.app;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.MonthAdjusted;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.persistence.dao.book.BookDao;
import com.sleroux.bank.presentation.ConsoleMonthBudgetPresenter;
import com.sleroux.bank.presentation.MonitorInterface;

public class Calc extends BusinessServiceAbstract {

	private MonitorInterface	monitorInterface	= new ConsoleMonthBudgetPresenter();

	private BookDao				customBookDao;

	public void run() throws Exception {
		// Get a clean workbook
		Book book = getBookDao().getBook();
		getBookDao().saveBook(book);
		for (Year year : book.getYears()) {
			calcYear(year);
		}
	}

	protected void calcYear(Year year) throws Exception {
		BudgetKeys keys = getKeys();
		int currentMonth = 0;
		int currentMonthAdjusted = 0;
		BugdetMonth monthBudget = null;
		MonthAdjusted monthAdjusted = null;
		Month month = new Month(year.getInitialCredit());
		BigDecimal soldeAsChecksum = year.getInitialCredit();
		for (Operation op : year.getOperations()) {
			// Manage month
			if (op.getMonth() > currentMonth) {
				printMonthReport(year, op, month);
				month.newMonth();
				currentMonth = op.getMonth();
			}
			// Manage monthAdjusted
			if (op.getMonthAdjusted() > currentMonthAdjusted) {
				if (monthAdjusted != null) {
					soldeAsChecksum = monthAdjusted.checkSumSolde(soldeAsChecksum);
					printAdjustedMonthReport(year, op, monthAdjusted);
					// Update timeline
					updateBudget(monthAdjusted, monthBudget);
					updateTimeline(monthBudget);
				}
				monthAdjusted = new MonthAdjusted(year, op.getMonthAdjusted());
				monthBudget = getMonthBudget(year, op.getMonthAdjusted());
				monthBudget.setFirstValue(soldeAsChecksum);
				currentMonthAdjusted = op.getMonthAdjusted();
			}
			month.addOperation(op);
			monthAdjusted.addOperation(op, keys);
			if (isLastOperationOfTheYear(year, op)) {
				soldeAsChecksum = monthAdjusted.checkSumSolde(soldeAsChecksum);
				printMonthReport(year, op, month);
				printAdjustedMonthReport(year, op, monthAdjusted);
				if (year.isLastYearOfTheBook()) {
					monitorInterface.print(monthAdjusted, month, monthBudget, keys);
				} else {
					updateBudget(monthAdjusted, monthBudget);
					updateTimeline(monthBudget);
				}
			}
		}
	}

	private boolean isLastOperationOfTheYear(Year _year, Operation _op) {
		return _year.getOperations().indexOf(_op) == (_year.getOperations().size() - 1);
	}

	private BugdetMonth getMonthBudget(Year _year, int _month) {
		return getBookDao().getBudgetByYearMonth(_year.getYear(), _month);
	}

	private void printAdjustedMonthReport(Year _year, Operation _op, MonthAdjusted _monthAdjusted) {
		getBookDao().saveBookMonthAdjusted(_year, _op, _monthAdjusted);
	}

	private void printMonthReport(Year _year, Operation _op, Month _month) {
		getBookDao().saveBookMonth(_year, _op, _month);
	}

	private BudgetKeys getKeys() {
		return getBookDao().getBudgetKeys();
	}

	private void updateTimeline(BugdetMonth _budget) throws Exception {
		getBookDao().saveBudget(_budget);
	}

	@Override
	public BookDao getBookDao() {
		if (customBookDao != null) {
			return customBookDao;
		}
		return super.getBookDao();
	}

	public BookDao getCustomBookDao() {
		return customBookDao;
	}

	public void setCustomBookDao(BookDao _customBookDao) {
		customBookDao = _customBookDao;
	}

	public void setMonitorInterface(MonitorInterface _monitorInterface) {
		monitorInterface = _monitorInterface;
	}

	public static void updateBudget(MonthAdjusted _monthAdjusted, BugdetMonth _monthBudget) throws Exception {
		_monthBudget.getCredits().clear();
		_monthBudget.getDebits().clear();
		BigDecimal totalDebit = new BigDecimal(0);
		BigDecimal totalCredit = new BigDecimal(0);
		BigDecimal totalByKeyDebit = new BigDecimal(0);
		BigDecimal totalByKeyCredit = new BigDecimal(0);
		HashMap<String, BigDecimal> monthReport = _monthAdjusted.getMonthReport();
		Set<String> debitKeys = BugdetMonth.getKeyList().getDebit().keySet();
		Set<String> creditKeys = BugdetMonth.getKeyList().getCredit().keySet();

		for (String key : monthReport.keySet()) {
			BigDecimal value = monthReport.get(key);
			if (BigDecimal.ZERO.compareTo(value) > 0) {
				totalDebit = totalDebit.add(value);
				if (debitKeys.contains(key)) {
					_monthBudget.getDebits().put(key, monthReport.get(key));
					totalByKeyDebit = totalByKeyDebit.add(value);
				}
			} else {
				totalCredit = totalCredit.add(value);
				if (creditKeys.contains(key)) {
					_monthBudget.getCredits().put(key, value);
					totalByKeyCredit = totalByKeyCredit.add(value);
				}
			}
		}

		// Check
		if (!totalDebit.equals(totalByKeyDebit) || !totalCredit.equals(totalByKeyCredit)) {
			throw new Exception("Key control failed");
		}
		_monthBudget.setTotalCredit(totalCredit);
		_monthBudget.setTotalDebit(totalDebit);
	}
}
