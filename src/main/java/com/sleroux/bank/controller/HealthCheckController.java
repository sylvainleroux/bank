package com.sleroux.bank.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.AnalysisFact;
import com.sleroux.bank.service.AnalysisService;

@Controller
public class HealthCheckController extends BusinessServiceAbstract {

	@Autowired
	AnalysisService	analysisService;

	@Override
	@Transactional
	public void run() throws Exception {

		List<AnalysisFact> facts = analysisService.getFacts();
		for (AnalysisFact a : facts) {
			System.out.println(a);
		}

	}

}
