package com.sleroux.bank.domain;

import com.sleroux.bank.service.importer.ImportType;

public class ImportReportFile {

	private ImportType	importType	= null;

	private String		filename	= "UNDEFINED";

	private int			rawLines;

	private int			newLines;

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

	public ImportType getImportType() {
		return importType;
	}

	public void setImportType(ImportType _importType) {
		importType = _importType;
	}

	@Override
	public String toString() {
		return "ImportReportFile [importType=" + importType + ", filename=" + filename + ", rawLines=" + rawLines
				+ ", newLines=" + newLines + "]";
	}

	
}
