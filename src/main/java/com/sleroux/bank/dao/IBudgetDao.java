package com.sleroux.bank.dao;

import java.math.BigDecimal;
import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.budget.Changes;

public interface IBudgetDao extends IOperations<Budget> {

	List<String> getCredits(long _userID);

	List<String> getDebits(long _userID);

	List<Integer> getYears(long _userID);

	List<String> getComptesEpargne(long _userID);

	List<Budget> getMonthDebits(Integer _year, int _month, long _userID);

	List<Budget> getMonthCredits(Integer _year, int _month, long _userID);

	Budget getBudgetForCompte(String _compte, Integer _year, int _month, long _userID);

	void backupAndTruncate();

	List<Changes> getAdded(long _userID);   

	List<Changes> getUpdated(long _userID);

	List<Changes> getDeleted(long _userID);

	void saveChange(Changes _u, long _userID);

	Budget findByYearMonthCatego(int _year, int _month, String _catego, String _compte, long _userID);

	List<AggregatedOperations> findBudgetForMonth(int _year, int _month, long _userID);

	BigDecimal getEstimatedEndOfMonthBalance(int _year, int _month, long _userID);

}
