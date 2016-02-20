package com.sleroux.bank.util.formats;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sleroux.bank.model.statement.Operation;

public class OperationFormater {

	public static String toString(Operation o) {
		String sep = ";";
		return "[" + o.getAccountID() + sep + formatDate(o.getDateCompta()) + sep + formatDate(o.getDateOperation()) + sep + o.getLibelle()
				+ sep + o.getReference() + sep + formatDate(o.getDateValeur()) + sep + o.getMontant() + sep + o.getCatego() + "]";
	}

	public static String toStringLight(Operation o) {
		return "Date Op.  : " + formatDate(o.getDateOperation()) + "    Date Compta.  : " + formatDate(o.getDateCompta())
				+ "\nLibelelle : " + o.getLibelle() + "\nReference : " + o.getReference() + "\nMontant   : " + o.getMontant();
	}

	private static String formatDate(Date d) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		if (d == null)
			return "null";
		return formatter.format(d);
	}

	public static String selector(Operation _operation, int _i, boolean _equals) {
		return String.format("%-3s %s %10.2f %-13s %s", _equals ? ">>>" : _i + ".", formatDate(_operation.getDateCompta()),
				_operation.getMontant(), _operation.getCatego(), _operation.getLibelle());
	}

	public static String toStringLight(com.sleroux.bank.model.Operation o) {

		BigDecimal montant = (o.getCredit().compareTo(BigDecimal.ZERO) > 0) ? o.getCredit() : o.getDebit().negate();

		return "Compte    : " + o.getCompte() + "    Date Op.  : " + formatDate(o.getDateOperation()) + "    Date Valeur.  : "
				+ formatDate(o.getDateValeur()) + "    Id : " + o.getId() + "\nLibelelle : " + o.getLibelle() + "\nMontant   : "
				+ montant;

	}
}
