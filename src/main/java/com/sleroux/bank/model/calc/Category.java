package com.sleroux.bank.model.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sleroux.bank.model.statement.Operation;

@SuppressWarnings("serial")
public class Category implements Serializable {

	private String			name;
	private BigDecimal		total		= BigDecimal.ZERO;
	private List<Operation>	operations	= new ArrayList<Operation>();

	public Category() {
		// Empty
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> _operations) {
		operations = _operations;
	}

	public void setName(String _name) {
		name = _name;
	}

	public void setTotal(BigDecimal _total) {
		total = _total;
	}

	public Category(String _name) {
		name = _name;
	}

	public void append(Operation _op) {
		if (_op.getMontant() != null) {
			total = total.add(_op.getMontant());
			operations.add(_op);
		}
	}

	public BigDecimal getTotal() {
		return total;
	}

	public String getName() {
		return name;
	}

}
