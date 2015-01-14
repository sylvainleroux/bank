package com.sleroux.bank.web.service;

import java.util.concurrent.ExecutorService;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.business.app.Calc;
import com.sleroux.bank.model.budget.BudgetKeys;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.model.calc.Month;
import com.sleroux.bank.model.calc.MonthAdjusted;
import com.sleroux.bank.persistence.PersistenceContext;
import com.sleroux.bank.presentation.MonitorInterface;
import com.sleroux.bank.web.domain.COBudget;

public class BudgetHandler extends HttpHandler {

	private ObjectMapper	mapper	= new ObjectMapper();

	@SuppressWarnings("unused")
	private ExecutorService	executorService;

	public BudgetHandler(ExecutorService _executorService) {
		executorService = _executorService;
	}

	@Override
	public void service(Request _request, Response _response) throws Exception {
		PersistenceContext context = PersistenceContext.getStandardInstance();
		context.setReadOnly(true);
		final COBudget budget = new COBudget();
		Calc calc = new Calc();
		calc.setMonitorInterface(new MonitorInterface() {
			@Override
			public void print(MonthAdjusted _monthAdjusted, Month _month, BugdetMonth _monthBudget, BudgetKeys _keys) {
				budget.setMonthAdjusted(_monthAdjusted);
				budget.setMonth(_month);
				budget.setMonthBudget(_monthBudget);
				budget.setKeyList(_keys);
			}
		});
		context.exec(calc);
		_response.getWriter().write(mapper.writeValueAsString(budget));
		_response.setContentType("application/json");
		context.finalizeContext();
	}
}
