package com.sleroux.bank.business.extract;

import java.util.List;

import org.apache.log4j.Logger;

import com.sleroux.bank.Bank;
import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.persistence.dao.extract.ExtractDao;
import com.sleroux.bank.persistence.dao.extract.ExtractDaoImpl;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

public class FileImport extends BusinessServiceAbstract {

	private Logger		logger	= Logger.getLogger(FileImport.class);
	private ExtractDao	extractDao;

	public FileImport() {
		extractDao = new ExtractDaoImpl();
	}

	public void run() {
		ConsoleAppHeader.printAppHeader("IMPORT");
		List<ExtractDocument> extracts = extractDao.getAll();
		if (extracts.size() > 0) {
			System.out.println("Found files :");
			for (ExtractDocument s : extracts) {
				System.out.println(s.getFilename());
			}
		} else {
			System.out.println("There is no file to import in " + Config.getExtractDownloadPath());
			return;
		}
		logger.info("start merge");
		Book book = getBookDao().getBook();
		MergeExtract mergeExtract = new MergeExtract(book);
		int terminalWidth = Bank.getInstance().getTerminalWidth();
		String line = "";
		for (int i = 0; i < terminalWidth; i++) {
			line += ".";
		}
		for (ExtractDocument extract : extracts) {
			System.out.println(line);
			logger.info("merge report" + extract);
			mergeExtract.merge(extract);
		}
		System.out.println(line);
		if (mergeExtract.nbNewLines() > 0) {
			getBookDao().saveBook(book);
		}
		// rename extracted files
		for (ExtractDocument e : extracts) {
			extractDao.markAsProcessed(e);
		}
	}
}
