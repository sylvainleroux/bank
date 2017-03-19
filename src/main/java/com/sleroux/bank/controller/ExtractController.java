package com.sleroux.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.domain.SessionData;
import com.sleroux.bank.model.Account;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.presentation.ImportReportPresenter;
import com.sleroux.bank.service.AccountService;
import com.sleroux.bank.service.ExtractService;
import com.sleroux.bank.service.ImportService;
import com.sleroux.bank.service.ImportType;
import com.sleroux.bank.service.impl.MockImportService;
import com.sleroux.bank.util.Encryption;

@Component
public class ExtractController extends AbstractController {
	
	@Autowired
	ImportService		importService;

	@Autowired
	ExtractService		extractService;

	@Autowired
	SessionData			sessionData;

	@Autowired
	MockImportService	mockImportService;

	@Autowired
	AccountService		accountService;

	@Override
	public void run() throws Exception {

		List<Account> accounts = accountService.getAccountsByUserID(sessionData.getUserID());

		accounts.forEach(account -> {
			String login = account.getLogin();
			String password = null;
			try {
				String sha1 = Encryption.sha1(sessionData.getPassword() + Encryption.encryptionSalt());
				password = Encryption.mysqlAesDecrypt(account.getPasswordEnc(),sha1);
				System.out.println(password);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (password == null) {
				System.out.println("No password");
				return;
			}
			
			extractService.launchExtract(account.getType(), login, password);

		});

	}

	public void archive() {
		// Credentials
		// sessionData.getCmbLogin();
		// sessionData.getPassword();

		// Crawler interface
		// command = Config.geteImportCommandCMB

		// BPO Import

		ConsoleAppHeader.printAppHeader("Extract BPO");
		// extractService.runExtract(Config.getImportCommandBPO());

		ImportReport reportBPO = importService.importFiles(ImportType.BPO, extractService.getFilesBPO());
		ImportReportPresenter.displayReport(reportBPO);

		// Generic CMB Importer
		// Hide password from process-list : write in File ?

		ConsoleAppHeader.printAppHeader("Extract CMB");
		// extractService.runExtract(Config.getImportCommandCMB());

		/*
		 * ConsoleAppHeader.printAppHeader("Import documents"); ImportReport
		 * reportCMB = importService.importFiles(ImportType.CMB,
		 * extractService.getFilesCMB());
		 * ImportReportPresenter.displayReport(reportCMB);
		 * 
		 * ConsoleAppHeader.printAppHeader("Balances");
		 * importService.updateBalances(extractService.getBalanceFiles());
		 * 
		 * // Mock - Create fake operation
		 * mockImportService.run(sessionData.getUserID());
		 * 
		 */

	}

}
