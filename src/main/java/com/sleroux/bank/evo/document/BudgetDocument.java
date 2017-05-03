package com.sleroux.bank.evo.document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

	private String						documentFile;
	private Workbook					wb;
	private Sheet						sheet;
	private Logger						logger					= Logger.getLogger(BudgetDocument.class);

	private final static short			ROW_YEAR				= 0;
	private final static short			ROW_MONTH				= 1;
	private final static short			ROW_MARKER				= 2;
	private final static short			ROW_TOTAL				= 3;
	private final static short			ROW_SAVINGS_BLOCKED		= 4;
	private final static short			ROW_SAVINGS_AVAILABLE	= 5;
	private final static short			ROW_CHECKING			= 6;

	private final static short			ROW_TEMPLATE_COMPTE		= 8;
	private final static short			ROW_TEMPLATE_SUBTOTAL	= 9;
	private final static short			ROW_TEMPLATE_CATEGO		= 10;

	private final static short			COL_0					= 0;
	private final static short			COL_1					= 1;

	private final static String			DEBIT					= "DEBIT";
	private final static String			CREDIT					= "CREDIT";
	private final static String			SOLDE_INIT				= "SOLDE_INIT";

	private int							firstYear				= 0;

	private List<BudgetDocumentCompte>	checking				= new ArrayList<>();
	private List<BudgetDocumentCompte>	savingsAvailable		= new ArrayList<>();
	private List<BudgetDocumentCompte>	savingsBlocked			= new ArrayList<>();

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

	public BudgetDocumentCompte addCompte(String _compte, List<String> _credits, List<String> _debits, String _type) {
		BudgetDocumentCompte compte = new BudgetDocumentCompte(_compte, _credits, _debits);

		switch (_type) {
		case "SAVINGS_BLOCKED":
			savingsBlocked.add(compte);
			break;
		case "SAVINGS_AVAILABLE":
			savingsAvailable.add(compte);
			break;
		case "CHECKING":
		default:
			checking.add(compte);
			break;
		}

		return compte;
	}

	public void summary(int _year, int _month) {
		int col = (_year - firstYear) * 12 + _month + 1;

		CellStyle styleTotal = getCell(ROW_TOTAL, COL_1).getCellStyle();
		CellStyle style = getCell(ROW_CHECKING, COL_1).getCellStyle();

		String formulaTotal = "SUM(0";
		String formulaChecking = "SUM(0";
		String formulaSavingsBlocked = "SUM(0";
		String formulaSavingsAvailable = "SUM(0";

		String cname = columnName(col);

		for (BudgetDocumentCompte compte : checking) {
			String f = "+" + cname + (compte.lineFirst + 1);
			formulaTotal += f;
			formulaChecking += f;
		}

		for (BudgetDocumentCompte compte : savingsAvailable) {
			String f = "+" + cname + (compte.lineFirst + 1);
			formulaTotal += f;
			formulaSavingsAvailable += f;
		}

		for (BudgetDocumentCompte compte : savingsBlocked) {
			String f = "+" + cname + (compte.lineFirst + 1);
			formulaTotal += f;
			formulaSavingsBlocked += f;
		}

		formulaTotal += ")";
		formulaChecking += ")";
		formulaSavingsBlocked += ")";
		formulaSavingsAvailable += ")";

		Cell c;

		c = getCell(ROW_TOTAL, col);
		c.setCellFormula(formulaTotal);
		c.setCellStyle(styleTotal);

		c = getCell(ROW_CHECKING, col);
		c.setCellFormula(formulaChecking);
		c.setCellStyle(style);

		c = getCell(ROW_SAVINGS_AVAILABLE, col);
		c.setCellFormula(formulaSavingsAvailable);
		c.setCellStyle(style);

		c = getCell(ROW_SAVINGS_BLOCKED, col);
		c.setCellFormula(formulaSavingsBlocked);
		c.setCellStyle(style);

	}

	public class BudgetDocumentCompte {

		private HashMap<String, Integer>	credits	= new LinkedHashMap<>();
		private HashMap<String, Integer>	debits	= new LinkedHashMap<>();

		private int							lineFirst;
		private int							lineCredits;
		private int							lineDebits;

		public BudgetDocumentCompte(String _compte, List<String> _credits, List<String> _debits) {

			int line = sheet.getLastRowNum() + 2;
			lineFirst = line;

			// Headers
			Cell title = getCell(line++, COL_0);
			title.setCellValue(_compte);
			title.setCellStyle(getCell(ROW_TEMPLATE_COMPTE, COL_0).getCellStyle());

			lineCredits = line;
			Cell credit = getCell(line++, COL_0);
			credit.setCellValue(CREDIT);
			credit.setCellStyle(getCell(ROW_TEMPLATE_SUBTOTAL, COL_0).getCellStyle());

			CellStyle style = getCell(ROW_TEMPLATE_CATEGO, COL_0).getCellStyle();

			for (String s : _credits) {

				if (s.equals(SOLDE_INIT)) {
					continue;
				}

				credits.put(s, line);

				Cell c = getCell(line++, COL_0);
				c.setCellValue(s);
				c.setCellStyle(style);
			}

			lineDebits = line;
			Cell debit = getCell(line++, COL_0);
			debit.setCellValue(DEBIT);
			debit.setCellStyle(getCell(ROW_TEMPLATE_SUBTOTAL, COL_0).getCellStyle());

			for (String s : _debits) {
				if (s.equals(SOLDE_INIT)) {
					continue;
				}

				debits.put(s, line);

				Cell c = getCell(line++, COL_0);
				c.setCellValue(s);
				c.setCellStyle(style);
			}
		}

		public void addMonthData(Integer _year, int _month, List<Budget> _budgets) {

			if (firstYear == 0) {
				firstYear = _year;
			}

			int col = (_year - firstYear) * 12 + _month + 1;

			CellStyle cellStyle = getCell(ROW_TEMPLATE_SUBTOTAL + 1, COL_1).getCellStyle();

			BigDecimal soldeInit = BigDecimal.ZERO;
			Cell c;

			c = getCell(lineFirst, col);
			c.setCellStyle(getCell(ROW_TEMPLATE_COMPTE, COL_1).getCellStyle());

			c = getCell(lineCredits, col);
			c.setCellStyle(getCell(ROW_TEMPLATE_SUBTOTAL, COL_1).getCellStyle());

			for (int i = 0; i < credits.size(); i++) {
				getCell(i + 1 + lineCredits, col).setCellStyle(cellStyle);
			}

			for (Budget b : _budgets) {
				if (b.getCatego().equals(SOLDE_INIT)) {
					soldeInit = soldeInit.add(b.getCredit());
					continue;
				}
				if (b.getCredit().compareTo(BigDecimal.ZERO) > 0) {
					getCell(credits.get(b.getCatego()), col).setCellValue(b.getCredit().doubleValue());
				}

			}

			c = getCell(lineDebits, col);
			c.setCellStyle(getCell(ROW_TEMPLATE_SUBTOTAL, COL_1).getCellStyle());

			for (int i = 0; i < debits.size(); i++) {
				getCell(i + 1 + lineDebits, col).setCellStyle(cellStyle);
			}

			for (Budget b : _budgets) {
				if (b.getCatego().equals(SOLDE_INIT)) {
					soldeInit = soldeInit.subtract(b.getDebit());
					continue;
				}
				if (b.getDebit().compareTo(BigDecimal.ZERO) > 0) {
					getCell(debits.get(b.getCatego()), col).setCellValue(b.getDebit().doubleValue());
				}
			}

			// Tolal
			String cname = columnName(col);

			String sumCredit = "0";
			if (credits.size() > 0) {
				sumCredit = "SUM(" + cname + (lineCredits + 2) + ":" + cname + (lineCredits + credits.size() + 1) + ")";
			}

			String sumDebit = "0";
			if (debits.size() > 0) {
				sumDebit = "SUM(" + cname + (lineDebits + 2) + ":" + cname + (lineDebits + debits.size() + 1) + ")";
			}
			String total = cname + (lineCredits + 1) + "-" + cname + (lineDebits + 1);

			if (soldeInit.compareTo(BigDecimal.ZERO) != 0) {
				total = soldeInit.doubleValue() + " + " + total;
			}

			if (col > 1) {
				total += "+" + columnName(col - 1) + (lineFirst + 1);
			}

			setCellFormula(lineFirst, col, total);
			setCellFormula(lineCredits, col, sumCredit);
			setCellFormula(lineDebits, col, sumDebit);

		}

	}

	public void writeYears(List<Integer> _years) throws Exception {

		firstYear = _years.get(0);

		// Write month labels
		Calendar cal = Calendar.getInstance();
		Calendar now = Calendar.getInstance();

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

		}

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

		firstYear = list.get(0);

		return list;
	}

	private BigDecimal getCellValue(int _i, int _index) {
		Cell c = getCell(_i, _index);
		if (c.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			return new BigDecimal(c.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP);
		}
		return BigDecimal.ZERO;
	}

	public void clearTemplate() {
		for (int i = 0; i < 6; i++) {
			removeRow(ROW_TEMPLATE_COMPTE);
		}
	}

	public void removeRow(int rowIndex) {
		int lastRowNum = sheet.getLastRowNum();
		if (rowIndex >= 0 && rowIndex < lastRowNum) {
			sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
		}
		if (rowIndex == lastRowNum) {
			Row removingRow = sheet.getRow(rowIndex);
			if (removingRow != null) {
				sheet.removeRow(removingRow);
			}
		}
	}

	public List<BudgetDocumentCompteReader> getComptes() {
		List<BudgetDocumentCompteReader> comptes = new ArrayList<>();

		boolean blankLine = false;
		Cell c;
		String s;
		BudgetDocumentCompteReader cr = null;
		List<String> catego = new ArrayList<>();
		for (int row = ROW_CHECKING + 1; row <= sheet.getLastRowNum(); row++) {
			c = getCell(row, COL_0);
			s = c.getStringCellValue();
			if (s.trim().equals("")) {
				if (cr != null) {
					// Flush debits
					cr.debits.addAll(catego);
					catego.clear();
				}

				blankLine = true;
				continue;
			}

			if (s.trim().equals(CREDIT)) {
				cr.rowCredits = row;
				continue;
			}

			if (s.trim().equals(DEBIT)) {
				cr.rowDebits = row;
				// Flush credits
				if (cr != null) {
					cr.credits.addAll(catego);
					catego.clear();
				}
				continue;
			}

			if (blankLine) {
				cr = new BudgetDocumentCompteReader();
				cr.compte = s;
				cr.row = row;
				comptes.add(cr);
				blankLine = false;
				continue;
			}

			catego.add(s);

		}

		return comptes;
	}

	public class BudgetDocumentCompteReader {
		String			compte;
		int				row;
		int				rowCredits;
		int				rowDebits;
		List<String>	credits	= new ArrayList<>();
		List<String>	debits	= new ArrayList<>();

		public List<Budget> getBudget(int _year, int _month) {
			HashMap<String, Budget> list = new HashMap<>();

			int col = (_year - firstYear) * 12 + _month;
			Budget b;
			BigDecimal v;
			for (int i = 0; i < credits.size(); i++) {
				b = list.get(credits.get(i));
				if (b == null) {
					b = new Budget();
					b.setCatego(credits.get(i));
					b.setYear(_year);
					b.setMonth(_month);
					b.setCompte(compte);
					list.put(credits.get(i), b);
				}

				int row = rowCredits + 1 + i;
				v = getCellValue(row, col);
				b.setCredit(v);
			}

			for (int i = 0; i < debits.size(); i++) {
				b = list.get(debits.get(i));
				if (b == null) {
					b = new Budget();
					b.setCatego(debits.get(i));
					b.setYear(_year);
					b.setMonth(_month);
					b.setCompte(compte);
					list.put(debits.get(i), b);
				}

				int row = rowDebits + 1 + i;
				v = getCellValue(row, col);
				b.setDebit(v);
			}

			return new ArrayList<>(list.values());

		}

	}

}
