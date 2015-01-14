package com.sleroux.bank.model.fileimport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExtractDocument {

	private String					filename;
	private List<ExtractOperation>	operations	= new ArrayList<ExtractOperation>();
	private BigDecimal				debit		= new BigDecimal("0.00");
	private BigDecimal				credit		= new BigDecimal("0.00");

	public void addOperation(ExtractOperation o) {
		operations.add(o);
		BigDecimal value = o.getMontant();

		if (BigDecimal.ZERO.compareTo(value) > 0)
			credit = value.add(credit);
		else
			debit = value.add(debit);

	}

	public List<ExtractOperation> getOperations() {
		return operations;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String _filename) {
		filename = _filename;
	}

}
