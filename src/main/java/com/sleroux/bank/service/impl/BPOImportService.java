package com.sleroux.bank.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.dao.IExtractHistoryDao;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.domain.ImportReportFile;
import com.sleroux.bank.model.ExtractHistory;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.service.ImportType;
import com.sleroux.bank.util.Config;

@Service
public class BPOImportService {

	private final static ImportType	IMPORT_TYPE	= ImportType.BPO;

	private Logger					logger		= Logger.getLogger(BPOImportService.class);

	@Autowired
	IOperationDao					operationDao;

	@Autowired
	IExtractHistoryDao				extractHistoryDao;

	private ObjectMapper			mapper		= new ObjectMapper();

	@Transactional
	public void importFiles(List<String> _files, ImportReport _report) {

		for (String f : _files) {

			ImportReportFile rf = new ImportReportFile();
			rf.setImportType(IMPORT_TYPE.toString());
			rf.setFilename(f);
			_report.getReportFiles().add(rf);

			logger.info("Import file [" + f + "]");
			ExtractDocument extractDocument = null;
			try {
				extractDocument = readFile(f);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			checkDuplicatesInFile(extractDocument);

			rf.setRawLines(extractDocument.getOperations().size());

			int newLines = 0;
			for (Operation o : extractDocument.getOperations()) {
				newLines += operationDao.insertIgnore(o);
			}
			rf.setNewLines(newLines);

			// Mark as processed or deleted

			if (Config.getArchiveImportFiles()) {
				archiveFile(f);
			} else if (Config.deleteImportFile()) {
				logger.info("Delete file [" + f + "]");
				File file = new File(f);
				file.delete();
			}
		}

		if (_report.getNbLines() > 0) {
			createHistoryEntry();
		}

	}

	private void createHistoryEntry() {
		ExtractHistory extractHistory = new ExtractHistory();
		extractHistory.setExtractDate(new Date());
		extractHistoryDao.create(extractHistory);
	}

	private void archiveFile(String _f) {
		File file = new File(_f);
		String newFilename = file.getParent() + File.separator + "IMPORTED_" + file.getName();
		logger.info("Rename file to [" + newFilename + "]");
		File renameTo = new File(newFilename);
		file.renameTo(renameTo);
	}

	private ExtractDocument readFile(String _file) throws Exception {

		String accountNumber = "BPO";

		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		mapper.setDateFormat(df);
		BPOReport bpo = mapper.readValue(new File(_file), BPOReport.class);

		ExtractDocument report = new ExtractDocument();
		for (BPOOperation bpoo : bpo.getContent()) {
			Operation o = createOperationBPO(bpoo, accountNumber);
			report.addOperation(o);
		}
		report.setFilename(_file);
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

	public Operation createOperationBPO(BPOOperation op, String _accountNumber) throws Exception {

		DecimalFormat formatter = new DecimalFormat("#0,00");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);

		Operation operation = new Operation();

		operation.setCompte(_accountNumber);
		operation.setDateOperation(op.getDateOperation());
		operation.setDateValeur(op.getDateValeur());
		operation.setLibelle(op.getLibelle() + " " + op.getRef());

		if (op.getCredit() != null) {

			operation.setCredit(op.getCredit());
		}
		if (op.getDebit() != null) {
			operation.setDebit(op.getDebit());
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
