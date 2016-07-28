package com.sleroux.bank.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IExtractHistoryDao;
import com.sleroux.bank.util.Config;

@Service
public class ExtractService {

	@Autowired
	IExtractHistoryDao	extractHistoryDao;

	private Logger	logger	= Logger.getLogger(this.getClass());

	public void runExtract(String _command) {
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

	public List<String> getFilesCMB() {
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

	public List<String> getFilesBPO() {
		// Scan Downloads directory
				List<String> foundFiles = new ArrayList<String>();
				File f = new File(Config.getExtractDownloadPath());
				String[] files = f.list();
				for (int i = 0; i < files.length; i++) {
					String filename = files[i];
					if (filename.startsWith("BPO")) {
						foundFiles.add(Config.getExtractDownloadPath() + File.separator + filename);
					}
				}
				return foundFiles;
	}

}
