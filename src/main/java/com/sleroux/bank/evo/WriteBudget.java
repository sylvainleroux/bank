package com.sleroux.bank.evo;

import java.sql.Connection;
import java.util.List;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.BudgetDao;
import com.sleroux.bank.evo.document.BudgetDocument;
import com.sleroux.bank.evo.model.Budget;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

public class WriteBudget extends BusinessServiceAbstract {

	@Override
	public void run() throws Exception {
		ConsoleAppHeader.printAppHeader("Write Budget");

		BudgetDocument document = new BudgetDocument(Config.getBudgetDocumentTemplate());

		Connection conn = DatabaseConnection.getConnection();
		BudgetDao budgetDao = new BudgetDao(conn);
		List<String> credits = budgetDao.getCredits();
		List<String> debits = budgetDao.getDebits();
		List<Integer> years = budgetDao.getYears();

		// Headers
		document.writeCredits(credits);
		document.writeDebits(debits);

		List<String> comptes = budgetDao.getComptesEpargne();
		for (String compte : comptes) {
			document.addCompteEpargne(compte);
		}

		document.writeYears(years);

		int index = 1;
		for (Integer year : years) {
			for (int month = 0; month < 12; month++) {
				List<Budget> monthCredits = budgetDao.getMonthCredits(year, month + 1);
				List<Budget> monthDebits = budgetDao.getMonthDebits(year, month + 1);
				document.writeMonth(index, monthCredits, monthDebits);

				for (String compte : comptes) {
					Budget summary = budgetDao.getBudgetForCompte(compte, year, month + 1);
					document.writeSummary(compte, index, summary);
				}
				document.makeSummary(index, comptes);
				index++;
			}
		}
		document.close();

	}

}
