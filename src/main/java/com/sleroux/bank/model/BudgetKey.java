package com.sleroux.bank.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BudgetKey implements Serializable {
	protected int		year;
	protected int		month;
	protected String	compte;
	protected String	catego;

	public BudgetKey() {
		// Empty
	}

	public BudgetKey(int _year, int _month, String _compte, String _catego) {
		year = _year;
		month = _month;
		catego = _catego;
		compte = _compte;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catego == null) ? 0 : catego.hashCode());
		result = prime * result + ((compte == null) ? 0 : compte.hashCode());
		result = prime * result + month;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BudgetKey other = (BudgetKey) obj;
		if (catego == null) {
			if (other.catego != null)
				return false;
		} else if (!catego.equals(other.catego))
			return false;
		if (compte == null) {
			if (other.compte != null)
				return false;
		} else if (!compte.equals(other.compte))
			return false;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}



}
