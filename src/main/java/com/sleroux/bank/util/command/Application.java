package com.sleroux.bank.util.command;

import com.sleroux.bank.business.BusinessServiceAbstract;

public class Application {

	private Class<? extends BusinessServiceAbstract>	service;
	private String										helpMessage;

	public Application(Class<? extends BusinessServiceAbstract> _class, String _helpMessage) {
		service = _class;
		helpMessage = _helpMessage;
	}

	public String getHelpMessage() {
		return helpMessage;
	}

	public BusinessServiceAbstract createInstance(Command _command) {
		try {
			return (BusinessServiceAbstract) service.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
