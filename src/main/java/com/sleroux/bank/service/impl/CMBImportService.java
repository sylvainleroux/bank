package com.sleroux.bank.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CMBImportService {

	private final static ImportType	IMPORT_TYPE	= ImportType.CMB;

	private Logger					logger		= Logger.getLogger(CMBImportService.class);

	@Autowired
	IOperationDao					operationDao;

	@Autowired
	IExtractHistoryDao				extractHistoryDao;

	@Transactional
	public void importFiles(List<String> _files, ImportReport _report) {

		for (String f : _files) {

			ImportReportFile rf = new ImportReportFile();
			rf.setImportType(IMPORT_TYPE.toString());
			rf.setFilename(f);
			_report.getReportFiles().add(rf);

			logger.info("Import file [" + f + "]");
			ExtractDocument extractDocument = null;
			// extractDocument = readFile(f);
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

}
