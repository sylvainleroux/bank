package com.sleroux.bank.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.domain.AlertType;
import com.sleroux.bank.model.AnalysisFact;
import com.sleroux.bank.model.Operation;
import com.sleroux.bank.service.AnalysisService;
import com.sleroux.bank.service.BudgetService;
import com.sleroux.bank.util.formats.OperationFormater;

@Controller
public class HealthCheckController extends BusinessServiceAbstract {

	@Autowired
	AnalysisService analysisService;

	@Autowired
	BudgetService budgetService;

	@Autowired
	IOperationDao operationDao;

	@Override
	@Transactional
	public void run() throws Exception {

		Calendar c = Calendar.getInstance();

		List<AnalysisFact> facts = analysisService.getFacts(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
		for (AnalysisFact a : facts) {

			System.out.println("-----------------------------------------");
			System.out.println(a.getReason());

			if (a.getReason() == AlertType.DEBIT_OVER_ESTIMATE) {

				System.out.println("Reduce debit " + a.getCatego() + ":" + a.getYear() + "/" + a.getMonth() + " from "
						+ a.getDebit_bud() + " to " + a.getDebit_ops() + " ?");

				if (validate()) {
					budgetService.updateDebit(a);
				}
			}

			if (a.getReason() == AlertType.CREDIT_OVER_ESTIMATE) {

				System.out.println("Reduce credit " + a.getCatego() + ":" + a.getYear() + "/" + a.getMonth() + " from "
						+ a.getCredit_bud() + " to " + a.getCredit_ops() + " ?");

				if (validate()) {
					budgetService.updateCredit(a);
				}

			}

			if (a.getReason() == AlertType.DEBIT_NOT_BUDGETED) {

				System.out.println("Create missing budget of " + a.getDebit_ops() + " for catego " + a.getCatego()
						+ " : " + a.getYear() + "/" + a.getMonth() + " ?");
				System.out.println("  Corresponding operations : ");

				List<Operation> ops = operationDao.findByCategoYearMonth(a.getYear(), a.getMonth(), a.getCatego());
				for (Operation o : ops) {
					System.out.println("  " + OperationFormater.toString(o));
				}

				if (validate()) {
					budgetService.createDebitBudgetFor(a);
				}
			}

			if (a.getReason() == AlertType.CREDIT_NOT_BUDGETED) {
				System.out.println("Create missing budget of " + a.getCredit_ops() + " for catego " + a.getCatego()
						+ " : " + a.getYear() + "/" + a.getMonth() + " ?");
				System.out.println("  Corresponding operations : ");

				List<Operation> ops = operationDao.findByCategoYearMonth(a.getYear(), a.getMonth(), a.getCatego());
				for (Operation o : ops) {
					System.out.println("  " + OperationFormater.toString(o));
				}

				if (validate()) {
					budgetService.creatCreditBudgetFor(a);
				}
			}

			if (a.getReason() == AlertType.DEBIT_BURNED) {
				System.out.println("Increase " + a.getCatego() + ":" + a.getYear() + "/" + a.getMonth()
						+ " budget from " + a.getDebit_bud() + " to " + a.getDebit_ops() + " ?");

				if (validate()) {
					budgetService.updateDebit(a);
				}
			}

			if (a.getReason() == AlertType.CREDIT_BURNED) {
				System.out.println("Increase budget from " + a.getCredit_bud() + " to " + a.getCredit_ops() + " ?");
				System.out.println("  " + a.getCatego() + " : " + a.getYear() + "/" + a.getMonth());

				if (validate()) {
					budgetService.updateCredit(a);
				}
			}

		}

	}

	private boolean validate() {
		Request r = new Request();
		while (request(r))
			;
		return r.accept;
	}

	private boolean request(Request _r) {
		System.out.print("y/[n]>");
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();

			if (s.equals("y") || s.equals("Y")) {
				_r.accept = true;
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public class Request {
		public boolean accept = false;

	}
}
