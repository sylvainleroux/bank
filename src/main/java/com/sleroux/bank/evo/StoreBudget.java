package com.sleroux.bank.evo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.BudgetDao;
import com.sleroux.bank.evo.model.Budget;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.presentation.ConsoleAppHeader;

public class StoreBudget extends BusinessServiceAbstract {

	@Override
	public void run() throws Exception {

		ConsoleAppHeader.printAppHeader("STORE BUDGET IN DB");

		Book book = getBookDao().getBook();
		
		List<Budget> list = new ArrayList<>();

		for (int year = book.getFirstYear(); year <= book.getLastYear().getYear() + 2; year++) {
			for (int i = 1; i <= 12; i++) {
				BugdetMonth budget = getBookDao().getBudgetByYearMonth(year, i);
				for (String key : budget.getCredits().keySet()) {
					BigDecimal value = budget.getCredits().get(key);
					if (value != null) {
						System.out.println(year + " " + i + " " + key + " " + value);

						Budget b = new Budget();
						b.setYear(year);
						b.setMonth(i);
						b.setCatego(key);
						b.setCredit(value);

						list.add(b);
					}
				}
				
				for (String key : budget.getDebits().keySet()) {
					BigDecimal value = budget.getDebits().get(key);
					if (value != null) {
						System.out.println(year + " " + i + " " + key + " " + value);
						Budget b = new Budget();
						b.setYear(year);
						b.setMonth(i);
						b.setCatego(key);
						b.setDebit(value);
						list.add(b);
					}
				}
			}
		}
		System.out.println(list.size());
		
		Connection conn = DatabaseConnection.getConnection();
		BudgetDao budgetDao = new BudgetDao(conn);
		budgetDao.createAll(list);
		conn.close();
	}
}
