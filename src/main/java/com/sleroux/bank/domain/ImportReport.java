package com.sleroux.bank.domain;

import java.util.ArrayList;
import java.util.List;

public class ImportReport {

	private List<ImportReportFile> reportFiles = new ArrayList<>();

	public List<ImportReportFile> getReportFiles() {
		return reportFiles;
	}

	public void setReportFiles(List<ImportReportFile> _reportFiles) {
		reportFiles = _reportFiles;
	}

	public int getNbLines() {
		return reportFiles.stream().mapToInt(r -> r.getRawLines()).sum();
	}

	@Override
	public String toString() {
		return "ImportReport [reportFiles=" + reportFiles + "]";
	}

	public Object getNewLines() {
		return reportFiles.stream().mapToInt(r -> r.getNewLines()).sum();
	}

}
