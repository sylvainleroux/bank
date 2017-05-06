package com.sleroux.bank.model.balance;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "imported_balance")
public class AccountBalance implements Serializable {

	@Id
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
