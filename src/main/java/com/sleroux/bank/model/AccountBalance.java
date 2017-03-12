package com.sleroux.bank.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
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

	@Column(name = "user_id")
	private int			user_id;

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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int _user_id) {
		user_id = _user_id;
	}
	
	

}
