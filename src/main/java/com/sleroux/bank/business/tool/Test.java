package com.sleroux.bank.business.tool;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

public class Test extends BusinessServiceAbstract {

	boolean	parameterForce;

	public Test(boolean _force) {
		parameterForce = _force;
	}

	@Override
	public void run() throws Exception {
		run(parameterForce);
	}

	public void run(boolean _force) {
		ConsoleAppHeader.printAppHeader("TEST");
		System.out.println("Properties:");
		Properties p = Config.getProperties();
		Set<Object> keys = p.keySet();
		String[] strkeys = keys.toArray(new String[keys.size()]);
		Arrays.sort(strkeys);
		for (String key : strkeys) {
			System.out.printf("    %-25s : %s\n", key, Config.getProperty(key));
		}
		if (_force) {
			System.out.println("Credentials : ");
			System.out.println("   Username : " + Config.getLogin());
			System.out.println("   Password : " + Config.getPassword());
		}
	}

}
