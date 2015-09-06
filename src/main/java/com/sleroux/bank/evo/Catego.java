package com.sleroux.bank.evo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.DatabaseConnection;
import com.sleroux.bank.evo.dao.OperationDao;
import com.sleroux.bank.evo.model.Operation;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.formats.OperationFormater;

public class Catego extends BusinessServiceAbstract {

	private OperationDao	operationDao;

	@Override
	public void run() throws Exception {
		ConsoleAppHeader.printAppHeader("Categorize");

		operationDao = new OperationDao(DatabaseConnection.getConnection());

		List<Operation> operations = operationDao.getNotCategorized();

		for (Operation o : operations) {
			System.out.println(OperationFormater.toStringLight(o));

			List<String> suggest = operationDao.getSuggestionsFor(o.getLibelle());
			System.out.println("[Enter:" + suggest.get(0) + "], [m] for more suggestions, or type catego");
			while (categoRequests(o, suggest))
				;

			Calendar c = Calendar.getInstance();
			c.setTime(o.getDateValeur());

			int opYear = c.get(Calendar.YEAR);
			int opMonth = c.get(Calendar.MONTH) + 1;

			// Shift salaire
			if (o.getCatego().equals("SALAIRE")) {
				if (c.get(Calendar.DAY_OF_MONTH) >= 20) {
					opMonth++;
					if (opMonth > 12) {
						opMonth = 1;
						opYear++;
					}
				}
			}

			o.setYear(opYear);
			o.setMonthAdjusted(opMonth);

			operationDao.saveOperation(o);

			ConsoleAppHeader.printLine();

		}

	}

	private boolean categoRequests(Operation _op, List<String> _suggest) {
		System.out.print(">");
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();

			if (s.equals("")) {
				_op.setCatego(_suggest.get(0));
			} else if (s.equals("m")) {
				System.out.println("display more");
				for (int i = 1; i < _suggest.size(); i++) {
					System.out.println("- " + _suggest.get(i));
				}
				return true;
			} else {
				// TODO more validation
				_op.setCatego(s);
			}

			System.out.println("Apply category : " + _op.getCatego());
			return false;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
