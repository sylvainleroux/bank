package com.sleroux.bank.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.dao.ICompteDao;
import com.sleroux.bank.domain.BudgetIndex;
import com.sleroux.bank.evo.document.BudgetDocument;
import com.sleroux.bank.evo.document.BudgetDocument.BudgetDocumentCompte;
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

		Date start = new Date();

		BudgetDocument document = new BudgetDocument(Config.getBudgetDocumentTemplate());

		BudgetIndex index = new BudgetIndex(budgetDao.findAll());
		index.firstYear(2017);

		List<Compte> comptes = compteDao.findAll();
		document.writeYears(index.getYears());

		for (Compte compte : comptes) {

			List<String> credits;
			List<String> debits;

			credits = index.getCredits(compte.getNom());
			debits = index.getDebits(compte.getNom());

			BudgetDocumentCompte budgetDocumentCompte = document.addCompte(compte.getNom(), credits, debits,
					compte.getType());

			for (int year : index.getYears()) {
				for (int month = 0; month < 12; month++) {
					budgetDocumentCompte.addMonthData(year, month, index.find(year, month + 1, compte.getNom()));
				}
			}
		}

		for (int year : index.getYears()) {
			for (int month = 0; month < 12; month++) {
				document.summary(year, month);
			}
		}

		document.clearTemplate();

		document.close();

		long nanos = new Date().getTime() - start.getTime();
		System.out.println("Completed in " + nanos + " ms");

	}

}
