package com.sleroux.bank.model.statement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

@SuppressWarnings("serial")
public class Operation implements Serializable {

	public Operation() {
		// Empty
	}

	private String		accountID;
	private Date		dateCompta;
	private Date		dateOperation;
	private String		libelle;
	private String		reference;
	private Date		dateValeur;
	private BigDecimal	montant;

	private int			line;
	private String		catego;
	private int			month;
	private int			monthAdjusted;

	public String getCatego() {
		return catego;
	}

	public void setCatego(String _catego) {
		catego = _catego;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int _monthBank) {
		month = _monthBank;
	}

	public int getMonthAdjusted() {
		return monthAdjusted;
	}

	public void setMonthAdjusted(int _monthAdjusted) {
		monthAdjusted = _monthAdjusted;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int _line) {
		line = _line;
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

	public String getHash() {
		return DigestUtils.md5Hex(accountID + "|" + dateCompta + "|" + dateOperation + "|" + dateValeur + "|" + libelle + "|" + reference
				+ "|" + montant);
	}

}
