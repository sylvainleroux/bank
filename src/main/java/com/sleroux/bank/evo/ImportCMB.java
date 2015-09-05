package com.sleroux.bank.evo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.model.fileimport.ExtractOperation;
import com.sleroux.bank.persistence.storage.extract.OperationBuilder;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

public class ImportCMB extends BusinessServiceAbstract {

	private Logger	logger	= Logger.getLogger(ImportCMB.class);

	@Override
	public void run() throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Connection conn = DatabaseConnection.getConnection();

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM bank.operation");
		while (rs.next()) {
			System.out.println(rs.getInt(1));
		}

		ConsoleAppHeader.printAppHeader("IMPORT CMB");

		List<String> files = getFiles();
		for (String s : files) {
			ExtractDocument doc = readFile(s);

			for (ExtractOperation o : doc.getOperations()) {

				stmt = conn.createStatement();

				String sql = String.format(Locale.ENGLISH, "values ('%s','%s','%s','%s', '%.2f') ", o.getAccountID(),
						format.format(o.getDateOperation()), format.format(o.getDateValeur()), o.getLibelle(), o.getMontant());
				sql = "INSERT into operation (compte, date_operation, date_valeur, libelle, montant) " + sql;

				System.out.println(sql);

				stmt.executeUpdate(sql);

			}

		}

	}

	private List<String> getFiles() {
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

		String accountNumber = "CMB-CPTCHEQUE";
		if (_file.contains("LIVRET BLEU")) {
			accountNumber = "CMB-LIVRETBLEU";
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
