package com.sleroux.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.domain.SessionData;
import com.sleroux.bank.model.Account;
import com.sleroux.bank.model.extract.ExtractedReport;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.presentation.ImportReportPresenter;
import com.sleroux.bank.service.AccountService;
import com.sleroux.bank.service.ExtractService;
import com.sleroux.bank.service.ImportService;
import com.sleroux.bank.service.ImportType;
import com.sleroux.bank.util.Encryption;

@Component
public class ExtractController extends AbstractController {

	@Autowired
	ImportService	importService;

	@Autowired
	ExtractService	extractService;

	@Autowired
	SessionData		sessionData;

	@Autowired
	AccountService	accountService;

	@Override
	public void run() throws Exception {

		List<Account> accounts = accountService.getAccountsByUserID(sessionData.getUserID());

		accounts.forEach(account -> {
			String password = getAccountPassword(sessionData.getPassword(), account.getPasswordEnc());
			List<ExtractedReport> extractedReport = extractService.extractData(account.getType(), account.getLogin(),
					password);
			
			
			System.out.println("complete");
		});

	}

	private String getAccountPassword(String _sessionPassword, String _encryptedAccoundPassword) {
		try {
			String sha1 = Encryption.sha1(_sessionPassword + Encryption.encryptionSalt());
			return Encryption.mysqlAesDecrypt(_encryptedAccoundPassword, sha1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void archive() {

		//ImportReport reportBPO = importService.importFiles(ImportType.BPO, extractService.getFilesBPO());
		//ImportReportPresenter.displayReport(reportBPO);


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
