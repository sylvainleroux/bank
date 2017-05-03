package com.sleroux.bank.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.domain.BudgetIndex;
import com.sleroux.bank.evo.document.BudgetDocument;
import com.sleroux.bank.evo.document.BudgetDocument.BudgetDocumentCompteReader;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.budget.Changes;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

@Service
@Lazy
public class FileToDB extends BusinessServiceAbstract {

	@Autowired
	IBudgetDao budgetDao;

	@Override
	@Transactional
	public void run() throws Exception {

		ConsoleAppHeader.printAppHeader("Read Budget");

		BudgetDocument document = new BudgetDocument(Config.getBudgetDocument());

		List<Integer> years = document.getYears();
		List<BudgetDocumentCompteReader> comptes = document.getComptes();

		List<Budget> budgets = new ArrayList<>();
		for (BudgetDocumentCompteReader cr : comptes) {
			for (int year : years) {
				for (int month = 1; month <= 12; month++) {
					budgets.addAll(cr.getBudget(year, month));
				}
			}
		}

		List<Budget> db = budgetDao.findAll();

		BudgetIndex index = new BudgetIndex(db);
		for (Budget b : budgets) {
			Budget dbMatch = index.find(b);
			if (dbMatch == null) {
				if (b.getCredit().equals(BigDecimal.ZERO) && b.getDebit().equals(BigDecimal.ZERO)) {
					continue;
				}
				System.out.println("new " + b);
				budgetDao.create(b);
			} else {
				
				boolean update = false;
				
				if (!b.getCredit().setScale(2).equals(dbMatch.getCredit().setScale(2))) {
					System.out.println("Credit updated : " + b + " | " + dbMatch);
					update = true;
				}

				if (!b.getDebit().setScale(2).equals(dbMatch.getDebit().setScale(2))) {
					System.out.println("Debit updated : " + b + " | " + dbMatch);
					update = true;
				}
				
				if (update){
					budgetDao.update(b);
				}
				
				
				if (b.getCredit().equals(BigDecimal.ZERO) && b.getDebit().equals(BigDecimal.ZERO)) {
					budgetDao.delete(b);
				}

			}
		}

	}

	public void avion() throws Exception {

		BudgetDocument document = new BudgetDocument(Config.getBudgetDocument());
		List<Integer> years = document.getYears();

		List<Budget> budgets = new ArrayList<>();
		int index = 1;
		for (int year : years) {
			for (int month = 1; month <= 12; month++) {
				/*
				 * budgets.addAll(document.readOperations(year, month, index,
				 * credits, debits)); for (String compte : comptes) {
				 * budgets.addAll(document.readComptesEpargnes(year, month,
				 * index, compte)); }
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
		if (added.size() == 0) {
			System.out.println("No additions");
		}

		System.out.println("-- Updated entries in Budget file :");
		List<Changes> updated = budgetDao.getUpdated();
		for (Changes u : updated) {
			System.out.println(u);
			budgetDao.saveChange(u);
		}
		if (updated.size() == 0) {
			System.out.println("No updates");
		}

		System.out.println("-- Deleted entries in Budget file : ");
		List<Changes> deleted = budgetDao.getDeleted();
		for (Changes u : deleted) {
			System.out.println(u);
			budgetDao.saveChange(u);
		}
		if (deleted.size() == 0) {
			System.out.println("No deletions");
		}

	}

}
