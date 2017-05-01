package com.sleroux.bank.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.evo.document.BudgetDocument;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.budget.Changes;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

@Service
@Lazy
public class FileToDB extends BusinessServiceAbstract {

	@Autowired
	IBudgetDao	budgetDao;

	@Override
	@Transactional
	public void run() throws Exception {

		ConsoleAppHeader.printAppHeader("Read Budget");

		BudgetDocument document = new BudgetDocument(Config.getBudgetDocument());

		List<Integer> years = document.getYears();
		/*
		List<String> credits = document.getCredits();
		List<String> debits = document.getDebits();
		List<String> comptes = document.getComptesEpargne();
		*/

		List<Budget> budgets = new ArrayList<>();
		int index = 1;
		for (int year : years) {
			for (int month = 1; month <= 12; month++) {
				/*
				budgets.addAll(document.readOperations(year, month, index, credits, debits));
				for (String compte : comptes) {
					budgets.addAll(document.readComptesEpargnes(year, month, index, compte));
				}
				*/
				index++;
			}
		}

		budgetDao.backupAndTruncate();
		for (Budget b : budgets) {
			budgetDao.create(b);
		}

		System.out.println("-- New entries in Budget file : ");
		List<Changes> added = budgetDao.getAdded();
		for (Changes u : added) {
			System.out.println(u);
			budgetDao.saveChange(u);
		}
		if (added.size() == 0){
			System.out.println("No additions");
		}

		System.out.println("-- Updated entries in Budget file :");
		List<Changes> updated =budgetDao.getUpdated();
		for (Changes u : updated) {
			System.out.println(u);
			budgetDao.saveChange(u);
		}
		if (updated.size() == 0){
			System.out.println("No updates");
		}

		System.out.println("-- Deleted entries in Budget file : ");
		List<Changes> deleted = budgetDao.getDeleted();
		for (Changes u : deleted) {
			System.out.println(u);
			budgetDao.saveChange(u);
		}
		if (deleted.size() == 0){
			System.out.println("No deletions");
		}

	}


}
