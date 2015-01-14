package com.sleroux.bank.persistence.storage.extract;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.model.fileimport.ExtractOperation;

public class ExtractStorage {

	private Logger			logger	= Logger.getLogger(ExtractDocument.class);

	private List<ExtractDocument>	reports	= new ArrayList<ExtractDocument>();

	public List<ExtractDocument> getAll(List<String> _files) {
		for (String file : _files) {
			try {
				reports.add(readFile(file));
			} catch (FileNotFoundException e) {
				logger.error(e.getStackTrace());
				e.printStackTrace();
			} catch (IOException e) {
				logger.error(e.getStackTrace());
			}
		}
		displayReports();
		return reports;
	}

	private void displayReports() {
		for (ExtractDocument r : reports) {
			logger.info("Extracted : " + r);
		}
	}

	private ExtractDocument readFile(String _file) throws IOException {
		ExtractDocument report = new ExtractDocument();
		String fileName = _file;
		logger.info("fileName : " + fileName);
		InputStreamReader r = new InputStreamReader(new FileInputStream(fileName), "ISO-8859-1");
		CSVReader reader = new CSVReader(r, ';');
		String[] nextLine;
		boolean firstLine = true;
		while ((nextLine = reader.readNext()) != null) {
			if (firstLine) {
				firstLine = false;
				continue;
			}
			try {
				ExtractOperation o = OperationBuilder.createOperation(nextLine);
				report.addOperation(o);
			} catch (Exception e) {
				logger.error(e.getStackTrace());
				continue;
			}
		}
		report.setFilename(fileName);
		reader.close();
		return report;
	}

	public List<ExtractDocument> getReports() {
		return reports;
	}

	public void setReports(List<ExtractDocument> reports) {
		this.reports = reports;
	}

}
