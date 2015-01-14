package com.sleroux.bank.persistence.dao.book;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.calc.MonthAdjusted;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.persistence.storage.xls.BookStorage;
import com.sleroux.bank.persistence.storage.xls.reader.BookReader;
import com.sleroux.bank.persistence.storage.xls.reader.TimelineReader;
import com.sleroux.bank.persistence.storage.xls.writer.MonthAdjustedReportWritter;
import com.sleroux.bank.persistence.storage.xls.writer.MonthReportWriter;
import com.sleroux.bank.persistence.storage.xls.writer.TimelineWriter;
import com.sleroux.bank.persistence.storage.xls.writer.YearWriter;

public class BookDaoImpl implements BookDao {

	private Logger			logger	= Logger.getLogger(BookDaoImpl.class);
	private TimelineReader	timelineReader;
	private TimelineWriter	timelineWriter;
	private BookStorage		lazyInitStorage;

	public BookStorage getStorage() {
		if (lazyInitStorage == null) {
			lazyInitStorage = BookStorage.getInstance();
			timelineReader = new TimelineReader(lazyInitStorage.getWorkBook());
			timelineWriter = new TimelineWriter(lazyInitStorage.getWorkBook());
			BugdetMonth.setKeyList(timelineReader.readKeyList());
		}
		return lazyInitStorage;
	}

	@Override
	public Book getBook() {
		BookReader bookTransformer = new BookReader();
		Book book = null;
		try {
			book = bookTransformer.transform(getStorage().getWorkBook());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return book;
	}

	@Override
	public void saveBook(Book _book) {
		Workbook wb = getStorage().getWorkBook();
		YearWriter yearWriter = new YearWriter(wb);
		for (Year year : _book.getYears()) {
			int sheetIndex = year.getSheetIndex();
			Sheet sheet = wb.getSheetAt(sheetIndex);
			if (sheet.getProtect()) {
				logger.info("Do not update proteced sheet : " + year.getYear());
				continue;
			}
			// Write data
			yearWriter.write(sheet, year);
			getStorage().setUpdated(true);
		}
	}

	@Override
	public void saveBookMonth(Year _year, Operation _op, Month _bankReport) {
		Sheet sheet = getStorage().getWorkBook().getSheetAt(_year.getSheetIndex());
		if (!sheet.getProtect()) {
			MonthReportWriter bankReportWriter = new MonthReportWriter(getStorage().getWorkBook());
			bankReportWriter.write(sheet, _op.getLine(), _bankReport);
			getStorage().setUpdated(true);
		}
	}

	@Override
	public void saveBookMonthAdjusted(Year _year, Operation _op, MonthAdjusted _monthData) {
		Sheet sheet = getStorage().getWorkBook().getSheetAt(_year.getSheetIndex());
		if (!sheet.getProtect()) {
			int rowIndex = _op.getLine();
			MonthAdjustedReportWritter writer = new MonthAdjustedReportWritter(getStorage().getWorkBook());
			writer.write(sheet, rowIndex, _monthData);
			getStorage().setUpdated(true);
		}
	}

	@Override
	public BugdetMonth getBudgetByYearMonth(int _year, int _month) {
		getStorage();
		return timelineReader.getBudget(_month, _year);
	}

	@Override
	public void saveBudget(BugdetMonth _monthBudget) {
		getStorage();
		timelineWriter.write(_monthBudget);
		getStorage().setUpdated(true);
	}

	@Override
	public BudgetKeys getBudgetKeys() {
		getStorage();
		return BugdetMonth.getKeyList();
	}

}
