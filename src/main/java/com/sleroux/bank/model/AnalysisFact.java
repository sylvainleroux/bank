package com.sleroux.bank.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.sleroux.bank.domain.AlertType;

public class AnalysisFact {

	private Integer		year;
	private Integer		month;
	private String		catego;

	private BigDecimal	credit_ops;
	private BigDecimal	debit_ops;

	private BigDecimal	credit_bud;
	private BigDecimal	debit_bud;

	private String		notes;
	private BigInteger	flag;

	private AlertType	alertType	= AlertType.UNDEFINED;

	public AlertType getReason() {
		return alertType;
	}

	public void setReason(AlertType _type) {
		alertType = _type;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer _year) {
		year = _year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer _month) {
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

	public BigInteger getFlag() {
		return flag;
	}

	public void setFlag(BigInteger _flag) {
		flag = _flag;
	}

	public String toString() {

		return String.format("%d/%02d:%s DEBIT[%.2f|%.2f] CREDIT[%.2f|%.2f] %s", year, month, catego, debit_ops, debit_bud, credit_ops,
				credit_bud, alertType.toString());
	}

}
