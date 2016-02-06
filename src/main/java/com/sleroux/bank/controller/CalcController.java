package com.sleroux.bank.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.service.CalcService;

@Controller
public class CalcController extends BusinessServiceAbstract {

	@Autowired
	CalcService	calcService;

	@Override
	@Transactional
	public void run() throws Exception {
		
		calcService.run();

	}

}
