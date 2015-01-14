package com.sleroux.bank.web.service;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.sleroux.bank.business.app.Calc;
import com.sleroux.bank.persistence.PersistenceContext;

public class CalcHandler extends HttpHandler {

	@Override
	public void service(Request _request, Response _response) throws Exception {
		PersistenceContext context = PersistenceContext.getStandardInstance();
		context.exec(new Calc());
		context.finalizeContext();
	}
}
