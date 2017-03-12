package com.sleroux.bank.business;

import org.springframework.context.ApplicationContext;

public abstract class BusinessServiceAbstract {


	public abstract void run() throws Exception;

	public boolean requireConfig() {
		return true;
	}

	public void run(ApplicationContext _applicationContext) throws Exception {
		run();
	}


}