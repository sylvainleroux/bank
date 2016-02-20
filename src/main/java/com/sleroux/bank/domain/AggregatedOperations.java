package com.sleroux.bank.domain;

import java.math.BigDecimal;

public class AggregatedOperations {

	private int			year;
	private int			month;
	private String		account;
	private String		catego;
	private BigDecimal	credit;
	private BigDecimal	debit;

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

	public String getAccount() {
		return account;
	}

	public void setAccount(String _account) {
		account = _account;
	}

	public String getCatego() {
		return catego;
	}

	public void setCatego(String _catego) {
		catego = _catego;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal _credit) {
		credit = _credit;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal _debit) {
		debit = _debit;
	}

	public boolean isCredit() {
		return credit.compareTo(BigDecimal.ZERO) > 0;
	}

}
