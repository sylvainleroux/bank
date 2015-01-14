package com.sleroux.bank.persistence.storage.xls.writer;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sleroux.bank.model.calc.MonthAdjusted;
import com.sleroux.bank.persistence.storage.xls.BookStorage;

public class MonthAdjustedReportWritter extends Writer {

	public MonthAdjustedReportWritter(Workbook _wb) {
		super(_wb);
	}

	public void write(Sheet _sheet, int _rowIndex, MonthAdjusted _monthData) {
		int row = _rowIndex;
		writeString(_sheet, row, "DEBIT", BookStorage.REPORT_COL_NEW_MONTH_FIXED_REPORT + 1);
		writeString(_sheet, row, "DEBIT", BookStorage.REPORT_COL_NEW_MONTH_FIXED_REPORT + 2);
		row++;
		BigDecimal credit = new BigDecimal("0");
		BigDecimal debit = new BigDecimal("0");
		for (String s : _monthData.getMonthReport().keySet()) {
			writeString(_sheet, row, s, BookStorage.REPORT_COL_NEW_MONTH_FIXED_REPORT);
			BigDecimal value = _monthData.getMonthReport().get(s);
			if (BigDecimal.ZERO.compareTo(value) > 0) {
				writeNumber(_sheet, row, _monthData.getMonthReport().get(s).negate(), BookStorage.REPORT_COL_NEW_MONTH_FIXED_REPORT + 1);
				debit = debit.add(value);
			} else {
				writeNumber(_sheet, row, _monthData.getMonthReport().get(s), BookStorage.REPORT_COL_NEW_MONTH_FIXED_REPORT + 2);
				credit = credit.add(value);
			}
			row++;
		}
		writeString(_sheet, row, "TOTAL", BookStorage.REPORT_COL_NEW_MONTH_FIXED_REPORT);
		writeNumber(_sheet, row, debit.negate(), BookStorage.REPORT_COL_NEW_MONTH_FIXED_REPORT + 1);
		writeNumber(_sheet, row, credit, BookStorage.REPORT_COL_NEW_MONTH_FIXED_REPORT + 2);

	}
}
