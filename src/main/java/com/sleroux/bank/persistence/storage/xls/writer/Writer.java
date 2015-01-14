package com.sleroux.bank.persistence.storage.xls.writer;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Writer {

	private static CellStyle	cellStyleDate;
	private static CellStyle	cellStyleCurrency;

	public Writer(Workbook wb) {
		CreationHelper createHelper = wb.getCreationHelper();
		if (cellStyleDate == null) {
			cellStyleDate = wb.createCellStyle();
		}
		cellStyleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
		if (cellStyleCurrency == null) {
			cellStyleCurrency = wb.createCellStyle();
		}
		cellStyleCurrency.setDataFormat(createHelper.createDataFormat().getFormat("#0.00")); // #,##0.00

	}

	protected void writeString(Sheet _sheet, int _row, String _string, int _column) {
		if (_string == null || _string.equals(""))
			return;
		Row row = _sheet.getRow(_row);
		if (row == null)
			row = _sheet.createRow(_row);
		writeString(row, _string, _column);
	}

	protected void writeString(Row _row, String _string, int _column) {
		Cell c = _row.getCell(_column);
		if (c == null) {
			c = _row.createCell(_column);
		}
		c.setCellValue(_string);
	}

	protected void writeDate(Row _row, Date _date, int _column) {
		if (_date == null)
			return;
		Cell c = _row.createCell(_column);
		c.setCellValue(_date);
		c.setCellStyle(cellStyleDate);
	}

	protected void writeNumber(Row _row, BigDecimal _number, int _column) {
		if (_number == null)
			return;
		Cell c = _row.getCell(_column);
		if (c == null) {
			c = _row.createCell(_column, Cell.CELL_TYPE_NUMERIC);
			c.setCellStyle(cellStyleCurrency);
		}
		c.setCellValue(_number.doubleValue());
	}

	protected void writeNumber(Sheet _sheet, int _line, BigDecimal _number, int _column) {
		if (_number == null)
			return;
		Row row = _sheet.getRow(_line);
		if (row == null)
			row = _sheet.createRow(_line);
		writeNumber(row, _number, _column);
	}

	protected void setDoubleValue(Sheet sheet, int line, int i, double doubleValue) {
		Row row = sheet.getRow(line);
		if (row == null)
			row = sheet.createRow(line);
		Cell cell = row.getCell(i);
		if (cell == null)
			cell = row.createCell(i);
		cell.setCellValue(doubleValue);
	}

	protected void setFormula(Sheet sheet, int line, int _column, String formula) {
		Row row = sheet.getRow(line);
		if (row == null)
			row = sheet.createRow(line);
		Cell cell = row.getCell(_column);
		if (cell == null)
			cell = row.createCell(_column);
		cell.setCellType(Cell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);
	}

}
