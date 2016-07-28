package com.sleroux.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.presentation.ImportReportPresenter;
import com.sleroux.bank.service.ExtractService;
import com.sleroux.bank.service.ImportService;
import com.sleroux.bank.service.ImportType;
import com.sleroux.bank.util.Config;

@Component
public class ExtractController extends BusinessServiceAbstract {

	@Autowired
	ImportService	importService;

	@Autowired
	ExtractService	extractService;

	public List<String> runExtract() {

		// Request CMB website
		extractService.runExtract(Config.getImportCommandCMB());

		// list files from download directory
		return extractService.getFilesCMB();
	}

	@Override
	public void run() throws Exception {
		ImportReport reportBPO;
		{
			ConsoleAppHeader.printAppHeader("Extract BPO");
			extractService.runExtract(Config.getImportCommandBPO());
			List<String> files = extractService.getFilesBPO();
			ConsoleAppHeader.printAppHeader("Import BPO");

			reportBPO = importService.importFiles(ImportType.BPO, files);
			

		}
		
		ImportReport reportCMB;
		{
			ConsoleAppHeader.printAppHeader("Extract CMB");
			List<String> files = runExtract();
			ConsoleAppHeader.printAppHeader("Import");
			reportCMB = importService.importFiles(ImportType.CMB, files);
		}

		ImportReportPresenter.displayReport(reportCMB);
		ImportReportPresenter.displayReport(reportBPO);
	

	}

}
