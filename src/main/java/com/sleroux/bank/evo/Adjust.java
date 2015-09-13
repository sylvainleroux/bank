package com.sleroux.bank.evo;

import java.util.List;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.evo.dao.BudgetDao;
import com.sleroux.bank.evo.dao.DatabaseConnection;
import com.sleroux.bank.model.budget.Update;
import com.sleroux.bank.presentation.ConsoleAppHeader;

public class Adjust extends BusinessServiceAbstract {

	@Override
	public void run() throws Exception {

		ConsoleAppHeader.printAppHeader("Adjust");

		BudgetDao dao = new BudgetDao(DatabaseConnection.getConnection());

		List<Update> list = dao.getAdded();
		for (Update b : list) {
			System.out.println(b);
		}

	}

}
