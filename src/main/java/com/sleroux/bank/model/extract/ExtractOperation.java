package com.sleroux.bank.model.extract;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtractOperation {

	private String		accountID;
	private Date		dateCompta;
	private Date		dateOperation;
	private String		libelle;
	private String		reference;
	private Date		dateValeur;
	private BigDecimal	montant;

	public ExtractOperation() {
		// Empty
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String _accountID) {
		accountID = _accountID;
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

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String _libelle) {
		libelle = _libelle;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String _reference) {
		reference = _reference;
	}

	public Date getDateValeur() {
		return dateValeur;
	}

	public void setDateValeur(Date _dateValeur) {
		dateValeur = _dateValeur;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal _montant) {
		montant = _montant;
	}

	@Override
	public String toString() {
		String sep = ";";
		return "[" + accountID + sep + formatDate(dateOperation) + sep + formatDate(dateValeur) + sep + libelle + sep + montant + "]";

	}

	private String formatDate(Date d) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		if (d == null)
			return "null";
		return formatter.format(d);
	}

	public String getIndex() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return accountID + "-" + format.format(dateOperation) + "-" + format.format(dateValeur) + "-" + libelle + "-" + montant.toString();
	}

}
