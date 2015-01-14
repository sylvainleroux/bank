package com.sleroux.bank.business.tool;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.util.Config;

public class Version extends BusinessServiceAbstract {

	@Override
	public void run() throws Exception {
		System.out.println(Config.getVersion()+"-SLR");
	}

}
