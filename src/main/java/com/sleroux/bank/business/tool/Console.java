package com.sleroux.bank.business.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.util.Config;

public class Console extends BusinessServiceAbstract {

	@Override
	public void run() throws Exception {
		System.out.println("Run Bank console");
		System.out.println("Version : " + Config.getVersion());
		while (consoleEntry())
			;
	}

	private boolean consoleEntry() {
		try {
			System.out.print("bank> ");
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();
			if (s.equals("exit")) {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
