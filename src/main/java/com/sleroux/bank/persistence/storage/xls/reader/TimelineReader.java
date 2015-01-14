package com.sleroux.bank.persistence.storage.xls.reader;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.persistence.storage.xls.TimelineColumns;
import com.sleroux.bank.util.Transformer;

public class TimelineReader extends Transformer implements TimelineColumns {

	private Workbook	wb;
	private Sheet		sheet;

	public TimelineReader(Workbook _wb) {
		wb = _wb;
		sheet = wb.getSheetAt(SHEET_MAIN_TIMELINE);
	}

	public BudgetKeys readKeyList() {
		BudgetKeys keyList = new BudgetKeys();
		keyList.getCredit().putAll(getKeys(ROW_CREDIT));
		keyList.getDebit().putAll(getKeys(ROW_DEBIT));
		return keyList;

	}

	private LinkedHashMap<String, Integer> getKeys(int _rowStart) {
		LinkedHashMap<String, Integer> list = new LinkedHashMap<String, Integer>();
		for (int rowIndex = _rowStart; sheet.getRow(rowIndex) != null && sheet.getRow(rowIndex).getCell(0) != null
				&& sheet.getRow(rowIndex).getCell(0).getStringCellValue() != ""; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			Cell keyCell = row.getCell(0);
			String key = keyCell.getStringCellValue();
			list.put(key, Integer.valueOf(rowIndex));
		}

		return list;
	}

	public BugdetMonth getBudget(int month, int year) {
		int monthColumn = findColumn(month, year);
		if (monthColumn <= 0) {
			System.err.println("Error in column selection");
			return null;
		}
		BugdetMonth budget = new BugdetMonth();
		budget.setMonth(month);
		budget.setYear(year);
		budget.setColumnIndex(monthColumn);
		// CREDIT
		for (Entry<String, Integer> entry : BugdetMonth.getKeyList().getCredit().entrySet()) {
			Cell valueCell = sheet.getRow(entry.getValue().intValue()).getCell(monthColumn);
			if (valueCell != null) {
				BigDecimal creditLine = readNumberValue(valueCell);
				budget.getCredits().put(entry.getKey(), creditLine);
				if (creditLine != null) {
					budget.setTotalCredit(budget.getTotalCredit().add(creditLine));
				}
			}
		}
		// DEBIT
		for (Entry<String, Integer> entry : BugdetMonth.getKeyList().getDebit().entrySet()) {
			Cell valueCell = sheet.getRow(entry.getValue().intValue()).getCell(monthColumn);
			if (valueCell != null) {
				BigDecimal debitLine = readNumberValue(valueCell);
				budget.getDebits().put(entry.getKey(), debitLine);
				if (debitLine != null) {
					budget.setTotalDebit(budget.getTotalDebit().add(debitLine));
				}
			}
		}
		return budget;
	}

	private int findColumn(int month, int year) {

		// Calendar month start at 0
		month = month - 1;
		Row row = sheet.getRow(TIMELINE_MONTH);
		Row yearRow = sheet.getRow(TIMELINE_YEAR);
		int nbCol = row.getLastCellNum();
		String lastFoundYear = "";
		Cell cell = null;
		for (int i = 1; i < nbCol; i++) {
			cell = row.getCell(i);
			if (cell == null) {
				continue;
			}
			String currentMonth = cell.getStringCellValue();
			cell = yearRow.getCell(i);
			String currentYear;
			if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				currentYear = lastFoundYear;
			} else {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					double val = cell.getNumericCellValue();
					currentYear = Long.toString(Math.round(val));
				} else {
					currentYear = cell.getStringCellValue();
				}
				lastFoundYear = currentYear;
			}
			Date date = null;
			DateFormat formatter = new SimpleDateFormat("MMMMM yyyy", Locale.forLanguageTag("fr-FR"));
			try {
				date = (Date) formatter.parse(currentMonth + " " + currentYear);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if (cal.get(Calendar.YEAR) == year) {
				if (cal.get(Calendar.MONTH) == month) {
					return i;
				}
			}
		}
		return -1;
	}

}
