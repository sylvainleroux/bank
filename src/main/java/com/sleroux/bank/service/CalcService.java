package com.sleroux.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.AccountBalance;
import com.sleroux.bank.model.CalcResult;
import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BudgetMonth;
import com.sleroux.bank.model.calc.Category;
import com.sleroux.bank.model.calc.MonthAdjusted;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.presentation.ConsoleMonthBudgetPresenter;
import com.sleroux.bank.presentation.MonitorInterface;

@Service
public class CalcService {

	@Autowired
	IOperationDao						operationDao;

	@Autowired
	IBudgetDao							budgetDao;

	private MonitorInterface			monitorInterface	= new ConsoleMonthBudgetPresenter();

	private final static List<String>	ACCOUNTS			= Arrays.asList("COURANT", "CMB", "BPO");

	public void run() throws Exception {

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int currentMonth = c.get(Calendar.MONTH) + 1;

		List<AggregatedOperations> operations = operationDao.findAggregatedYearMonth(year, currentMonth);
		List<AggregatedOperations> budget = budgetDao.findBudgetForMonth(year, currentMonth);

		List<CalcResult> calc = buildReport(operations, budget);

		MonthAdjusted monthAdjusted = createMonthAdjusted(calc, year, currentMonth);

		// Calculate real balance
		List<AccountBalance> balances = operationDao.getSoldes();
		BigDecimal bal = new BigDecimal(0);
		for (AccountBalance ab : balances) {
			if (ACCOUNTS.contains(ab.getCompte())) {
				bal = bal.add(ab.getSolde());
			}
		}
		monthAdjusted.setBalance(bal);

		BudgetMonth budgetMonth = createBudgetMonth(calc);
		budgetMonth.setEstimatedEndOfMonthBalance(budgetDao.getEstimatedEndOfMonthBalance(year, currentMonth));

		BudgetKeys keys = createKeys(calc);

		monitorInterface.print(monthAdjusted, budgetMonth, keys);

	}

	private List<CalcResult> buildReport(List<AggregatedOperations> _operations, List<AggregatedOperations> _budget)
			throws Exception {
		List<CalcResult> r = new ArrayList<>();

		for (AggregatedOperations o : _operations) {

			if (!ACCOUNTS.contains(o.getAccount())) {
				continue;
			}

			CalcResult existing = null;
			for (CalcResult calcResult : r) {
				if (calcResult.getCatego().equals(o.getCatego())) {
					existing = calcResult;
					break;
				}
			}

			if (existing != null) {
				existing.setOps(existing.getOps().add(o.getCredit().add(o.getDebit())));
				if (o.isCredit() != existing.isCredit()) {
					throw new Exception("Can't merge credit and debit operation");
				}

				existing.setCredit(o.isCredit());
			} else {

				CalcResult cr = new CalcResult();
				cr.setOps(o.getCredit().add(o.getDebit()));
				cr.setCredit(o.isCredit());
				cr.setCatego(o.getCatego());
				cr.setBud(new BigDecimal("0.00"));
				r.add(cr);
			}
		}

		for (AggregatedOperations o : _budget) {
			if (!ACCOUNTS.contains(o.getAccount())) {
				continue;
			}

			CalcResult cr = null;
			for (CalcResult existing : r) {
				if (existing.getCatego().equals(o.getCatego())) {
					cr = existing;
					break;
				}
			}
			if (cr == null) {
				cr = new CalcResult();
				cr.setCatego(o.getCatego());
				r.add(cr);
			}
			cr.setBud(o.getCredit().add(o.getDebit()));
			cr.setCredit(o.isCredit());

		}

		Collections.sort(r, new Comparator<CalcResult>() {

			@Override
			public int compare(CalcResult _o1, CalcResult _o2) {
				return _o1.getCatego().compareTo(_o2.getCatego());
			}
		});

		return r;
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

	private BudgetMonth createBudgetMonth(List<CalcResult> _calc) {
		BudgetMonth bm = new BudgetMonth();
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
