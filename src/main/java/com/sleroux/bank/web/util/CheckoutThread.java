package com.sleroux.bank.web.util;

import com.sleroux.bank.business.extract.CyberplusImport;
import com.sleroux.bank.persistence.PersistenceContext;

public class CheckoutThread implements Runnable {

	private String	status	= "CREATED";

	@Override
	public void run() {
		setStatus("RUNNING:IMPORT");
		PersistenceContext context = PersistenceContext.getStandardInstance();
		context.exec(new CyberplusImport());
		context.finalizeContext();
		setStatus("DONE");
	}

	public synchronized String getStatus() {
		return status;
	}

	public synchronized void setStatus(String _status) {
		status = _status;
	}

}
