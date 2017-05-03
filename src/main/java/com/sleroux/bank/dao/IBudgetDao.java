package com.sleroux.bank.dao;

import java.math.BigDecimal;
import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.budget.Changes;

public interface IBudgetDao extends IOperations<Budget> {

	List<String> getCredits(String _compte);

	List<String> getDebits(String _compte);

	List<Integer> getYears();

	List<Budget> getMonth(String _compte, Integer _year, int _month);



	void backupAndTruncate();

	List<Changes> getAdded();

	List<Changes> getUpdated();

	List<Changes> getDeleted();

	void saveChange(Changes _u);

	Budget findByYearMonthCatego(int _year, int _month, String _catego, String _compte);

	List<AggregatedOperations> findBudgetForMonth(int _year, int _month);

	BigDecimal getEstimatedEndOfMonthBalance(int _year, int _month);


}
