package com.sleroux.bank.util;

import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;

public interface OperationIterator {

	public void next(Year _year, Operation _operation);

}
