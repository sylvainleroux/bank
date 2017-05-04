package com.sleroux.bank.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.model.AnalysisFact;
import com.sleroux.bank.model.Budget;

@Service
public class BudgetService {

	@Autowired
	IBudgetDao	budgetDao;

	@Transactional
	public void createUpdateDebit(AnalysisFact _a) {
		Budget b = budgetDao.findByYearMonthCatego(_a.getYear(), _a.getMonth(), _a.getCatego(), _a.getCompte());
		if (b != null) {
			b.setDebit(_a.getDebit_ops());
			budgetDao.update(b);
		}else{
			b = new Budget();
			b.setYear(_a.getYear());
			b.setMonth(_a.getMonth());
			b.setCatego(_a.getCatego());
			b.setDebit(_a.getDebit_ops());
			b.setCompte(_a.getCompte());
			budgetDao.create(b);
		}
	}

	@Transactional
	public void createUpdateCredit(AnalysisFact _a) {
		Budget b = budgetDao.findByYearMonthCatego(_a.getYear(), _a.getMonth(), _a.getCatego(), _a.getCompte());
		
		if (b != null) {
			b.setCredit(_a.getCredit_ops());
			budgetDao.update(b);
		}else{
			b = new Budget();
			b.setYear(_a.getYear());
			b.setMonth(_a.getMonth());
			b.setCatego(_a.getCatego());
			b.setCredit(_a.getCredit_ops());
			b.setCompte(_a.getCompte());
			budgetDao.create(b);
		}
	}

}
