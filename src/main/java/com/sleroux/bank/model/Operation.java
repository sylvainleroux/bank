package com.sleroux.bank.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "operation")
public class Operation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int			id;
	private String		compte;

	@Column(name = "date_operation")
	private Date		dateOperation;

	@Column(name = "date_valeur")
	private Date		dateValeur;
	private String		libelle;
	private BigDecimal	montant;
	private String		catego;
	private Integer		year;

	@Column(name = "month_bank")
	private Integer		monthBank;

	@Column(name = "month_adjusted")
	private Integer		monthAdjusted;

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

	@Transient
	public String toString() {
		String sep = "|";
		return "[" + compte + sep + formatDate(dateOperation) + sep + formatDate(dateValeur) + sep + libelle + sep + montant + "]";

	}

	@Transient
	private String formatDate(Date d) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		if (d == null)
			return "null";
		return formatter.format(d);
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + compte.hashCode();
		hash = 31 * hash + dateOperation.hashCode();
		hash = 31 * hash + dateValeur.hashCode();
		hash = 31 * hash + libelle.hashCode();
		hash = 31 * hash + montant.hashCode();

		return hash;
	}

}
