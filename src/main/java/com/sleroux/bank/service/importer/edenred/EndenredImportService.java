package com.sleroux.bank.service.importer.edenred;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.dao.IExtractHistoryDao;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.domain.ImportReportFile;
import com.sleroux.bank.model.extract.JSONExtract;
import com.sleroux.bank.model.operation.Operation;
import com.sleroux.bank.service.importer.ImportType;
import com.sleroux.bank.util.Config;

@Service
public class EndenredImportService {

	private Logger					logger			= LogManager.getLogger(EndenredImportService.class);

	@Autowired
	IOperationDao					operationDao;

	@Autowired
	IExtractHistoryDao				extractHistoryDao;

	private final static ImportType	TYPE			= ImportType.EDENRED;

	private int						currentYear;
	private int						currentMonth;

	private static DateFormat		df				= new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);

	private ObjectMapper			objectMapper	= new ObjectMapper();

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

		/* Edenred extract generating JSON Files */

		_files.forEach(f -> {
			ImportReportFile importReportFile = new ImportReportFile();
			importReportFile.setFilename(f);
			importReportFile.setImportType(TYPE);

			try {
				objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

				JSONExtract jsonExtract = objectMapper.readValue(new File(f), JSONExtract.class);

				int newLines = 0;
				for (Operation o : jsonExtract.getOperations()) {
					System.out.println(o);
					newLines += operationDao.insertIgnore(o);
				}
				importReportFile.setNewLines(newLines);
				importReportFile.setRawLines(jsonExtract.getOperations().size());

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (Config.getArchiveImportFiles()) {
				archiveFile(f);
			} else if (Config.deleteImportFile()) {
				logger.info("Delete file [" + f + "]");
				File file = new File(f);
				file.delete();
			}

			_report.getReportFiles().add(importReportFile);

		});

	}

	private void archiveFile(String _f) {
		File file = new File(_f);
		String newFilename = file.getParent() + File.separator + "IMPORTED_" + file.getName();
		logger.info("Rename file to [" + newFilename + "]");
		File renameTo = new File(newFilename);
		file.renameTo(renameTo);
	}

}
