package com.sleroux.bank.controller;

import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.service.AnalysisService;
import com.sleroux.bank.service.CategoService;
import com.sleroux.bank.service.ExtractHistoryService;
import com.sleroux.bank.service.SoldeService;

@Controller
public class SummaryController extends BusinessServiceAbstract {

	@Autowired
	ExtractHistoryService	extractHistoryService;

	@Autowired
	SoldeService			soldeService;

	@Autowired
	CategoService			categoService;

	@Autowired
	AnalysisService			analysisService;

	@Override
	@Transactional
	public void run() throws Exception {
		ConsoleAppHeader.printLine();
		System.out.println("Last Import   : " + extractHistoryService.getFormattedImportDate());

		printNonCategorized(categoService.getNonCategorized());
		printHealthCheck();
		ConsoleAppHeader.printLine();
		soldeService.run();

	}

	private void printHealthCheck() {

		Calendar c = Calendar.getInstance();

		int nbFacts = analysisService.getNbFacts(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);
		String status = "OK";
		if (nbFacts > 0) {
			status = nbFacts + " alerts";
		}

		System.out.println("Health check  : " + status);
	}

	private String printNonCategorized(int _nonCategorized) {
		if (_nonCategorized > 0) {
			System.out.println("To categorize : " + _nonCategorized + " operations");
		}

		return null;
	}

}
