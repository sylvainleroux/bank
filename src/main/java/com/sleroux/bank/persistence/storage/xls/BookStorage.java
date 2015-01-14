package com.sleroux.bank.persistence.storage.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import com.sleroux.bank.util.Config;
import com.sleroux.bank.util.CopyFile;

public class BookStorage {

	private static BookStorage	instance;
	//
	public final static int		REPORT_COL_COMPTE					= 0;
	public final static int		REPORT_COL_DATE_COMPTA				= 1;
	public final static int		REPORT_COL_DATE_OPERATION			= 2;
	public final static int		REPORT_COL_LIBELLE					= 3;
	public final static int		REPORT_COL_REFERENCE				= 4;
	public final static int		REPORT_COL_DATE_VALEUR				= 5;
	public final static int		REPORT_COL_MONTANT					= 6;
	public static final int		REPORT_COL_CATEGO					= 7;
	public static final int		REPORT_COL_NEW_MONTH_BANK			= 8;
	public static final int		REPORT_COL_NEW_MONTH_BANK_REPORT	= 9;
	public static final int		REPORT_COL_NEW_MONTH_FIXED			= 10;
	public static final int		REPORT_COL_NEW_MONTH_FIXED_REPORT	= 11;
	//
	private Workbook			wb;
	private File				original;
	//
	private boolean				updated								= false;
	//
	private Logger				logger								= Logger.getLogger(BookStorage.class);

	public static BookStorage getInstance() {
		if (instance == null) {
			instance = new BookStorage();
		}

		return instance;
	}

	public BookStorage() {
		String filename = Config.getMainDocumentPath() + File.separator + Config.getMainDocumentName();
		logger.info("Use file : " + filename);
		original = new File(filename);
		try {
			wb = new HSSFWorkbook(new FileInputStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (wb == null) {
			System.err.println("Workbook is null");
			return;
		}
	}

	public static void closeInstanceIfExits() {
		if (instance == null)
			return;
		instance.close();
	}

	private void close() {
		logger.info("Start closing book");
		if (!updated) {
			logger.info("Nothing to update");
			return;
		}
		logger.info("Write book");
		// Make a dated backup
		File backupDir = new File(Config.getMainDocumentPath() + File.separator + Config.getMainDocumentBackupPath());
		if (!backupDir.exists())
			backupDir.mkdir();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = f.format(Calendar.getInstance().getTime());
		String bakcupFileName = Config.getMainDocumentPath() + File.separator + Config.getMainDocumentBackupPath() + File.separator + date
				+ "_" + Config.getMainDocumentName();
		File backupFile = new File(bakcupFileName);
		CopyFile.copyfile(original, backupFile);
		logger.info("Backuped file into " + bakcupFileName);
		try {
			wb.write(new FileOutputStream(Config.getMainDocumentPath() + File.separator + Config.getMainDocumentName()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Workbook getWorkBook() {
		return wb;
	}

	public void setUpdated(boolean _b) {
		updated = _b;
	}

}
