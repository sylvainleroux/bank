package com.sleroux.bank.service.extract;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sleroux.bank.model.extract.ExtractOperation;

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

	@Deprecated
	public static ExtractOperation createOperationCMB(String[] _nextLine, String _accountNumber) throws Exception {

		ExtractOperation operation = new ExtractOperation();
		operation.setDateOperation(parseDate(_nextLine[0]));
		operation.setDateValeur(parseDate(_nextLine[1]));
		operation.setLibelle(_nextLine[2]);
		DecimalFormat formatter = new DecimalFormat("#0,00");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);

		if (_nextLine[3] != null && !_nextLine[3].equals("")) {
			try {
				Number d = formatter.parse(_nextLine[3]);
				BigDecimal value = new BigDecimal(d.toString());
				operation.setMontant(value.negate());
			} catch (ParseException e) {
				throw new Exception("Unable to parse operation amount for debit [" + _nextLine[3] + "|" + _nextLine[4] + "]", e);
			}
		} else if (_nextLine[4] != null) {
			try {
				Number d = formatter.parse(_nextLine[4]);
				BigDecimal value = new BigDecimal(d.toString());
				operation.setMontant(value);
			} catch (ParseException e) {
				throw new Exception("Unable to parse operation amount", e);
			}
		}

		operation.setAccountID(_accountNumber);
		return operation;

	}
}
