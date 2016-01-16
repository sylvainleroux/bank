package com.sleroux.bank.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.BudgetDao;
import com.sleroux.bank.evo.document.BudgetDocument;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.budget.Changes;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

@Service
@Lazy
public class FileToDB extends BusinessServiceAbstract {

	@Autowired
	BudgetDao	budgetDao;

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

		budgetDao.backupAndReplace(budgets);

		System.out.println("-- New entries in Budget file : ");
		for (Changes u : budgetDao.getAdded()) {
			budgetDao.saveChange(u);
			System.out.println(u);
		}

		System.out.println("-- Updated entries in Budget file :");
		for (Changes u : budgetDao.getUpdated()) {
			budgetDao.saveChange(u);
			System.out.println(u);
		}

		System.out.println("-- Deleted entries in Budget file : ");
		for (Changes u : budgetDao.getDeleted()) {
			budgetDao.saveChange(u);
			System.out.println(u);
		}

	}

}
