package com.sleroux.bank.model.extract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sleroux.bank.model.operation.Operation;

public class ExtractDocument {

	private String			filename;
	private List<Operation>	operations	= new ArrayList<Operation>();
	private BigDecimal		debit		= new BigDecimal("0.00");
	private BigDecimal		credit		= new BigDecimal("0.00");

	public void addOperation(Operation o) {
		operations.add(o);
		credit = credit.add(o.getCredit());
		debit = debit.add(o.getDebit());
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String _filename) {
		filename = _filename;
	}

}
