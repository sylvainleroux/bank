package com.sleroux.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.service.impl.CMBImportService;

@Service
public class ImportService {

	@Autowired
	ApplicationContext	ctx;

	public ImportReport importFiles(ImportType _cmb, List<String> _files) {

		ImportReport report = new ImportReport();

		if (_cmb == ImportType.CMB) {
			ctx.getBean(CMBImportService.class).importFiles(_files, report);
		}

		return report;

	}

}
