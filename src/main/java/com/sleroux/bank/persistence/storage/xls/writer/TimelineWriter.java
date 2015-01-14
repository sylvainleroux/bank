package com.sleroux.bank.persistence.storage.xls.writer;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.persistence.storage.xls.TimelineColumns;

public class TimelineWriter extends Writer implements TimelineColumns {

	private Sheet	sheet;

	public TimelineWriter(Workbook _wb) {
		super(_wb);
		sheet = _wb.getSheetAt(SHEET_MAIN_TIMELINE);
	}

	public void write(BugdetMonth _monthBudget) {
		int column = _monthBudget.getColumnIndex();
		for (String key : BugdetMonth.getKeyList().getCredit().keySet()) {
			int rowIndex = BugdetMonth.getKeyList().getCredit().get(key);
			BigDecimal credit = _monthBudget.getCredits().get(key);
			if (credit == null) {
				writeString(sheet, rowIndex, "-", _monthBudget.getColumnIndex());
			} else {
				writeNumber(sheet, rowIndex, credit, _monthBudget.getColumnIndex());
			}
		}

		for (String key : BugdetMonth.getKeyList().getDebit().keySet()) {
			int rowIndex = BugdetMonth.getKeyList().getDebit().get(key);
			BigDecimal debit = _monthBudget.getDebits().get(key);
			if (debit == null) {
				writeString(sheet, rowIndex, ".", _monthBudget.getColumnIndex());
			} else {
				writeNumber(sheet, rowIndex, debit.negate(), _monthBudget.getColumnIndex());
			}
		}

		int rowIndex = ROW_TOTAL;
		setDoubleValue(sheet, rowIndex++, column, _monthBudget.getTotalCredit().doubleValue());
		setDoubleValue(sheet, rowIndex++, column, _monthBudget.getTotalDebit().negate().doubleValue());
		CellReference report = new CellReference(ROW_TOTAL + 2, column - 1, false, false);
		CellReference credit = new CellReference(ROW_TOTAL, column, false, false);
		CellReference debit = new CellReference(ROW_TOTAL + 1, column, false, false);
		CellReference solde = new CellReference(ROW_TOTAL + 2, column, false, false);
		String reportString = null;
		if (column == 1) {
			reportString = Double.toString(_monthBudget.getSolde().doubleValue());
		} else {
			reportString = report.formatAsString();
		}
		String formula = reportString + "+" + credit.formatAsString() + "-" + debit.formatAsString();
		setFormula(sheet, rowIndex++, column, formula);
		// setDoubleValue(sheet, rowIndex, column, );
		setFormula(sheet, rowIndex++, column, _monthBudget.getEndOfMonthSolde().doubleValue() + "-" + solde.formatAsString());
	}
}
