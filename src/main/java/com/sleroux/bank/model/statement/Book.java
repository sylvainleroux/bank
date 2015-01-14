package com.sleroux.bank.model.statement;

import java.util.ArrayList;
import java.util.List;

import com.sleroux.bank.util.OperationIterator;

public class Book {

	private List<Year>	years	= new ArrayList<Year>();

	public List<Year> getYears() {
		return years;
	}

	public void setYears(List<Year> _years) {
		years = _years;
	}

	public void iterateOperations(OperationIterator _iterator) {
		for (Year year : getYears()) {
			for (Operation operation : year.getOperations()) {
				_iterator.next(year, operation);
			}
		}
	}

	public int getFirstYear() {
		if (years.size() > 0) {
			return years.get(0).getYear();
		}
		return 0;
	}

	public Year getLastYear() {
		if (years.size() > 0) {
			return years.get(years.size() - 1);
		}
		return null;
	}

}
