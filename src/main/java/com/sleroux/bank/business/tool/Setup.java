package com.sleroux.bank.business.tool;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.util.Config;

public class Setup extends BusinessServiceAbstract {

	// Default values
	public void run() {
		System.out.println("Start Bank configuration ...");
		try {
			String path = Config.setup();
			System.out.println("Config file created :");
			System.out.println(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean requireConfig() {
		return false;
	}
}
