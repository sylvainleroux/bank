package com.sleroux.bank.persistence.storage.xls.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sleroux.bank.model.statement.Book;

public class BookReader {

	public Book transform(Workbook workBook) throws Exception {
		Book book = new Book();
		List<Sheet> years = new ArrayList<Sheet>();
		for (int i = 1; i < workBook.getNumberOfSheets(); i++) {
			years.add(0, workBook.getSheetAt(i));
		}
		// Transform
		Iterator<Sheet> i = years.iterator();
		YearReader yearTransformer = new YearReader();
		while (i.hasNext()) {
			Sheet s = i.next();
			int year;
			try {
				year = Integer.parseInt(s.getSheetName());
			} catch (Exception e) {
				throw new Exception("Sheet titles must be years. ex: 2015", e);
			}
			book.getYears().add(yearTransformer.transform(s, year, !i.hasNext()));
		}
		return book;
	}
}
