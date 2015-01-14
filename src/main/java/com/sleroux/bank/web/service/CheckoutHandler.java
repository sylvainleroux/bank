package com.sleroux.bank.web.service;

import java.util.concurrent.ExecutorService;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.web.domain.COCheckOutStatus;
import com.sleroux.bank.web.util.CheckoutThread;
import com.sleroux.bank.web.util.Storage;

public class CheckoutHandler extends HttpHandler {

	private static Storage	storage;
	private ObjectMapper	mapper	= new ObjectMapper();
	private ExecutorService	executorService;

	public CheckoutHandler(ExecutorService _executorService) {
		executorService = _executorService;
	}

	@Override
	public void service(Request _request, Response _response) throws Exception {
		if (storage == null) {
			storage = Storage.getInstance();
		}
		if (_request.getParameter("id") == null) {
			CheckoutThread checkoutThread = new CheckoutThread();
			COCheckOutStatus resume = storage.create();
			resume.setCheckoutThread(checkoutThread);
			executorService.execute(checkoutThread);
			_response.getWriter().write(mapper.writeValueAsString(resume));
		} else {
			// Watch current process
			long id = Long.parseLong(_request.getParameter("id"));
			_response.getWriter().write(mapper.writeValueAsString(storage.get(id)));
		}
		_response.setContentType("application/json");
	}
}
