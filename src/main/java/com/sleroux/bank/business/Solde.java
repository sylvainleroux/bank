package com.sleroux.bank.business;

import java.util.List;

import com.sleroux.bank.evo.dao.CalcDao;
import com.sleroux.bank.evo.dao.DatabaseConnection;
import com.sleroux.bank.evo.model.Balance;
import com.sleroux.bank.presentation.BalancesPresenter;

public class Solde extends BusinessServiceAbstract {
	private CalcDao				calcDao;
	private BalancesPresenter	balancesPresenter	= new BalancesPresenter();

	public void run() throws Exception {
		calcDao = new CalcDao(DatabaseConnection.getConnection());

		List<Balance> balances = calcDao.getBalances();
		balancesPresenter.printBalances(balances);
	}

}
