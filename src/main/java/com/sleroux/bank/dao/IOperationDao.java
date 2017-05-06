package com.sleroux.bank.dao;

import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.model.balance.AccountBalance;
import com.sleroux.bank.model.operation.Operation;

public interface IOperationDao extends IOperations<Operation> {

	int insertIgnore(Operation _o);

	List<Operation> findUncategorized();

	List<String> getCategoriesDebit();

	List<String> getCategoriesCredit();

	List<String> getSuggestionsFor(Operation _o);

	List<AccountBalance> getSoldes();

	List<Operation> findByCategoYearMonth(Integer year, Integer month, String catego);

	List<Operation> findByYearMonth(int _year, int _month);

}
