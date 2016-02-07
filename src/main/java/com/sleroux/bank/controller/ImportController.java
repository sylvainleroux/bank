package com.sleroux.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.service.ExtractService;
import com.sleroux.bank.service.ImportService;
import com.sleroux.bank.service.ImportType;

@Component
public class ImportController extends BusinessServiceAbstract {

	@Autowired
	ImportService	importService;

	@Autowired
	ExtractService	extractService;

	@Override
	public void run() throws Exception {

		ConsoleAppHeader.printAppHeader("Import");
		importService.importFiles(ImportType.CMB, extractService.getFilesCMB());

	}

}
