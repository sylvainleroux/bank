package com.sleroux.bank.util.formats;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OperationFormater {

	public static String toString(com.sleroux.bank.model.operation.Operation o) {
		String sep = ";";
		return "[" + o.getCompte() + sep + formatDate(o.getDateOperation()) + sep + formatDate(o.getDateValeur()) + sep
				+ o.getLibelle() + sep + o.getDebit() + sep + o.getCredit() + sep + o.getCatego() + "]";

	}

	private static String formatDate(Date d) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		if (d == null)
			return "null";
		return formatter.format(d);
	}

	public static String toStringLight(com.sleroux.bank.model.operation.Operation o) {

		BigDecimal montant = (o.getCredit().compareTo(BigDecimal.ZERO) > 0) ? o.getCredit() : o.getDebit().negate();

		return "Compte    : " + o.getCompte() + "    Date Op.  : " + formatDate(o.getDateOperation())
				+ "    Date Valeur.  : " + formatDate(o.getDateValeur()) + "    Id : " + o.getId() + "\nLibelelle : "
				+ o.getLibelle() + "\nMontant   : " + montant;

	}

}
