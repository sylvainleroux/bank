package com.sleroux.bank.evo.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

public class Operation {

	private int			id;
	private String		compte;
	private Date		dateOperation;
	private Date		dateValeur;
	private String		libelle;
	private BigDecimal	montant;
	private String		catego;
	private int			year;
	private int			monthBank;
	private int			monthAdjusted;

	public int getId() {
		return id;
	}

	public void setId(int _id) {
		id = _id;
	}

	public String getCompte() {
		return compte;
	}

	public void setCompte(String _compte) {
		compte = _compte;
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

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal _montant) {
		montant = _montant;
	}

	public String getCatego() {
		return catego;
	}

	public void setCatego(String _catego) {
		catego = _catego;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int _year) {
		year = _year;
	}

	public int getMonthBank() {
		return monthBank;
	}

	public void setMonthBank(int _monthBank) {
		monthBank = _monthBank;
	}

	public int getMonthAdjusted() {
		return monthAdjusted;
	}

	public void setMonthAdjusted(int _monthAdjusted) {
		monthAdjusted = _monthAdjusted;
	}

	public String toString() {
		return compte + "|" + dateOperation + "|" + dateValeur + "|"  + libelle  + "|" + montant;
	}

}
