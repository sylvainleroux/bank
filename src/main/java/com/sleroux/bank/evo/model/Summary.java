package com.sleroux.bank.evo.model;

import java.math.BigDecimal;

public class Summary {
	private BigDecimal	credit;
	private BigDecimal	debit;

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

}
