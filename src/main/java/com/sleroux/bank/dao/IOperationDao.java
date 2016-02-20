package com.sleroux.bank.dao;

import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.AccountBalance;
import com.sleroux.bank.model.Operation;

public interface IOperationDao extends IOperations<Operation> {

	int insertIgnore(Operation _o);

	List<Operation> findUncategorized();

	List<String> getCategoriesDebit();

	List<String> getCategoriesCredit();

	List<String> getSuggestionsFor(Operation _o);

	List<AccountBalance> getSoldes();

	List<AggregatedOperations> findAggregatedYearMonth(int _year, int _month);

}
