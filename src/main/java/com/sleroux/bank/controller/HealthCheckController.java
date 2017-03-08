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
		if (facts.size() == 0){
			System.out.println("Everything looks good!");
		}
		for (AnalysisFact a : facts) {

			System.out.println("-----------------------------------------");
			System.out.println(a.getReason());

			if (a.getReason() == AlertType.DEBIT_OVER_ESTIMATE) {

				System.out.println("Reduce debit " + a.getCatego() + ":" + a.getYear() + "/" + a.getMonth() + " from "
						+ a.getDebit_bud() + " to " + a.getDebit_ops() + " ?");

				if (validate(a)) {
					budgetService.createUpdateDebit(a);
				}
			}

			if (a.getReason() == AlertType.CREDIT_OVER_ESTIMATE) {

				System.out.println("Reduce credit " + a.getCatego() + ":" + a.getYear() + "/" + a.getMonth() + " from "
						+ a.getCredit_bud() + " to " + a.getCredit_ops() + " ?");

				if (validate(a)) {
					budgetService.createUpdateCredit(a);
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

				if (validate(a)) {
					budgetService.createUpdateDebit(a);
				}
			}

			if (a.getReason() == AlertType.CREDIT_NOT_BUDGETED) {
				System.out.println("Create missing budget of " + a.getCredit_ops() + " for catego " + a.getCatego()
						+ " : " + a.getYear() + "/" + a.getMonth() + " ?");

				if (validate(a)) {
					budgetService.createUpdateCredit(a);
				}
			}

			if (a.getReason() == AlertType.DEBIT_BURNED) {
				System.out.println("Increase " + a.getCatego() + ":" + a.getYear() + "/" + a.getMonth()
						+ " budget from " + a.getDebit_bud() + " to " + a.getDebit_ops() + " ?");

				if (validate(a)) {
					budgetService.createUpdateDebit(a);
				}
			}

			if (a.getReason() == AlertType.CREDIT_BURNED) {
				System.out.println("Increase budget from " + a.getCredit_bud() + " to " + a.getCredit_ops() + " ?");
				System.out.println("  " + a.getCatego() + " : " + a.getYear() + "/" + a.getMonth());

				if (validate(a)) {
					budgetService.createUpdateCredit(a);
				}
			}
			

		}

	}

	private boolean validate(AnalysisFact _fact) {
		Request r = new Request();
		while (true) {
			while (request(r))
				;
			if (r.action == RequestAction.LIST_OPERATIONS) {
				System.out.println("  Corresponding operations : ");
				List<Operation> ops = operationDao.findByCategoYearMonth(_fact.getYear(), _fact.getMonth(),
						_fact.getCatego());
				for (Operation o : ops) {
					System.out.println("  " + OperationFormater.toString(o));
				}
			} else {
				return r.action == RequestAction.ACCEPT;
			}
		}
	}

	private boolean request(Request _r) {
		System.out.print("y/[n]/l>");
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();

			if (s.equals("y") || s.equals("Y")) {
				_r.action = RequestAction.ACCEPT;
			}

			if (s.equals("l") || s.equals("L")) {
				_r.action = RequestAction.LIST_OPERATIONS;
			}

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public class Request {

		public RequestAction action = RequestAction.REJECT;

	}

	public enum RequestAction {
		ACCEPT, LIST_OPERATIONS, REJECT
	}
}
