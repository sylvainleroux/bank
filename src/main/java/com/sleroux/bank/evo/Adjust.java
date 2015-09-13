package com.sleroux.bank.evo;

import java.util.List;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.BudgetDao;
import com.sleroux.bank.evo.dao.DatabaseConnection;
import com.sleroux.bank.model.budget.Changes;
import com.sleroux.bank.presentation.ConsoleAppHeader;

public class Adjust extends BusinessServiceAbstract {

	@Override
	public void run() throws Exception {

		ConsoleAppHeader.printAppHeader("Adjust");

		BudgetDao dao = new BudgetDao(DatabaseConnection.getConnection());

		List<Changes> list = dao.getAdded();
		for (Changes b : list) {
			System.out.println(b);
		}

	}

}
