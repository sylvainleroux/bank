package com.sleroux.bank.model;

import java.math.BigDecimal;

public class AccountBalance {

	private String		compte;
	private BigDecimal	solde;

	public String getCompte() {
		return compte;
	}

	public void setCompte(String _compte) {
		compte = _compte;
	}

	public BigDecimal getSolde() {
		return solde;
	}

	public void setSolde(BigDecimal _solde) {
		solde = _solde;
	}

}
