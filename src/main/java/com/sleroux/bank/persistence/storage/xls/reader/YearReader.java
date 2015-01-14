package com.sleroux.bank.persistence.storage.xls.reader;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Sheet;

import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.persistence.storage.xls.BookStorage;
import com.sleroux.bank.util.Transformer;

public class YearReader extends Transformer {

	public Year transform(Sheet s, int _year, boolean _lastYear) {
		Year year = new Year();
		year.setSheetIndex(s.getWorkbook().getSheetIndex(s));
		year.setYear(_year);
		year.setInitialCredit(getInitalCredit(s));
		year.setLastYearOfTheBook(_lastYear);
		OperationReader tr = new OperationReader();
		for (int line = 1; line <= s.getLastRowNum(); line++) {
			Operation newOperation = tr.transform(s.getRow(line), line);
			if (newOperation.getAccountID() == null && newOperation.getDateCompta() == null && newOperation.getDateOperation() == null
					&& newOperation.getDateValeur() == null && newOperation.getLibelle() == null && newOperation.getReference() == null
					&& newOperation.getMontant() == null) {
				// System.out.println("found empty line " + newOperation);
				continue;
			}
			year.addOperation(newOperation);
		}
		year.setLastMonthBank(tr.getCurrentMonth());
		year.setLastMonthAdjusted(tr.getCurrentMonthAdjusted());
		return year;
	}

	private BigDecimal getInitalCredit(Sheet _s) {
		return number(_s.getRow(0), BookStorage.REPORT_COL_NEW_MONTH_BANK_REPORT);
	}

}
