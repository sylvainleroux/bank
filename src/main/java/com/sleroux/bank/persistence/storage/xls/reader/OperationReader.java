package com.sleroux.bank.persistence.storage.xls.reader;

import org.apache.poi.ss.usermodel.Row;

import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.persistence.storage.xls.BookStorage;
import com.sleroux.bank.util.Transformer;

public class OperationReader extends Transformer {

	private int	currentMonth	= 0;
	private int	currentMonthAdjusted	= 0;

	public Operation transform(Row _row, int _line) {
		Operation o = new Operation();
		o.setLine(_line);
		o.setAccountID(str(_row, BookStorage.REPORT_COL_COMPTE));
		o.setDateCompta(date(_row, BookStorage.REPORT_COL_DATE_COMPTA));
		o.setDateOperation(date(_row, BookStorage.REPORT_COL_DATE_OPERATION));
		o.setLibelle(str(_row, BookStorage.REPORT_COL_LIBELLE));
		o.setReference(str(_row, BookStorage.REPORT_COL_REFERENCE));
		o.setDateValeur(date(_row, BookStorage.REPORT_COL_DATE_VALEUR));
		o.setMontant(number(_row, BookStorage.REPORT_COL_MONTANT));
		o.setCatego(str(_row, BookStorage.REPORT_COL_CATEGO));
		if ("NEW_FIXED".equals(str(_row, BookStorage.REPORT_COL_NEW_MONTH_FIXED))) {
			currentMonthAdjusted++;
		}
		if ("NEW_BANK".equals(str(_row, BookStorage.REPORT_COL_NEW_MONTH_BANK))) {
			currentMonth++;
		}
		o.setMonth(currentMonth);
		o.setMonthAdjusted(currentMonthAdjusted);
		return o;
	}

	public int getCurrentMonth() {
		return currentMonth;
	}

	public int getCurrentMonthAdjusted() {
		return currentMonthAdjusted;
	}

}
