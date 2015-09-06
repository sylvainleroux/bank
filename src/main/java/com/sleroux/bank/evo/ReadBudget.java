package com.sleroux.bank.evo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.BudgetDao;
import com.sleroux.bank.evo.dao.DatabaseConnection;
import com.sleroux.bank.evo.document.BudgetDocument;
import com.sleroux.bank.evo.model.Budget;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

public class ReadBudget extends BusinessServiceAbstract {

	@Override
	public void run() throws Exception {

		ConsoleAppHeader.printAppHeader("Read Budget");

		BudgetDocument document = new BudgetDocument(Config.getBudgetDocument());

		List<Integer> years = document.getYears();
		List<String> credits = document.getCredits();
		List<String> debits = document.getDebits();
		List<String> comptes = document.getComptesEpargne();

		List<Budget> budgets = new ArrayList<>();
		int index = 1;
		for (int year : years) {
			for (int month = 1; month <= 12; month++) {
				budgets.addAll(document.readOperations(year, month, index, credits, debits));
				for (String compte : comptes) {
					budgets.addAll(document.readComptesEpargnes(year, month, index, compte));
				}
				index++;
			}
		}

		Connection conn = DatabaseConnection.getConnection();
		BudgetDao budgetDao = new BudgetDao(conn);
		budgetDao.backupAndReplace(budgets);
		conn.close();

	}

}
