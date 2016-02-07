package com.sleroux.bank.dao;

import java.util.Date;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.model.ExtractHistory;

public interface IExtractHistoryDao extends IOperations<ExtractHistory> {

	Date getLastExtractDate();
}
