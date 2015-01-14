package com.sleroux.bank.persistence.storage.xls.writer;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.persistence.storage.xls.BookStorage;

public class YearWriter extends Writer {

	public YearWriter(Workbook _wb) {
		super(_wb);
	}

	public void write(Sheet _sheet, Year _year) {
		for (int i = 1; i <= _sheet.getLastRowNum(); i++) {
			Row row = _sheet.getRow(i);
			if (row != null) {
				_sheet.removeRow(row);
			}
		}
		int rowNum = 1;
		int newMonth = 0;
		int newMonthAdjusted = 0;
		for (Operation o : _year.getOperations()) {
			String newBank = o.getMonth() + "";
			String newAdjusted = o.getMonthAdjusted() + "";
			if (o.getMonth() > newMonth) {
				newBank = "NEW_BANK";
				newMonth = o.getMonth();
			}
			if (o.getMonthAdjusted() > newMonthAdjusted) {
				newAdjusted = "NEW_FIXED";
				newMonthAdjusted = o.getMonthAdjusted();
			}
			try {
				Row row = _sheet.createRow(rowNum);
				writeString(row, o.getAccountID(), BookStorage.REPORT_COL_COMPTE);
				writeDate(row, o.getDateCompta(), BookStorage.REPORT_COL_DATE_COMPTA);
				writeDate(row, o.getDateOperation(), BookStorage.REPORT_COL_DATE_OPERATION);
				writeString(row, o.getLibelle(), BookStorage.REPORT_COL_LIBELLE);
				writeString(row, o.getReference(), BookStorage.REPORT_COL_REFERENCE);
				writeDate(row, o.getDateValeur(), BookStorage.REPORT_COL_DATE_VALEUR);
				writeNumber(row, o.getMontant(), BookStorage.REPORT_COL_MONTANT);
				writeString(row, o.getCatego(), BookStorage.REPORT_COL_CATEGO);
				writeString(row, newBank, BookStorage.REPORT_COL_NEW_MONTH_BANK);
				writeString(row, newAdjusted, BookStorage.REPORT_COL_NEW_MONTH_FIXED);
			} catch (Exception e) {
				System.out.println(o);
				e.printStackTrace();
			}
			rowNum++;
		}
	}

}
