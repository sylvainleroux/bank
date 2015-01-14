package com.sleroux.bank.persistence.storage.xls.writer;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.persistence.storage.xls.BookStorage;

public class MonthReportWriter extends Writer {

	public MonthReportWriter(Workbook _wb) {
		super(_wb);
	}

	public void write(Sheet _sheet, int _line, Month _bankReport) {
		writeNumber(_sheet, _line, _bankReport.getSolde(), BookStorage.REPORT_COL_NEW_MONTH_BANK_REPORT);
		writeNumber(_sheet, _line + 1, _bankReport.getDebit(), BookStorage.REPORT_COL_NEW_MONTH_BANK_REPORT);
		writeNumber(_sheet, _line + 2, _bankReport.getCredit(), BookStorage.REPORT_COL_NEW_MONTH_BANK_REPORT);
	}

}
