package com.sleroux.bank.model.budget;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class BudgetKeys implements Serializable {

	private LinkedHashMap<String, Integer>	credit = new LinkedHashMap<>();
	private LinkedHashMap<String, Integer>	debit = new LinkedHashMap<>();

	public BudgetKeys() {
		// Empty
	}

	public LinkedHashMap<String, Integer> getCredit() {
		return credit;
	}

	public LinkedHashMap<String, Integer> getDebit() {
		return debit;
	}

	@Override
	public String toString() {
		String d = "DEBIT : ";
		String c = "CREDIT : ";
		for (Entry<String, Integer> e : credit.entrySet())
			c += e.getKey() + "(" + e.getValue().toString() + ") ";
		for (Entry<String, Integer> e : debit.entrySet())
			d += e.getKey() + "(" + e.getValue().toString() + ") ";

		return c + "\n" + d;
	}

}
