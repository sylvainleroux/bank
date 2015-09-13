package com.sleroux.bank.model.budget;

import java.math.BigDecimal;

import com.sleroux.bank.evo.model.Budget;

public class Update extends Budget {

	private BigDecimal	oldDebit;
	private BigDecimal	oldCredit;

	public BigDecimal getOldDebit() {
		return oldDebit;
	}

	public void setOldDebit(BigDecimal _oldDebit) {
		oldDebit = _oldDebit;
	}

	public BigDecimal getOldCredit() {
		return oldCredit;
	}

	public void setOldCredit(BigDecimal _oldCredit) {
		oldCredit = _oldCredit;
	}

	@Override
	public String toString() {
		return super.toString() + " (Previously debit:" + oldDebit + " credit:" + oldCredit + ")";
	}

}
