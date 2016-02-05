package com.sleroux.bank.dao;

import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.model.AccountBalance;
import com.sleroux.bank.model.CalcResult;
import com.sleroux.bank.model.Operation;

public interface IOperationDao extends IOperations<Operation> {

	void doBackup();

	void insertIgnore(Operation _o);

	List<Operation> findUncategorized();

	List<String> getCategoriesDebit();

	List<String> getCategoriesCredit();

	List<String> getSuggestionsFor(Operation _o);

	List<CalcResult> getCalcForMonth(int _year, int _month);

	List<AccountBalance> getSoldes();

}
