package com.sleroux.bank.dao;

import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.AccountBalance;
import com.sleroux.bank.model.Operation;

public interface IOperationDao extends IOperations<Operation> {

	int insertIgnore(Operation _o);

	List<Operation> findUncategorized(long _userID);

	List<String> getCategoriesDebit(long _userID);

	List<String> getCategoriesCredit(long _userID);

	List<String> getSuggestionsFor(Operation _o);

	List<AccountBalance> getSoldes(long _userID);

	List<AggregatedOperations> findAggregatedYearMonth(int _year, int _month, long _userID);

	List<Operation> findByCategoYearMonth(Integer year, Integer month, String catego, long _userID);

}
