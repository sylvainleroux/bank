package com.sleroux.bank.dao;

import java.math.BigDecimal;
import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.Budget;

public interface IBudgetDao extends IOperations<Budget> {

	void backupAndTruncate();

	Budget findByYearMonthCatego(int _year, int _month, String _catego, String _compte);

	List<AggregatedOperations> findBudgetForMonth(int _year, int _month);

	BigDecimal getEstimatedEndOfMonthBalance(int _year, int _month);

}
