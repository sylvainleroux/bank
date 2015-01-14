package com.sleroux.bank.model.calc;

import java.io.Serializable;
import java.math.BigDecimal;

import com.sleroux.bank.model.statement.Operation;

@SuppressWarnings("serial")
public class Month implements Serializable {

	private BigDecimal	solde	= BigDecimal.ZERO;
	private BigDecimal	debit	= BigDecimal.ZERO;
	private BigDecimal	credit	= BigDecimal.ZERO;

	public Month() {
		// Empty
	}

	public Month(BigDecimal _initialCredit) {
		solde = _initialCredit;
	}

	public BigDecimal getSolde() {
		return solde;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void addOperation(Operation op) {
		if (op == null)
			return;
		BigDecimal value = op.getMontant();
		if (value == null)
			return;
		solde = value.add(solde);
		if (BigDecimal.ZERO.compareTo(value) < 0) {
			credit = value.add(credit);
		} else {
			debit = value.add(debit);
		}

	}

	public void newMonth() {
		debit = BigDecimal.ZERO;
		credit = BigDecimal.ZERO;
	}

	public void init(BigDecimal _soldeInit) {
		solde = _soldeInit;
	}

}
