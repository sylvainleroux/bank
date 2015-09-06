package com.sleroux.bank.evo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.OperationDao;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.model.fileimport.ExtractOperation;
import com.sleroux.bank.persistence.dao.extract.ExtractDao;
import com.sleroux.bank.persistence.dao.extract.ExtractDaoImpl;
import com.sleroux.bank.persistence.storage.extract.OperationBuilder;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

public class Import extends BusinessServiceAbstract {

	private Logger			logger	= Logger.getLogger(Import.class);

	private OperationDao	operationDao;

	@Override
	public void run() throws Exception {
		ConsoleAppHeader.printAppHeader("Import");

		Connection conn = DatabaseConnection.getConnection();
		operationDao = new OperationDao(conn);

		runImportCMB();

		runImportBPO();

		System.out.println("Import compeleted");

	}

	private void runImportCMB() throws Exception {
		runImport(Config.getImportCommandCMB());
		List<String> files = getFilesCMB();

		for (String file : files) {
			logger.info("Import : " + file);
			ExtractDocument extractDocument = readFile(file);
			checkDuplicatesInFile(extractDocument);
			operationDao.insertIngnoreOperations(extractDocument);
			File f = new File(file);
			f.delete();
		}
	}

	private void runImportBPO() throws Exception {
		runImport(Config.getImportCommandBPO());

		ExtractDao extractDao = new ExtractDaoImpl();
		List<ExtractDocument> docs = extractDao.getAll();
		for (ExtractDocument extractDocument : docs) {
			logger.info("Import : " + extractDocument.getFilename());
			// Apply filters for BPO
			for (ExtractOperation o : extractDocument.getOperations()) {
				o.setLibelle(o.getLibelle() + "|" + o.getReference());
				o.setAccountID("BPO");
			}
			operationDao.insertIngnoreOperations(extractDocument);
			extractDao.markAsProcessed(extractDocument);

		}

	}

	private void checkDuplicatesInFile(ExtractDocument _doc) {
		List<String> keys = new ArrayList<>();

		for (ExtractOperation o : _doc.getOperations()) {
			int duplicateCount = 2;
			String libelle = o.getLibelle();
			while (keys.contains(o.getIndex())) {
				o.setLibelle(libelle + " DUPLICATE(" + duplicateCount++ + ")");
			}
			keys.add(o.getIndex());
		}
	}

	private void runImport(String _command) {
		File file = new File(Config.getImportCommandPath() + File.separator + _command);
		ProcessBuilder builder = new ProcessBuilder(file.getAbsolutePath());
		builder.directory(new File(Config.getImportCommandPath()));
		builder.redirectErrorStream(true);

		try {
			Process p = builder.start();
			Scanner s = new Scanner(p.getInputStream());
			StringBuilder text = new StringBuilder();
			while (s.hasNextLine()) {
				String line = s.nextLine();
				logger.info(line);

				text.append(line);
				text.append("\n");
			}
			s.close();

			int result = p.waitFor();

			logger.info("Import complete, finished with code " + result);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private List<String> getFilesCMB() {
		// Scan Downloads directory
		List<String> foundFiles = new ArrayList<String>();
		File f = new File(Config.getExtractDownloadPath());
		String[] files = f.list();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i];
			if (filename.startsWith("RELEVE_")) {
				foundFiles.add(Config.getExtractDownloadPath() + File.separator + filename);
			}
		}
		return foundFiles;
	}

	private ExtractDocument readFile(String _file) throws IOException {

		String accountNumber = "CMB";
		if (_file.contains("LB")) {
			accountNumber = "LB";
		}

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
				ExtractOperation o = OperationBuilder.createOperationCMB(nextLine, accountNumber);
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

}
