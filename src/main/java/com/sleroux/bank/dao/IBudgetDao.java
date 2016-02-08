package com.sleroux.bank.dao;

import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.budget.Changes;

public interface IBudgetDao extends IOperations<Budget> {

	List<String> getCredits();

	List<String> getDebits();

	List<Integer> getYears();

	List<String> getComptesEpargne();

	List<Budget> getMonthDebits(Integer _year, int _month);

	List<Budget> getMonthCredits(Integer _year, int _month);

	Budget getBudgetForCompte(String _compte, Integer _year, int _month);

	void backupAndTruncate();

	List<Changes> getAdded();

	List<Changes> getUpdated();

	List<Changes> getDeleted();

	void saveChange(Changes _u);

	Budget findByYearMonthCatego(int _year, int _month, String _catego, String _compte);

}
