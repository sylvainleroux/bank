package com.sleroux.bank.model.analysis;

import java.math.BigDecimal;

import com.sleroux.bank.domain.AlertType;

public class AnalysisFact {

	private Integer		year;
	private Integer		month;
	private String		catego;
	private String		compte;

	private BigDecimal	credit_ops;
	private BigDecimal	debit_ops;

	private BigDecimal	credit_bud;
	private BigDecimal	debit_bud;

	private Integer		flag;

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

	public String getCompte() {
		return compte;
	}

	public void setCompte(String _compte) {
		compte = _compte;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer _flag) {
		flag = _flag;
	}

	@Override
	public String toString() {
		return "AnalysisFact [year=" + year + ", month=" + month + ", catego=" + catego + ", compte=" + compte
				+ ", credit_ops=" + credit_ops + ", debit_ops=" + debit_ops + ", credit_bud=" + credit_bud
				+ ", debit_bud=" + debit_bud + ", flag=" + flag + ", alertType=" + alertType + "]";
	}

}
