package com.sleroux.bank.web.service;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.budget.BugdetMonth;
import com.sleroux.bank.persistence.PersistenceContext;

public class TimelineHandler extends HttpHandler {

	private ObjectMapper	mapper	= new ObjectMapper();

	@Override
	public void service(final Request _request, Response _response) throws Exception {
		PersistenceContext context = PersistenceContext.getStandardInstance();
		context.setReadOnly(true);
		final List<BugdetMonth> result = new ArrayList<>();

		context.exec(new BusinessServiceAbstract() {
			@Override
			public void run() throws Exception {
				// Decode Parameters
				int documentFirstYear = getBookDao().getBook().getFirstYear();
				int firstYear = documentFirstYear;
				int firstMonth = 1;
				int length = 12;

				if (_request.getParameterNames().contains("firstYear")) {
					firstYear = Integer.parseInt(_request.getParameter("firstYear"));
					if (firstYear < documentFirstYear) {
						firstYear = documentFirstYear;
					}
				}

				if (_request.getParameterNames().contains("firstMonth")) {
					firstMonth = Integer.parseInt(_request.getParameter("firstMonth"));
					if (firstMonth < 1) {
						firstMonth = 1;
					}
					if (firstMonth > 12) {
						firstMonth = 12;
					}
				}

				if (_request.getParameterNames().contains("length")) {
					length = Integer.parseInt(_request.getParameter("length"));
					if (length < 0) {
						length = 12;
					}
				}

				System.out.println("firstYear : " + firstYear);
				System.out.println("firstMonth : " + firstMonth);
				System.out.println("length : " + length);

				BugdetMonth monthBudget;

				for (int year = firstYear; length > 0; year++) {
					for (int month = firstMonth; month < 13 && length > 0; month++) {
						// reset firstMonth Value for next Year
						firstMonth = 1;
						monthBudget = getBookDao().getBudgetByYearMonth(year, month);
						if (monthBudget != null) {
							result.add(monthBudget);
						}
						--length;
					}
				}
			}
		});
		_response.getWriter().write(mapper.writeValueAsString(result));
		_response.setContentType("application/json");
	}
}
