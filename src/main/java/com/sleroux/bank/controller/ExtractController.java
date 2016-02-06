package com.sleroux.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.service.ExtractService;
import com.sleroux.bank.util.Config;

@Component
public class ExtractController extends BusinessServiceAbstract {

	@Autowired
	ExtractService	extractService;

	public List<String> runExtract() {

		ConsoleAppHeader.printAppHeader("Extract");

		// Request CMB website
		extractService.runExtract(Config.getImportCommandCMB());

		// list files from download directory
		return extractService.getFilesCMB();
	}

	@Override
	public void run() throws Exception {
		runExtract();
	}

}
