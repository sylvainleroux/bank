package com.sleroux.bank.domain;

public class ImportReportFile {

	private String	importType	= "UNDEFINED";

	private String	filename = "UNDEFINED";

	private int		rawLines;

	private int		newLines;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String _filename) {
		filename = _filename;
	}

	public int getRawLines() {
		return rawLines;
	}

	public void setRawLines(int _rawLines) {
		rawLines = _rawLines;
	}

	public int getNewLines() {
		return newLines;
	}

	public void setNewLines(int _newLines) {
		newLines = _newLines;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String _importType) {
		importType = _importType;
	}

}
