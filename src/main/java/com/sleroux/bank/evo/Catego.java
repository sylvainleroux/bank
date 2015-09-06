package com.sleroux.bank.evo;

import java.util.List;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.DatabaseConnection;
import com.sleroux.bank.evo.dao.OperationDao;
import com.sleroux.bank.evo.model.Operation;
import com.sleroux.bank.presentation.ConsoleAppHeader;

public class Catego extends BusinessServiceAbstract {

	private OperationDao	operationDao;

	@Override
	public void run() throws Exception {
		ConsoleAppHeader.printAppHeader("Categorize");

		operationDao = new OperationDao(DatabaseConnection.getConnection());

		List<Operation> operations = operationDao.getNotCategorized();

		for (Operation o : operations) {
			System.out.println(o.toString());

			List<String> suggest = operationDao.getSuggestionsFor(o.getLibelle());
			for (String s : suggest) {
				System.out.println(s);
			}

		}

	}

}
