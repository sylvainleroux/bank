package com.sleroux.bank.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.presentation.ConsoleAppHeader;
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

	@Override
	@Transactional
	public void run() throws Exception {
		ConsoleAppHeader.printLine();
		System.out.println("Last Import   : " + extractHistoryService.getFormattedImportDate());

		printNonCategorized(categoService.getNonCategorized());
		ConsoleAppHeader.printLine();
		soldeService.run();

	}

	private String printNonCategorized(int _nonCategorized) {
		if (_nonCategorized > 0) {
			System.out.println("To categorize : " + _nonCategorized + " operations");
		}

		return null;
	}

}
