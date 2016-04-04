package com.sleroux.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.evo.document.BudgetDocument;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

@Service
@Lazy
public class DBToFile extends BusinessServiceAbstract {

	@Autowired
	IBudgetDao	budgetDao;

	@Override
	@Transactional(readOnly = true)
	public void run() throws Exception {
		ConsoleAppHeader.printAppHeader("Write Budget");

		BudgetDocument document = new BudgetDocument(Config.getBudgetDocumentTemplate());

		List<String> credits = budgetDao.getCredits();
		List<String> debits = budgetDao.getDebits();
		List<Integer> years = budgetDao.getYears();

		// Headers
		document.writeCredits(credits);
		document.writeDebits(debits);

		List<String> comptes = budgetDao.getComptesEpargne();
		for (String compte : comptes) {
			if (compte == null || compte.equals("")){
				throw new Exception("Unknown account Exception");
			}
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
