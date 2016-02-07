package com.sleroux.bank.model;

import java.math.BigDecimal;

public class AnalysisFact {

	private int			year;
	private int			month;
	private String		catego;

	private BigDecimal	credit_ops;
	private BigDecimal	debit_ops;

	private BigDecimal	credit_bud;
	private BigDecimal	debit_bud;

	private String		notes;
	private Boolean		flag;

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

	public BigDecimal getCredit_ops() {
		return credit_ops;
	}

	public void setCredit_ops(BigDecimal _credit_ops) {
		credit_ops = _credit_ops;
	}

	public BigDecimal getDebit_ops() {
		return debit_ops;
	}

	public void setDebit_ops(BigDecimal _debit_ops) {
		debit_ops = _debit_ops;
	}

	public BigDecimal getCredit_bud() {
		return credit_bud;
	}

	public void setCredit_bud(BigDecimal _credit_bud) {
		credit_bud = _credit_bud;
	}

	public BigDecimal getDebit_bud() {
		return debit_bud;
	}

	public void setDebit_bud(BigDecimal _debit_bud) {
		debit_bud = _debit_bud;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String _notes) {
		notes = _notes;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean _flag) {
		flag = _flag;
	}

}
