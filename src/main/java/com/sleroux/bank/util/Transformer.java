package com.sleroux.bank.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import com.sleroux.bank.util.formats.DateFormater;

public class Transformer {

	protected String str(Row _row, int _col) {
		if (_row == null)
			return null;
		Cell cell = _row.getCell(_col);
		if (cell == null)
			return null;
		String value = null;
		try {
			value = cell.getStringCellValue();
		} catch (Exception e) {
			value = Double.toString(cell.getNumericCellValue());
		}
		return value;
	}

	protected BigDecimal number(Row _row, int reportColMontant) {
		if (_row == null)
			return null;
		Cell cell = _row.getCell(reportColMontant);
		if (cell == null)
			return null;
		double s = cell.getNumericCellValue();
		BigDecimal bigDecimal = new BigDecimal(Double.toString(s));
		return bigDecimal;
	}

	protected Date date(Row _row, int reportColDateCompta) {
		if (_row == null)
			return null;
		Cell cell = _row.getCell(reportColDateCompta);
		if (cell == null)
			return null;
		Date date = null;
		try {
			date = cell.getDateCellValue();
		} catch (Exception e) {
			try {
				String d = cell.getStringCellValue();
				if (d != null) {
					date = DateFormater.parse(d);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return date;

	}

	protected BigDecimal readNumberValue(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return new BigDecimal(Double.toString(cell.getNumericCellValue()));
		}
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			String content = cell.getStringCellValue();
			try {
				return new BigDecimal(content);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

			Workbook wb = cell.getRow().getSheet().getWorkbook();
			FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
			CellValue cellValue = evaluator.evaluate(cell);

			switch (cellValue.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				Double dd = cellValue.getNumberValue();
				return roundTwoDecimals(dd);
			case Cell.CELL_TYPE_STRING:
				String content = cell.getStringCellValue();
				try {
					return new BigDecimal(content);
				} catch (NumberFormatException e) {
					return null;
				}
			}
		}
		return null;
	}

	private static BigDecimal roundTwoDecimals(double d) {
		DecimalFormat df = new DecimalFormat("#.00");
		df.setMaximumFractionDigits(2);
		df.setParseBigDecimal(true);
		DecimalFormatSymbols sym = new DecimalFormatSymbols();
		sym.setDecimalSeparator('.');
		char ch = 0;
		sym.setGroupingSeparator(ch);
		df.setDecimalFormatSymbols(sym);

		BigDecimal bd2 = new BigDecimal(df.format(d));

		return bd2;
	}
	
}
