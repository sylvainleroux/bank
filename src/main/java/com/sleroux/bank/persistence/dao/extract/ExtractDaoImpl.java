package com.sleroux.bank.persistence.dao.extract;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sleroux.bank.model.fileimport.ExtractDocument;
import com.sleroux.bank.persistence.storage.extract.ExtractStorage;
import com.sleroux.bank.util.Config;

public class ExtractDaoImpl implements ExtractDao {

	private ExtractStorage	extractor	= new ExtractStorage();

	@Override
	public List<ExtractDocument> getAll() {
		// Get file list
		List<String> files = getFiles();
		return extractor.getAll(files);
	}

	private List<String> getFiles() {
		// Scan Downloads directory
		List<String> foundFiles = new ArrayList<String>();
		File f = new File(Config.getExtractDownloadPath());
		String[] files = f.list();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i];
			if (filename.startsWith("CyberPlus_OP")) {
				foundFiles.add(Config.getExtractDownloadPath() + File.separator + filename);
			}
		}
		return foundFiles;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private void renameFiles(List<String> files) {
		for (String s : files) {
			File f = new File(s);
			System.out.println("rename file: " + f.getName());
			String newName = "Imported_" + f.getName();
			File newFile = new File(Config.getExtractDownloadPath() + File.separator + newName);
			f.renameTo(newFile);
		}
	}

	private void eraseFiles(List<String> files) {
		for (String s : files) {
			File f = new File(s);
			System.out.println("Delete file: " + f.getName());
			// check
			if (f.getName().startsWith("CyberPlus_OP")) {
				f.delete();
			}
		}
	}

	@Override
	public void markAsProcessed(ExtractDocument _extract) {
		List<String> files = getFiles();
		if (!files.contains(_extract.getFilename())) {
			System.err.println("Unable to mark file as processed :" + _extract.getFilename());
			return;
		}
		// renameFiles(Arrays.asList(_extract.getFilename()));
		eraseFiles(Arrays.asList(_extract.getFilename()));
	}
}
