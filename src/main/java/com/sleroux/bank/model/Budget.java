package com.sleroux.bank.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@IdClass(BudgetKey.class)
@Table(name = "budget")
public class Budget implements Serializable {

	protected BigDecimal	debit	= BigDecimal.ZERO;
	protected BigDecimal	credit	= BigDecimal.ZERO;

	@Id
	int						year;

	@Id
	int						month;

	@Id
	String					catego;

	@Id
	String					compte;

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal _debit) {
		debit = _debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal _credit) {
		credit = _credit;
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

	public String getCatego() {
		return catego;
	}

	public void setCatego(String _catego) {
		catego = _catego;
	}

	public String getCompte() {
		return compte;
	}

	public void setCompte(String _compte) {
		compte = _compte;
	}

	@Override
	public String toString() {
		return "Budget [debit=" + debit + ", credit=" + credit + ", year=" + year + ", month=" + month + ", catego="
				+ catego + ", compte=" + compte + "]";
	}
	
	

}
