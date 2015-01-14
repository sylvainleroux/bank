package com.sleroux.bank.business.extract;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.sleroux.accountchecker.AccountChecker;
import com.sleroux.accountchecker.banquepopulaire.Cyberplus;
import com.sleroux.accountchecker.banquepopulaire.util.notifier.ConsoleNotifier;
import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;
import com.sleroux.bank.util.formats.DateFormater;

public class CyberplusImport extends BusinessServiceAbstract {

	private final static int	NB_DAYS_RE_DOWNLOAD	= 2;

	private Logger				logger				= Logger.getLogger(CyberplusImport.class);

	public void run() throws Exception {
		ConsoleAppHeader.printAppHeader("CYBERPLUS DONWLOAD");
		Book book = getBookDao().getBook();
		Year lastYear = book.getLastYear();
		// Calculate export time range, based on last import date
		Calendar fromDate = Calendar.getInstance();
		fromDate.setTime(lastYear.getLastOperationWithNonNullDate().getDateOperation());
		fromDate.add(Calendar.DATE, -NB_DAYS_RE_DOWNLOAD);
		Calendar toDate = Calendar.getInstance();
		System.out.println("Account : " + Config.getMainAccountID());
		System.out.println("From    : " + DateFormater.f(fromDate.getTime()));
		System.out.println("To      : " + DateFormater.f(toDate.getTime()));
		ConsoleAppHeader.printLine();
		ExtractDocument extract = null;
		try {
			AccountChecker cyberplusExtract = new Cyberplus(new ConsoleNotifier());
			cyberplusExtract.authenticate(Config.getLogin(), Config.getPassword());
			extract = cyberplusExtract.getLastOperations(Config.getMainAccountID(), Config.getCodeBanque(), fromDate.getTime(), toDate.getTime());
		} catch (Exception e) {
			throw new Exception("Download faileds", e);
		}
		if (extract == null) {
			throw new Exception("Null export");
		}
		logger.info("start merge");
		ConsoleAppHeader.printAppHeader("NEW OPERATIONS MERGE");
		MergeExtract mergeExtract = new MergeExtract(book);
		logger.info("merge report" + extract);
		mergeExtract.merge(extract);
		if (mergeExtract.nbNewLines() > 0) {
			getBookDao().saveBook(book);
		}

	}
}
