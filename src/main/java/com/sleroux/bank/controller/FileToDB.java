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
import com.sleroux.bank.model.budget.Budget;
import com.sleroux.bank.presenter.common.ConsoleAppHeader;
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
				if (b.getCredit().setScale(2).equals(BigDecimal.ZERO.setScale(2))
						&& b.getDebit().setScale(2).equals(BigDecimal.ZERO.setScale(2))) {
					continue;
				}
				System.out.println("new " + b);
				budgetDao.create(b);
			} else {

				boolean update = false;

				if (!b.getCredit().setScale(2).equals(dbMatch.getCredit().setScale(2))) {
					if (b.getCredit().compareTo(BigDecimal.ZERO) > 0) {
						System.out.println("Credit updated : " + b + " | " + dbMatch);
						update = true;
					}
				}

				if (!b.getDebit().setScale(2).equals(dbMatch.getDebit().setScale(2))) {
					if (b.getDebit().compareTo(BigDecimal.ZERO) > 0) {
						System.out.println("Debit updated : " + b + " | " + dbMatch);
						update = true;
					}
				}

				if (b.getCredit().setScale(2).equals(BigDecimal.ZERO.setScale(2))
						&& b.getDebit().setScale(2).equals(BigDecimal.ZERO.setScale(2))) {
					System.out.println("Debit and credit are zero, deleting line");
					budgetDao.delete(dbMatch);
				} else if (update) {
					budgetDao.update(b);
				}

			}
		}

	}

}
