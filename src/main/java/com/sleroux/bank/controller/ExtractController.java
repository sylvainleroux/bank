package com.sleroux.bank.controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.presenter.common.ConsoleAppHeader;
import com.sleroux.bank.service.extract.ExtractService;
import com.sleroux.bank.service.extract.ImportReportPresenter;
import com.sleroux.bank.service.importer.ImportService;
import com.sleroux.bank.service.importer.ImportType;
import com.sleroux.bank.util.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtractController extends BusinessServiceAbstract {

	@Autowired
	ImportService	importService;

	@Autowired
	ExtractService	extractService;

	@Override
	public void run() throws Exception {

		ConsoleAppHeader.printAppHeader("Extract CMB"); 
		extractService.runExtract(Config.getImportCommandCMB());

		ConsoleAppHeader.printAppHeader("Extract Edenred");
		extractService.runExtract(Config.getImportCommandEdenred());

		ConsoleAppHeader.printAppHeader("Import documents");

		ImportReport reportCMB = importService.importFiles(ImportType.CMB, extractService.getFilesCMB());
		ImportReportPresenter.displayReport(reportCMB);

		ImportReport reportEdenred = importService.importFiles(ImportType.EDENRED, extractService.getFilesEdenred());
		ImportReportPresenter.displayReport(reportEdenred);

		ConsoleAppHeader.printAppHeader("Balances");
		importService.updateBalances(extractService.getBalanceFiles());

	}

}
