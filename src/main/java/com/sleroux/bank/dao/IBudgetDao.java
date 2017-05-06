package com.sleroux.bank.dao;

import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.model.budget.Budget;

public interface IBudgetDao extends IOperations<Budget> {

	void backupAndTruncate();

	List<Budget> findByYearMonth(int _year, int _month);

	Budget findByYearMonthCatego(int _year, int _month, String _catego, String _compte);

}
