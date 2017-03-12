package com.sleroux.bank.dao;

import java.math.BigDecimal;
import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.budget.Changes;

public interface IBudgetDao extends IOperations<Budget> {

	List<String> getCredits(int _userID);

	List<String> getDebits(int _userID);

	List<Integer> getYears(int _userID);

	List<String> getComptesEpargne(int _userID);

	List<Budget> getMonthDebits(Integer _year, int _month, int _userID);

	List<Budget> getMonthCredits(Integer _year, int _month, int _userID);

	Budget getBudgetForCompte(String _compte, Integer _year, int _month, int _userID);

	void backupAndTruncate();

	List<Changes> getAdded(int _userID);   

	List<Changes> getUpdated(int _userID);

	List<Changes> getDeleted(int _userID);

	void saveChange(Changes _u, int _userID);

	Budget findByYearMonthCatego(int _year, int _month, String _catego, String _compte, int _userID);

	List<AggregatedOperations> findBudgetForMonth(int _year, int _month, int _userID);

	BigDecimal getEstimatedEndOfMonthBalance(int _year, int _month, int _userID);

}
