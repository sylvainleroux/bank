package com.sleroux.bank.presenter;

import org.junit.Test;

import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.domain.ImportReportFile;
import com.sleroux.bank.presentation.ImportReportPresenter;

public class TestImportReportPresenter {

	@Test
	public void testEmptyImportReportPresenter() {

		ImportReport report = new ImportReport();

		ImportReportPresenter.displayReport(report);

	}

	@Test
	public void testImportReportPresenter() {

		ImportReport report = new ImportReport();
		
		ImportReportFile f = new ImportReportFile();
		f.setFilename("CMB_Import");
		f.setRawLines(10);
		f.setNewLines(1);
		f.setImportType("CMB");
		
		report.getReportFiles().add(f);
		
		ImportReportPresenter.displayReport(report);

	}
}