package com.sleroux.bank.model.statement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("serial")
public class Year implements Serializable {

	public List<Operation> getOperations() {
		return operations;
	}

	public Year() {
		// Empty
	}

	private int				sheetIndex;
	private int				year;
	private List<Operation>	operations	= new ArrayList<Operation>();
	private BigDecimal		initialCredit;
	private int				lastMonthBank;
	private int				lastMonthAdjusted;
	private boolean			lastYearOfTheBook;

	public boolean addOperation(Operation _operation) {
		return operations.add(_operation);
	}

	public void setSheetIndex(int _sheetIndex) {
		sheetIndex = _sheetIndex;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int _year) {
		year = _year;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public BigDecimal getInitialCredit() {
		return initialCredit;
	}

	public void setInitialCredit(BigDecimal _initialCredit) {
		initialCredit = _initialCredit;
	}

	public int getLastMonthBank() {
		return lastMonthBank;
	}

	public void setLastMonthBank(int _lastMonthBank) {
		lastMonthBank = _lastMonthBank;
	}

	public int getLastMonthAdjusted() {
		return lastMonthAdjusted;
	}

	public void setLastMonthAdjusted(int _lastMonthAdjusted) {
		lastMonthAdjusted = _lastMonthAdjusted;
	}

	public void setLastYearOfTheBook(boolean _lastYear) {
		lastYearOfTheBook = _lastYear;
	}

	public boolean isLastYearOfTheBook() {
		return lastYearOfTheBook;
	}

	public Operation getLastOperationWithNonNullDate() {
		ListIterator<Operation> listIterator = operations.listIterator(operations.size());
		while (listIterator.hasPrevious()) {
			Operation operation = listIterator.previous();
			if (operation.getDateOperation() != null) {
				return operation;
			}
		}
		return null;
	}

}
