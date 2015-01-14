package com.sleroux.bank.persistence.storage.extract;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sleroux.bank.model.fileimport.ExtractOperation;

public class OperationBuilder {

	private final static DateFormat	formatter	= new SimpleDateFormat("dd/MM/yy");

	public static ExtractOperation createOperation(String[] s) throws Exception {
		ExtractOperation operation = new ExtractOperation();
		operation.setAccountID(s[0]);
		operation.setDateCompta(parseDate(s[1]));
		operation.setDateOperation(parseDate(s[2]));
		operation.setLibelle(s[3]);
		operation.setReference(s[4]);
		operation.setDateValeur(parseDate(s[5]));
		DecimalFormat formatter = new DecimalFormat("#0,00");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
		try {
			Number d = formatter.parse(s[6]);
			BigDecimal value = new BigDecimal(d.toString());
			operation.setMontant(value);
		} catch (ParseException e) {
			throw new Exception("Unable to parse operation amount", e);
		}
		return operation;
	}

	public static Date parseDate(String string) throws Exception {
		try {
			return (Date) formatter.parse(string);
		} catch (ParseException e) {
			throw new Exception("Unable to parse date");
		}
	}
}
