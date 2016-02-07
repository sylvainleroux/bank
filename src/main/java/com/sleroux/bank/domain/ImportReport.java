package com.sleroux.bank.domain;

import java.util.ArrayList;
import java.util.List;

public class ImportReport {

	private List<ImportReportFile>	reportFiles	= new ArrayList<>();

	public List<ImportReportFile> getReportFiles() {
		return reportFiles;
	}

	public void setReportFiles(List<ImportReportFile> _reportFiles) {
		reportFiles = _reportFiles;
	}


}
