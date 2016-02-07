package com.sleroux.bank.presentation;

import com.sleroux.bank.domain.ImportReport;
import com.sleroux.bank.domain.ImportReportFile;

public class ImportReportPresenter {

	public static void displayReport(ImportReport _report) {
		
		int nbFiles = _report.getReportFiles().size();
			
		for (int i = 0 ; i < nbFiles; i++){
			ImportReportFile rf = _report.getReportFiles().get(i);
			System.out.printf("%2d lines, %2d new in [%s]%n", rf.getRawLines(), rf.getNewLines(), rf.getFilename());
		} 
		
		if( nbFiles == 0){
			System.out.println("No file imported");
		}
		

	}

}
