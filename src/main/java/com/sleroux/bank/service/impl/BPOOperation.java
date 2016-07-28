package com.sleroux.bank.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BPOOperation {
	
	private BigDecimal credit;
	private BigDecimal debit;
	@JsonProperty("date_compta")
	private Date dateCompta;
	
	@JsonProperty("date_ope")
	private Date dateOperation;
	
	@JsonProperty("date_val")
	private Date dateValeur;
	
	private String libelle;
	
	private String ref;

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

	public Date getDateCompta() {
		return dateCompta;
	}

	public void setDateCompta(Date _dateCompta) {
		dateCompta = _dateCompta;
	}

	public Date getDateOperation() {
		return dateOperation;
	}

	public void setDateOperation(Date _dateOperation) {
		dateOperation = _dateOperation;
	}

	public Date getDateValeur() {
		return dateValeur;
	}

	public void setDateValeur(Date _dateValeur) {
		dateValeur = _dateValeur;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String _libelle) {
		libelle = _libelle;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String _ref) {
		ref = _ref;
	}
	
	

}
