package com.sleroux.bank.persistence.dao.operation;

import java.math.BigDecimal;
import java.util.Date;

public class Operation {

	private String		compte;
	private Date		dateOperation;
	private Date		dateValeur;
	private String		libelle;
	private BigDecimal	montant;
	private String		catego;
	private int			year;
	private int			month;
	private int			month_adjusted;
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
	public int getMonth() {
		return month;
	}
	public void setMonth(int _month) {
		month = _month;
	}
	public int getMonth_adjusted() {
		return month_adjusted;
	}
	public void setMonth_adjusted(int _month_adjusted) {
		month_adjusted = _month_adjusted;
	}
	
	

}
