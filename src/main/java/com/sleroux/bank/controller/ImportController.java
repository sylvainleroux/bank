package com.sleroux.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.service.ExtractService;
import com.sleroux.bank.service.ImportType;
import com.sleroux.bank.service.impl.CMBImportService;

@Component
public class ImportController extends BusinessServiceAbstract {

	@Autowired
	ApplicationContext	applicationContext;

	@Autowired
	ExtractService		extractService;

	public void importFiles(ImportType _cmb, List<String> _files) {

		if (_cmb == ImportType.CMB) {
			applicationContext.getBean(CMBImportService.class).importFiles(_files);
		}

	}

	@Override
	public void run() throws Exception {

		ConsoleAppHeader.printAppHeader("Import");
		importFiles(ImportType.CMB, extractService.getFilesCMB());

	}

}
