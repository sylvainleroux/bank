package com.sleroux.bank.evo.model;

import java.math.BigDecimal;

public class Budget {

	protected int			id;
	protected int			year;
	protected int			month;
	protected String		catego;
	protected BigDecimal	debit	= BigDecimal.ZERO;
	protected BigDecimal	credit	= BigDecimal.ZERO;
	protected String		notes;
	protected String		compte;

	public int getId() {
		return id;
	}

	public void setId(int _id) {
		id = _id;
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

	public String getCatego() {
		return catego;
	}

	public void setCatego(String _catego) {
		catego = _catego;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal _debit) {
		debit = _debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal _credit) {
		credit = _credit;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String _notes) {
		notes = _notes;
	}

	public String getCompte() {
		return compte;
	}

	public void setCompte(String _compte) {
		compte = _compte;
	}

	@Override
	public String toString() {
		return "year:" + year + " month:" + month + " catego:" + catego + " debit:" + debit + " credit:" + credit + " compte:" + compte;
	}

}
