package com.sleroux.bank.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.presenter.common.ConsoleAppHeader;
import com.sleroux.bank.service.CategoService;

@Controller
public class CategoController extends BusinessServiceAbstract {

	@Autowired
	CategoService	categoService;

	@Override
	@Transactional
	public void run() throws Exception {
		
		ConsoleAppHeader.printAppHeader("Catego");
		
		categoService.run();
	}

}