package com.sleroux.bank.controller;

import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.calc.BudgetCalc;
import com.sleroux.bank.presenter.calc.ConsoleMonthBudgetPresenter;
import com.sleroux.bank.presenter.calc.MonitorInterface;
import com.sleroux.bank.service.BudgetService;
import com.sleroux.bank.service.SoldeService;
import com.sleroux.bank.service.operation.OperationService;

@Controller
public class CalcController extends BusinessServiceAbstract {

	@Autowired
	BudgetService				budgetService;

	@Autowired
	OperationService			operationService;

	@Autowired
	SoldeService				soldeService;

	private MonitorInterface	monitorInterface	= new ConsoleMonthBudgetPresenter(System.out);

	public class CaclInputParams {
		int	year;
		int	month;

		public CaclInputParams() {
			Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH) + 1;
		}
	}

	@Override
	@Transactional
	public void run() throws Exception {

		CaclInputParams pr = new CaclInputParams();

		BudgetCalc budgetCalc = new BudgetCalc();
		budgetCalc.setOperations(operationService.getOperations(pr.year, pr.month));
		budgetCalc.setBudget(budgetService.getBudget(pr.year, pr.month));
		budgetCalc.setSoldes(soldeService.getSoldes());
		
		monitorInterface.printCalc(budgetCalc);

	}

}
