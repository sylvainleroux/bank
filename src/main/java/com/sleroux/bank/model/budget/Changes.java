package com.sleroux.bank.model.budget;

import java.math.BigDecimal;

import com.sleroux.bank.model.Budget;

@SuppressWarnings("serial")
public class Changes extends Budget {

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

	public boolean isCredit() {
		
		if (oldCredit != null){
			if (oldCredit.compareTo(BigDecimal.ZERO) > 0){
				return true;
			}
		}
		
		if (credit != null){
			if (credit.compareTo(BigDecimal.ZERO) > 0){
				return true;
			}
		}
		
		return false;
	}

}
