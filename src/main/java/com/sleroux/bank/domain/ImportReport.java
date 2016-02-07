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

	public int getNbLines() {
		int nbLines = 0;
		for (ImportReportFile rf : reportFiles) {
			nbLines += rf.getRawLines();
		}
		return nbLines;
	}

}
