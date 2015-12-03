package com.sleroux.bank.business;

public abstract class BusinessServiceAbstract {


	public abstract void run() throws Exception;

	public boolean requireConfig() {
		return true;
	}


}