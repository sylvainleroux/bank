package com.sleroux.bank.service.importer.edenred;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IExtractHistoryDao;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.domain.ImportReportFile;
import com.sleroux.bank.model.extract.ExtractDocument;
import com.sleroux.bank.model.operation.Operation;
import com.sleroux.bank.service.importer.ImportType;
import com.sleroux.bank.util.Config;

@Service
public class EndenredImportService {

	private Logger					logger	= Logger.getLogger(EndenredImportService.class);

	@Autowired
	IOperationDao					operationDao;

	@Autowired
	IExtractHistoryDao				extractHistoryDao;

	private final static ImportType	TYPE	= ImportType.EDENRED;

	private int						currentYear;
	private int						currentMonth;

	private static DateFormat		df		= new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);

	public EndenredImportService() {
		currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		currentYear = Calendar.getInstance().get(Calendar.YEAR);
	}

	public void setCurrentYear(int _currentYear) {
		currentYear = _currentYear;
	}

	public void setCurrentMonth(int _currentMonth) {
		currentMonth = _currentMonth;
	}

	@Transactional
	public void importFiles(List<String> _files, ImportReport _report) {

		Calendar cal = Calendar.getInstance();
		currentYear = cal.get(Calendar.YEAR);

		_files.forEach(f -> {
			ImportReportFile rf = new ImportReportFile();
			rf.setFilename(f);
			rf.setImportType(TYPE);

			ExtractDocument extractDocument = readFile(f);
			rf.setRawLines(extractDocument.getOperations().size());

			int newLines = 0;
			for (Operation o : extractDocument.getOperations()) {
				newLines += operationDao.insertIgnore(o);
			}
			rf.setNewLines(newLines);

			if (Config.getArchiveImportFiles()) {
				archiveFile(f);
			} else if (Config.deleteImportFile()) {
				logger.info("Delete file [" + f + "]");
				File file = new File(f);
				file.delete();
			}
			
			_report.getReportFiles().add(rf);

		});

	}

	private void archiveFile(String _f) {
		File file = new File(_f);
		String newFilename = file.getParent() + File.separator + "IMPORTED_" + file.getName();
		logger.info("Rename file to [" + newFilename + "]");
		File renameTo = new File(newFilename);
		file.renameTo(renameTo);
	}

	private ExtractDocument readFile(String _f) {

		String compte = "EDENRED.TICKET_RESTO";
		ExtractDocument report = new ExtractDocument();
		report.setFilename(_f);

		try {
			Files.readAllLines(Paths.get(_f)).forEach(line -> {
				Operation o = processLine(line, compte);
				report.addOperation(o);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return report;
	}

	protected Operation processLine(String _line, String _compte) {

		Operation operation = new Operation();

		operation.setCompte(_compte);

		String[] parts = _line.split(";");

		// Date
		String dayMonth = parts[0];
		int month = Integer.parseInt(dayMonth.substring(3));
		int yearOffset = 0;
		if (month > currentMonth) {
			yearOffset = -1;
		}
		String date = parts[0] + "/" + (currentYear + yearOffset);
		try {
			Date d = df.parse(date);
			operation.setDateOperation(d);
			operation.setDateValeur(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int index = 2;

		// Libelle
		String libelle = parts[index++];
		if (parts.length == 6) {
			libelle += parts[index++];
		}
		operation.setLibelle(libelle.trim());

		// Transaction status
		// String status = parts[index++];
		// line.status = status.trim();
		index ++;
		
		// Montant
		String montant = parts[index].trim().replaceAll(" ", "").replaceAll(",", ".");
		try {
			BigDecimal m = new BigDecimal(montant);
			if (m.compareTo(BigDecimal.ZERO) > 0) {
				operation.setCredit(m);
			} else {
				operation.setDebit(m.negate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return operation;
	}

}
