package com.sleroux.bank.evo.document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.sleroux.bank.model.Budget;
import com.sleroux.bank.util.Config;

public class BudgetDocument {

	private String				documentFile;
	private Workbook			wb;
	private Sheet				sheet;
	private Logger				logger		= Logger.getLogger(BudgetDocument.class);

	private final static short	ROW_YEAR	= 0;
	private final static short	ROW_MONTH	= 1;
	private final static short	ROW_MARKER	= 2;
	private final static short	ROW_TOTAL_1	= 3;
	private final static short	ROW_TOTAL_2	= 4;
	private final static short	ROW_TOTAL_3	= 5;
	private final static short	ROW_TOTAL_4	= 6;

	private final static short	COL_0		= 0;
	private final static short	COL_1		= 1;

	public BudgetDocument(String _budgetDocument) throws Exception {
		documentFile = _budgetDocument;

		System.out.println(documentFile);

		try {
			wb = new HSSFWorkbook(new FileInputStream(documentFile));

			sheet = wb.getSheet("Comptes");
			if (sheet == null) {
				throw new Exception("Unable to find the sheet named 'Comptes'");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (wb == null) {
			System.err.println("Workbook is null");
			return;
		}
	}

	public void close() {
		logger.info("Write book");

		try {
			wb.write(new FileOutputStream(Config.getBudgetDocument()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeCredits(List<String> _credits) throws Exception {
		int row = getRowForKey("CREDIT") + 1;
		insertCategories(_credits, row);
	}

	public void writeDebits(List<String> _debits) throws Exception {

		int row = getRowForKey("DEBIT") + 1;

		insertCategories(_debits, row);

	}

	private void insertCategories(List<String> _credits, int row) {
		sheet.shiftRows(row + 1, sheet.getLastRowNum(), _credits.size(), true, false);
		Cell model = sheet.getRow(row).getCell(0);
		for (int i = 0; i < _credits.size(); i++) {
			int index = row + 1 + i;
			Cell dest = getCell(index, COL_0);
			dest.setCellValue(_credits.get(i));
			dest.setCellStyle(model.getCellStyle());
			getCell(index, COL_1).setCellStyle(getCell(index - 1, COL_1).getCellStyle());
		}
		// Delete template
		sheet.removeRow(model.getRow());
		sheet.shiftRows(row + 1, sheet.getLastRowNum(), -1);
	}

	private short getRowForKey(String key) throws Exception {
		short row = 0;
		while (row < sheet.getLastRowNum()) {
			Row r = sheet.getRow(row);
			if (r != null) {
				Cell c = r.getCell(0);
				if (c != null) {
					String content = c.getStringCellValue();
					if (content != null && content.equals(key)) {
						return row;
					}
				}
			}
			row++;
		}
		throw new Exception("Unable to find key : [" + key + "]");
	}

	public void writeYears(List<Integer> _years) throws Exception {

		// Write month labels
		Calendar cal = Calendar.getInstance();
		Calendar now = Calendar.getInstance();

		short totalIndex = getRowForKey("TOTAL");

		for (int i = 1; i <= _years.size() * 12; i++) {

			int yearIndex = ((i - 1) / 12);
			int monthIndex = (i - 1) % 12;

			// Year
			if (monthIndex == 0) {
				getCell(ROW_YEAR, i).setCellValue(_years.get(yearIndex));
				if (i > 1) {
					// Copy style
					getCell(ROW_YEAR, i).setCellStyle(getCell(ROW_YEAR, 1).getCellStyle());
				}
				sheet.addMergedRegion(new CellRangeAddress(ROW_YEAR, ROW_YEAR, i, i + 11));

			}

			// Month

			cal.set(_years.get(yearIndex), monthIndex, 1);
			getCell(ROW_MONTH, i).setCellValue(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.FRENCH));
			if (i > 1) {
				getCell(ROW_MONTH, i).setCellStyle(getCell(ROW_MONTH, 1).getCellStyle());
			}

			// Marker

			if (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
				CellStyle red = wb.createCellStyle();
				red.setFillForegroundColor(HSSFColor.RED.index);
				red.setFillPattern(CellStyle.SOLID_FOREGROUND);
				getCell(ROW_MARKER, i).setCellStyle(red);
			} else if (cal.before(now) && i > 1) {
				getCell(ROW_MARKER, i).setCellStyle(getCell(ROW_MARKER, 1).getCellStyle());
			} else if (i > 1) {
				getCell(ROW_MARKER, i).setCellStyle(getCell(0, 0).getCellStyle());
			}

			// Total
			setCellFormula(ROW_TOTAL_1, i, columnName(i) + (totalIndex + 4));
			setCellFormula(ROW_TOTAL_4, i, "sum(" + ref(ROW_TOTAL_1 + 1, i) + ")+sum(" + ref(ROW_TOTAL_2 + 1, i)
					+ ")+sum(" + ref(ROW_TOTAL_3 + 1, i) + ")");

		}

	}

	private String ref(int _row, int _col) {
		return columnName(_col) + _row;
	}

	private void setCellFormula(int _row, int _column, String _formula) {
		if (_formula == null) {
			return;
		}
		getCell(_row, _column).setCellFormula(_formula);
		if (_column > 1) {
			getCell(_row, _column).setCellStyle(getCell(_row, 1).getCellStyle());
		}

	}

	private Cell getCell(int _row, int _column) {
		Row r = sheet.getRow(_row);
		if (r == null) {
			r = sheet.createRow(_row);
		}
		Cell c = r.getCell(_column);
		if (c == null) {
			c = r.createCell(_column);
		}
		return c;
	}

	public static String columnName(int index) {
		StringBuilder s = new StringBuilder();
		while (index >= 26) {
			s.insert(0, (char) ('A' + index % 26));
			index = index / 26 - 1;
		}
		s.insert(0, (char) ('A' + index));
		return s.toString();
	}

	public void writeMonth(int _index, List<Budget> _monthCredits, List<Budget> _monthDebits) throws Exception {
		int creditsRow = getRowForKey("CREDIT");
		int debitsRow = getRowForKey("DEBIT");

		// Credits
		getCell(creditsRow, _index).setCellStyle(getCell(creditsRow, COL_1).getCellStyle());
		CellStyle style = getCell(creditsRow + 1, COL_1).getCellStyle();
		for (Budget credit : _monthCredits) {
			Cell c = getCell(++creditsRow, _index);
			if (credit.getCredit() != null) {
				c.setCellValue(credit.getCredit().doubleValue());
			} else {
				c.setCellValue("-");
			}
			c.setCellStyle(style);
		}

		// Debits
		getCell(debitsRow, _index).setCellStyle(getCell(debitsRow, COL_1).getCellStyle());
		for (Budget debit : _monthDebits) {
			Cell c = getCell(++debitsRow, _index);
			if (debit.getDebit() != null) {
				c.setCellValue(debit.getDebit().doubleValue());
			} else {
				c.setCellValue("-");
			}
			c.setCellStyle(style);
		}

		// total
		int rowTotal = getRowForKey("TOTAL");
		getCell(rowTotal, _index).setCellStyle(getCell(rowTotal, COL_1).getCellStyle());
		creditsRow = getRowForKey("CREDIT") + 2;
		debitsRow = getRowForKey("DEBIT") + 2;
		String cname = columnName(_index);

		String sumCredit = "SUM(" + cname + creditsRow + ":" + cname + (creditsRow + _monthCredits.size() - 1) + ")";
		String sumDebit = "SUM(" + cname + debitsRow + ":" + cname + (debitsRow + _monthDebits.size() - 1) + ")";
		String total = cname + (rowTotal + 2) + "-" + cname + (rowTotal + 3);

		total += "+" + columnName(_index - 1) + (rowTotal + 4);

		setCellFormula(rowTotal + 1, _index, sumCredit);
		setCellFormula(rowTotal + 2, _index, sumDebit);
		setCellFormula(rowTotal + 3, _index, total);

	}

	public void addCompteEpargne(String _compte) throws Exception {

		int row = sheet.getLastRowNum() + 2;
		int totalRow = getRowForKey("TOTAL");

		Cell title = getCell(row, COL_0);
		title.setCellValue(_compte);
		title.setCellStyle(getCell(totalRow, COL_0).getCellStyle());

		CellStyle header = getCell(totalRow + 1, COL_0).getCellStyle();
		Cell credit = getCell(row + 1, COL_0);
		credit.setCellValue("Crédit");
		credit.setCellStyle(header);

		Cell debit = getCell(row + 2, COL_0);
		debit.setCellValue("Débit");
		debit.setCellStyle(header);

		Cell solde = getCell(row + 3, COL_0);
		solde.setCellValue("Solde");
		solde.setCellStyle(header);
	}

	public void writeSummary(String _compte, int _index, Budget _summary) throws Exception {
		int row = getRowForKey(_compte);

		Cell header = getCell(row, _index);
		Cell credit = getCell(row + 1, _index);
		Cell debit = getCell(row + 2, _index);
		Cell total = getCell(row + 3, _index);

		if (_summary != null && _summary.getCredit() != null) {
			credit.setCellValue(_summary.getCredit().doubleValue());
		} else {
			credit.setCellValue("-");
		}

		if (_summary != null && _summary.getDebit() != null) {
			debit.setCellValue(_summary.getDebit().doubleValue());
		} else {
			debit.setCellValue("-");
		}
		String formula = "sum(" + columnName(_index) + (row + 2) + ")-sum(" + columnName(_index) + (row + 3) + ")";
		if (_index > 1) {
			formula += " + " + columnName(_index - 1) + (row + 4);
		}

		total.setCellFormula(formula);

		header.setCellStyle(getCell(row, COL_0).getCellStyle());
		CellStyle style = getCell(getRowForKey("TOTAL") + 1, COL_1).getCellStyle();
		credit.setCellStyle(style);
		debit.setCellStyle(style);
		total.setCellStyle(style);
	}

	public void makeSummary(int _index, List<String> _comptes) throws Exception {
		// Reserve
		StringBuilder formula = new StringBuilder();
		for (String compte : _comptes) {
			if (!compte.equals("PEL")) {
				if (formula.length() > 0) {
					formula.append("+");
				}
				formula.append(columnName(_index) + (getRowForKey(compte) + 4));
			}
		}
		setCellFormula(ROW_TOTAL_2, _index, formula.toString());

		// Epargne
		formula = new StringBuilder();
		for (String compte : _comptes) {
			if (compte.equals("PEL")) {
				if (formula.length() > 0) {
					formula.append("+");
				}
				formula.append(columnName(_index) + (getRowForKey(compte) + 4));
			}
		}
		if (formula.length() == 0) {
			formula.append("0");
		}
		setCellFormula(ROW_TOTAL_3, _index, formula.toString());
	}

	public List<Integer> getYears() {
		List<Integer> list = new ArrayList<>();
		int col = COL_1;
		Row row = sheet.getRow(ROW_YEAR);
		while (row.getCell(col) != null) {
			Double value = row.getCell(col).getNumericCellValue();
			if (value != null && !value.equals("")) {
				list.add(value.intValue());
			}
			col = col + 12;
		}

		return list;
	}

	public List<String> getCredits() throws Exception {
		List<String> list = new ArrayList<>();
		int row = getRowForKey("CREDIT") + 1;
		while (!getCell(row, COL_0).getStringCellValue().equals("")) {
			String key = getCell(row, COL_0).getStringCellValue();
			list.add(key);
			row++;
		}
		return list;
	}

	public List<String> getDebits() throws Exception {
		List<String> list = new ArrayList<>();
		int row = getRowForKey("DEBIT") + 1;
		while (!getCell(row, COL_0).getStringCellValue().equals("")) {
			String key = getCell(row, COL_0).getStringCellValue();
			list.add(key);
			row++;
		}
		return list;
	}

	public List<Budget> readOperations(int _year, int _month, int _index, List<String> _credits, List<String> _debits)
			throws Exception {
		List<Budget> budgets = new ArrayList<>();

		int rowCredit = getRowForKey("CREDIT") + 1;
		int rowDebit = getRowForKey("DEBIT") + 1;

		for (int i = 0; i < _credits.size(); i++) {
			BigDecimal val = getCellValue(rowCredit + i, _index);
			if (val != null) {
				Budget b = new Budget();
				b.setCatego(_credits.get(i));
				b.setCredit(val);
				b.setMonth(_month);
				b.setYear(_year);
				b.setCompte("COURANT");
				budgets.add(b);
			}
		}

		for (int i = 0; i < _debits.size(); i++) {
			BigDecimal val = getCellValue(rowDebit + i, _index);
			if (val != null) {
				Budget b = new Budget();
				b.setCatego(_debits.get(i));
				b.setDebit(val);
				b.setMonth(_month);
				b.setYear(_year);
				b.setCompte("COURANT");
				budgets.add(b);
			}
		}

		return budgets;
	}

	public List<String> getComptesEpargne() throws Exception {
		List<String> list = new ArrayList<String>();
		int row = getRowForKey("TOTAL");
		CellStyle style = getCell(row, COL_0).getCellStyle();

		for (int i = row + 1; i < sheet.getLastRowNum(); i++) {
			Row r = sheet.getRow(i);
			if (r != null) {
				Cell c = r.getCell(COL_0);
				if (c != null && c.getCellStyle() != null && c.getCellStyle().getIndex() == style.getIndex()) {
					list.add(c.getStringCellValue());
				}
			}
		}

		return list;
	}

	public List<Budget> readComptesEpargnes(int _year, int _month, int _index, String _compte) throws Exception {
		List<Budget> list = new ArrayList<>();
		int row = getRowForKey(_compte);
		BigDecimal credit = getCellValue(row + 1, _index);
		BigDecimal debit = getCellValue(row + 2, _index);

		if (credit != null || debit != null) {
			Budget b = new Budget();
			b.setYear(_year);
			b.setMonth(_month);
			b.setCatego("NOPE");

			if (credit != null) {
				b.setCredit(credit);
			}

			if (debit != null) {
				b.setDebit(debit);
			}

			b.setCompte(_compte);
			list.add(b);
		}

		return list;
	}

	private BigDecimal getCellValue(int _i, int _index) {
		Cell c = getCell(_i, _index);
		if (c.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			return new BigDecimal(c.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP);
		}
		return null;
	}
}
