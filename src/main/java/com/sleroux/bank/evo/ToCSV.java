package com.sleroux.bank.evo;

import java.io.FileWriter;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import au.com.bytecode.opencsv.CSVWriter;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;

public class ToCSV extends BusinessServiceAbstract {

	@Override
	public void run() throws Exception {

		CSVWriter writer = new CSVWriter(new FileWriter("/Users/sleroux/Desktop/data.csv"), '\t', '\'', '\\');

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Book book = getBookDao().getBook();

		Date prevOp = null;
		Date prevVal = null;

		for (Year year : book.getYears()) {

			for (Operation operation : year.getOperations()) {

				String[] list = new String[9];
				list[0] = "BPO-10619827116";

				if (operation.getDateOperation() != null) {
					prevOp = operation.getDateOperation();
					list[1] = formatter.format(operation.getDateOperation());
				} else {
					if (prevOp != null) {
						list[1] = formatter.format(prevOp);
					}
				}

				if (operation.getDateValeur() != null) {
					prevVal = operation.getDateValeur();
					list[2] = formatter.format(operation.getDateValeur());
					;
				} else {
					if (prevVal != null) {
						list[2] = formatter.format(prevVal);

					}
				}
				if (operation.getLibelle() != null) {
					list[3] = operation.getLibelle();
				}

				if (operation.getMontant() != null) {
					list[4] = operation.getMontant().setScale(2, RoundingMode.HALF_UP).toString();
				}
				;

				list[5] = operation.getCatego();

				list[6] = year.getYear()+"";
				
				list[7] = operation.getMonth()+"";

				list[8] = operation.getMonthAdjusted()+"";

				writer.writeNext(list);

			}

		}

		writer.close();

	}

}
