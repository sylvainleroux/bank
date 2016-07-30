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

	private Logger		logger	= Logger.getLogger(this.getClass());

	public void runExtract(String _command) {
		File file = new File(Config.getImportCommandPath() + File.separator + _command);
		ProcessBuilder builder = new ProcessBuilder(file.getAbsolutePath());
		builder.directory(new File(Config.getImportCommandPath()));
		builder.redirectErrorStream(true);
		boolean first = true;
		try {
			Process p = builder.start();
			Scanner s = new Scanner(p.getInputStream());
			while (s.hasNextLine()) {

				String line = s.nextLine();
				if (line.contains("[Done]")) {
					System.out.print(" (" + line.replaceAll("\t\\[Done\\] in ", "") + ")");
				} else {
					if (first){
						first = false;
					}else{
						System.out.print("\n");
					}
					System.out.print(line);
				}

			}
			s.close();
			System.out.print("\n");

			int result = p.waitFor();

			logger.debug("Import complete, finished with code " + result);

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
			if (filename.startsWith("BPO.")) {
				foundFiles.add(Config.getExtractDownloadPath() + File.separator + filename);
			}
		}
		return foundFiles;
	}

	public List<String> getBalanceFiles() {
		List<String> foundFiles = new ArrayList<String>();
		File f = new File(Config.getExtractDownloadPath());
		String[] files = f.list();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i];
			if (filename.contains("_BALANCE")) {
				foundFiles.add(Config.getExtractDownloadPath() + File.separator + filename);
			}
		}
		return foundFiles;
	}

}
