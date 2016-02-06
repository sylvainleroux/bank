package com.sleroux.bank.model.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.util.formats.OperationFormater;

@SuppressWarnings("serial")
public class MonthAdjusted implements Serializable {

	private Year						year;
	private int							month;
	private HashMap<String, Category>	categories	= new LinkedHashMap<String, Category>();

	public MonthAdjusted() {
		// Empty
	}

	public BigDecimal getCheckSumSolde() {
		return checkSumSolde;
	}

	public void setCheckSumSolde(BigDecimal _checkSumSolde) {
		checkSumSolde = _checkSumSolde;
	}

	private BigDecimal	checkSumSolde;

	public HashMap<String, Category> getCategories() {
		return categories;
	}

	public void setCategories(HashMap<String, Category> _categories) {
		categories = _categories;
	}

	public void setYear(Year _year) {
		year = _year;
	}

	public void setMonth(int _month) {
		month = _month;
	}

	public MonthAdjusted(Year _year, int _month) {
		year = _year;
		month = _month;
	}

	public void addOperation(Operation _op, BudgetKeys _keyList) throws Exception {
		String catego = _op.getCatego();
		if (catego == null || catego.equals("")) {
			throw new Exception("Can't do reporting, not categorized entry. Did you ran Categorization ? Operation : "
					+ OperationFormater.toString(_op));
		}
		if (catego.trim().equals("#DISPATCHED"))
			return;
		if (!_keyList.getCredit().containsKey(catego) && !_keyList.getDebit().containsKey(catego)) {
			throw new Exception("Unknown key [" + catego + "]: " + OperationFormater.toString(_op));
		}
		if (_op.getMontant() == null) {
			throw new Exception("This operation has no value : " + OperationFormater.toString(_op));
		}
		// Check key consistency
		if (_op.getMontant().compareTo(BigDecimal.ZERO) > 0) {
			if (!_keyList.getCredit().containsKey(catego))
				throw new Exception("Catego is CREDIT type but value is negative: " + OperationFormater.toString(_op));
		} else {
			if (!_keyList.getDebit().containsKey(catego))
				throw new Exception("Catego is DEBIT type but value is positive : " + OperationFormater.toString(_op));
		}
		Category category = categories.get(catego);
		if (category == null) {
			category = new Category(catego);
			categories.put(catego, category);
		}
		category.append(_op);
	}

	public HashMap<String, Category> getMonthReportDetailled() {
		return categories;
	}

	public HashMap<String, BigDecimal> getMonthReport() {
		HashMap<String, BigDecimal> monthReport = new LinkedHashMap<String, BigDecimal>();
		for (Category c : categories.values()) {
			monthReport.put(c.getName(), c.getTotal());
		}
		return monthReport;
	}

	public Year getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public BigDecimal checkSumSolde(BigDecimal _soldeAsChecksum) {
		checkSumSolde = _soldeAsChecksum;
		for (BigDecimal value : getMonthReport().values()) {
			if (value != null) {
				checkSumSolde = checkSumSolde.add(value);
			}
		}
		return checkSumSolde;
	}

}
