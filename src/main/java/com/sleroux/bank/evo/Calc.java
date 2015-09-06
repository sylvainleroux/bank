package com.sleroux.bank.evo;

import java.util.Calendar;
import java.util.List;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.CalcDao;
import com.sleroux.bank.evo.dao.DatabaseConnection;
import com.sleroux.bank.evo.model.CalcResult;
import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.Category;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.calc.MonthAdjusted;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.presentation.ConsoleMonthBudgetPresenter;
import com.sleroux.bank.presentation.MonitorInterface;

public class Calc extends BusinessServiceAbstract {

	private CalcDao				calcDao;

	private MonitorInterface	monitorInterface	= new ConsoleMonthBudgetPresenter();

	@Override
	public void run() throws Exception {

		calcDao = new CalcDao(DatabaseConnection.getConnection());

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int currentMonth = c.get(Calendar.MONTH) ;

		List<CalcResult> calc = calcDao.getCalcForMonth(year, currentMonth);

		MonthAdjusted monthAdjusted = createMonthAdjusted(calc, year, currentMonth);

		Month month = new Month();
		BugdetMonth budgetMonth = createBudgetMonth(calc);
		BudgetKeys keys = createKeys(calc);

		monitorInterface.print(monthAdjusted, month, budgetMonth, keys);

	}

	private MonthAdjusted createMonthAdjusted(List<CalcResult> _calc, int _year, int _currentMonth) {
		MonthAdjusted ma = new MonthAdjusted();
		ma.setMonth(_currentMonth);
		ma.setYear(new Year());
		ma.getYear().setYear(_year);

		for (CalcResult cr : _calc) {
			Category cat = new Category();
			cat.setName(cr.getCatego());
			cat.setTotal(cr.getOps());
			ma.getCategories().put(cr.getCatego(), cat);
		}

		return ma;
	}

	private BugdetMonth createBudgetMonth(List<CalcResult> _calc) {
		BugdetMonth bm = new BugdetMonth();
		for (CalcResult cr : _calc) {
			if (cr.isCredit()) {
				bm.getCredits().put(cr.getCatego(), cr.getBud());
				bm.setTotalCredit(bm.getTotalCredit().add(cr.getBud()));
			} else {
				bm.getDebits().put(cr.getCatego(), cr.getBud());
				bm.setTotalDebit(bm.getTotalDebit().add(cr.getBud()));
			}
		}
		return bm;
	}

	private BudgetKeys createKeys(List<CalcResult> _calc) {
		BudgetKeys budgetKeys = new BudgetKeys();
		for (CalcResult cr : _calc) {
			if (cr.isCredit()) {
				budgetKeys.getCredit().put(cr.getCatego(), 0);
			} else {
				budgetKeys.getDebit().put(cr.getCatego(), 0);
			}
		}
		return budgetKeys;
	}

}
