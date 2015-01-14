package com.sleroux.bank.model.budget;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class BugdetMonth implements Serializable {

	private int							year;
	private int							month;
	// Calc
	private BigDecimal					totalCredit	= new BigDecimal(0L);
	private BigDecimal					totalDebit	= new BigDecimal(0L);
	private BigDecimal					solde		= new BigDecimal(0L);
	//
	private static BudgetKeys			keyList;
	//
	private HashMap<String, BigDecimal>	credits		= new HashMap<String, BigDecimal>();
	private HashMap<String, BigDecimal>	debits		= new HashMap<String, BigDecimal>();
	//
	private int							columnIndex;

	public BugdetMonth() {
		// Empty
	}

	public void setSolde(BigDecimal _solde) {
		solde = _solde;
	}

	public HashMap<String, BigDecimal> getCredits() {
		return credits;
	}

	public void setCredits(HashMap<String, BigDecimal> _credits) {
		credits = _credits;
	}

	public HashMap<String, BigDecimal> getDebits() {
		return debits;
	}

	public void setDebits(HashMap<String, BigDecimal> _debits) {
		debits = _debits;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int _year) {
		year = _year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int _month) {
		month = _month;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int _columnIndex) {
		columnIndex = _columnIndex;
	}

	public static void setKeyList(BudgetKeys _keyList) {
		keyList = _keyList;
	}

	public static BudgetKeys getKeyList() {
		return keyList;
	}

	public BigDecimal getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(BigDecimal _totalCredit) {
		totalCredit = _totalCredit;
	}

	public BigDecimal getTotalDebit() {
		return totalDebit;
	}

	public void setTotalDebit(BigDecimal _totalDebit) {
		totalDebit = _totalDebit;
	}

	public BigDecimal getSolde() {
		return solde;
	}

	public void setFirstValue(BigDecimal _solde) {
		solde = _solde;
	}

	@JsonIgnore
	public BigDecimal getEndOfMonthSolde() {
		return solde.add(totalCredit).add(totalDebit);
	}

}
