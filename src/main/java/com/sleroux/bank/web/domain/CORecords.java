package com.sleroux.bank.web.domain;

import java.util.ArrayList;
import java.util.List;

import com.sleroux.bank.model.statement.Operation;

public class CORecords {

	private List<Operation>	operations	= new ArrayList<>();
	private List<String>	categories	= new ArrayList<>();

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> _operations) {
		operations = _operations;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> _categories) {
		categories = _categories;
	}

}
