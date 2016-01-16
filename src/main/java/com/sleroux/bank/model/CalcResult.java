package com.sleroux.bank.model;

import java.math.BigDecimal;

public class CalcResult {

	private String		catego;
	private BigDecimal	ops;
	private BigDecimal	bud;
	private boolean		credit;

	public String getCatego() {
		return catego;
	}

	public void setCatego(String _catego) {
		catego = _catego;
	}

	public BigDecimal getOps() {
		return ops;
	}

	public void setOps(BigDecimal _ops) {
		ops = _ops;
	}

	public BigDecimal getBud() {
		return bud;
	}

	public void setBud(BigDecimal _bud) {
		bud = _bud;
	}

	public boolean isCredit() {
		return credit;
	}

	public void setCredit(boolean _credit) {
		credit = _credit;
	}

}
