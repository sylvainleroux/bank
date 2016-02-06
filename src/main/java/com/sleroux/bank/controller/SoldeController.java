package com.sleroux.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.service.SoldeService;

@Controller
public class SoldeController extends BusinessServiceAbstract {

	@Autowired
	SoldeService	soldeService;

	@Override
	@Transactional(readOnly = true)
	public void run() throws Exception {
		ConsoleAppHeader.printAppHeader("Balances");

		soldeService.run();
	}

}
