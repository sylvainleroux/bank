package com.sleroux.bank.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.service.ImportService;
import com.sleroux.bank.util.Config;

@Service
public class CMBImportService implements ImportService {

	private Logger	logger	= Logger.getLogger(CMBImportService.class);

	@Autowired
	IOperationDao	operationDao;

	@Override
	public void importFiles(List<String> _files) {

		for (String f : _files) {

			logger.info("Import file [" + f + "]");
			ExtractDocument extractDocument = null;
			try {
				extractDocument = readFile(f);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			checkDuplicatesInFile(extractDocument);

			for (Operation o : extractDocument.getOperations()) {
				operationDao.insertIgnore(o);
			}

			// Mark as processed or deleted

			if (Config.getArchiveImportFiles()) {
				archiveFile(f);
			} else if (Config.deleteImportFile()) {
				logger.info("Delete file [" + f + "]");
				File file = new File(f);
				file.delete();
			}

		}

	}

	private void archiveFile(String _f) {
		File file = new File(_f);
		String newFilename = file.getParent() + File.separator + "IMPORTED_" + file.getName();
		logger.info("Rename file to [" + newFilename + "]");
		File renameTo = new File(newFilename);
		file.renameTo(renameTo);
	}

	private ExtractDocument readFile(String _file) throws IOException {

		String accountNumber = "CMB";
		if (_file.contains("LB")) {
			accountNumber = "LB";
		}
		if (_file.contains("PEL")) {
			accountNumber = "PEL";
		}

		ExtractDocument report = new ExtractDocument();
		String fileName = _file;
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
				Operation o = createOperationCMB(nextLine, accountNumber);
				report.addOperation(o);
			} catch (Exception e) {
				logger.error(e);
				continue;
			}
		}
		report.setFilename(fileName);
		reader.close();
		return report;
	}

	protected void checkDuplicatesInFile(ExtractDocument _doc) {

		List<Integer> keys = new ArrayList<>();
		for (Operation o : _doc.getOperations()) {
			int duplicateCount = 2;
			String libelle = o.getLibelle();
			while (keys.contains(o.hashCode())) {
				o.setLibelle(libelle + " DUPLICATE(" + duplicateCount++ + ")");
			}
			keys.add(o.hashCode());
		}
	}

	public Operation createOperationCMB(String[] _nextLine, String _accountNumber) throws Exception {

		DecimalFormat formatter = new DecimalFormat("#0,00");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);

		Operation operation = new Operation();

		operation.setCompte(_accountNumber);
		operation.setDateOperation(parseDate(_nextLine[0]));
		operation.setDateValeur(parseDate(_nextLine[1]));
		operation.setLibelle(_nextLine[2]);

		if (_nextLine[3] != null && !_nextLine[3].equals("")) {
			try {
				Number d = formatter.parse(_nextLine[3]);
				BigDecimal value = new BigDecimal(d.toString());
				operation.setMontant(value.negate());
			} catch (ParseException e) {
				throw new Exception("Unable to parse operation amount for debit [" + _nextLine[3] + "|" + _nextLine[4] + "]", e);
			}
		} else if (_nextLine[4] != null) {
			try {
				Number d = formatter.parse(_nextLine[4]);
				BigDecimal value = new BigDecimal(d.toString());
				operation.setMontant(value);
			} catch (ParseException e) {
				throw new Exception("Unable to parse operation amount", e);
			}
		}

		return operation;

	}

	public Date parseDate(String string) throws Exception {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		try {
			return (Date) formatter.parse(string);
		} catch (ParseException e) {
			throw new Exception("Unable to parse date");
		}
	}

}
