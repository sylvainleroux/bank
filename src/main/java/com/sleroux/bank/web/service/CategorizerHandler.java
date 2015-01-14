package com.sleroux.bank.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.business.app.Catego;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.persistence.PersistenceContext;
import com.sleroux.bank.web.domain.CORecords;

public class CategorizerHandler extends HttpHandler {

	private ObjectMapper	mapper	= new ObjectMapper();

	@SuppressWarnings("unused")
	private ExecutorService	executorService;

	public CategorizerHandler(ExecutorService _executorService) {
		executorService = _executorService;
	}

	@Override
	public void service(Request _request, final Response _response) throws Exception {
		PersistenceContext context = PersistenceContext.getStandardInstance();
		if (_request.getParameter("id") == null) {
			// Retrieve all non categorized lines
			context.exec(new BusinessServiceAbstract() {
				@Override
				public void run() throws Exception {
					final List<Operation> nonCategorizedOperations = new ArrayList<>();
					final CORecords records = new CORecords();
					Catego categoApp = new Catego();
					Book book = getBookDao().getBook();
					Year lastYear = book.getYears().get(book.getYears().size() - 1);
					for (Year currentYear : book.getYears()) {
						for (Operation op : currentYear.getOperations()) {
							String catego = op.getCatego();
							// Do not use the line if the value is 0 or null
							if (currentYear.getYear() == lastYear.getYear() && (catego == null || catego.equals(""))) {
								if (op.getMontant() == null || op.getMontant().doubleValue() == 0)
									continue;
								// Try to set a catego
								categoApp.searchCatego(op);
								// Found a not categorized operation
								nonCategorizedOperations.add(op);
							} else {
								categoApp.addEntry(op.getLibelle(), op.getCatego());
							}
						}
					}
					records.getOperations().addAll(nonCategorizedOperations);
					records.getCategories().addAll(Catego.getCategories());
					_response.getWriter().write(mapper.writeValueAsString(records));
					// getBookDao().writeReports(book);
				}
			});
		} else {
			final String id = _request.getParameter("id");
			final String catego = _request.getParameter("catego");
			context.exec(new BusinessServiceAbstract() {
				@Override
				public void run() throws Exception {
					Book book = getBookDao().getBook();
					Year lastYear = book.getYears().get(book.getYears().size() - 1);
					for (Operation operation : lastYear.getOperations()) {
						if (operation.getHash().equals(id)) {
							operation.setCatego(catego);
						}
					}
					getBookDao().saveBook(book);
				}
			});
		}
		context.finalizeContext();
		_response.setContentType("application/json");
	}
}
