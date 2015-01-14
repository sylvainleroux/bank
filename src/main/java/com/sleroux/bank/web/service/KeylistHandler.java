package com.sleroux.bank.web.service;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.persistence.PersistenceContext;

public class KeylistHandler extends HttpHandler {

	private ObjectMapper	mapper	= new ObjectMapper();

	@Override
	public void service(Request _request, final Response _response) throws Exception {
		PersistenceContext context = PersistenceContext.getStandardInstance();
		context.setReadOnly(true);
		context.exec(new BusinessServiceAbstract() {
			@Override
			public void run() throws Exception {
				String json = mapper.writeValueAsString(getBookDao().getBudgetKeys());
				_response.getWriter().write(json);
			}
		});
		context.finalizeContext();
		_response.setContentType("application/json");
	}
}
