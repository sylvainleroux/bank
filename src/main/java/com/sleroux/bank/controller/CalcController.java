package com.sleroux.bank.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.service.CalcService;

@Lazy
@Controller
public class CalcController extends AbstractController {

	@Autowired
	CalcService calcService;

	@Override
	@Transactional
	public void run() throws Exception {

		calcService.run();

	}

}
