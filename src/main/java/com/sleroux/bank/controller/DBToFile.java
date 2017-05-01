package com.sleroux.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.dao.ICompteDao;
import com.sleroux.bank.evo.document.BudgetDocument;
import com.sleroux.bank.evo.document.BudgetDocument.BudgetDocumentCompte;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.Compte;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

@Service
@Lazy
public class DBToFile extends BusinessServiceAbstract {

	@Autowired
	IBudgetDao	budgetDao;

	@Autowired
	ICompteDao	compteDao;

	@Override
	@Transactional(readOnly = true)
	public void run() throws Exception {
		ConsoleAppHeader.printAppHeader("Write Budget");

		BudgetDocument document = new BudgetDocument(Config.getBudgetDocumentTemplate());

		List<Integer> years = budgetDao.getYears();
		List<Compte> comptes = compteDao.findAll();

		document.writeYears(years);
		
		for (Compte compte : comptes) {
			List<String> credits = budgetDao.getCredits(compte.getNom());
			List<String> debits = budgetDao.getDebits(compte.getNom());
			BudgetDocumentCompte budgetDocumentCompte = document.addCompte(compte.getNom(), credits, debits,
					compte.getType());

			for (int year : years) {
				for (int month = 0; month < 12; month++) {

					List<Budget> monthCredits = budgetDao.getMonthCredits(compte.getNom(), year, month + 1);
					List<Budget> monthDebits = budgetDao.getMonthDebits(compte.getNom(), year, month + 1);

					budgetDocumentCompte.addMonthData(year, month, monthCredits, monthDebits);
				}
			}
		}

		for (int year : years) {
			for (int month = 0; month < 12; month++) {
				document.summary(year, month);
			}
		}

		document.clearTemplate();

		document.close();

	}

}
