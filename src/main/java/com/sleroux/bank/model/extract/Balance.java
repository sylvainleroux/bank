package com.sleroux.bank.model.extract;

import java.math.BigDecimal;

public class Balance {

	private String		compte	= "";
	private BigDecimal	solde	= new BigDecimal("0.00");

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
