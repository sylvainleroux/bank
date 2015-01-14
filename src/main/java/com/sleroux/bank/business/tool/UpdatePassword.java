package com.sleroux.bank.business.tool;

import java.io.Console;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.util.Config;

public class UpdatePassword extends BusinessServiceAbstract {

	public void run() {
		System.out.println("Update Cyberplus password for account : " + Config.getLogin());
		String username = getUsername();
		String password = getPassword();
		Config.updateCredentials(username, password);
		System.out.println("Update finished");
	}

	private String getUsername() {
		Console cons;
		String username = null;
		if ((cons = System.console()) != null) {
			while (username == null) {
				username = cons.readLine("%s", "Login :");
				if (username == null || username.equals("")) {
					continue;
				}
			}
		}
		return username;
	}

	private String getPassword() {
		Console cons;
		char[] passwd = null;
		char[] confirm = null;
		String password = null;
		if ((cons = System.console()) != null) {
			while (password == null) {
				passwd = cons.readPassword("%s", "Password:");
				confirm = cons.readPassword("%s", "Confirm :");
				// Check null
				if (passwd == null || confirm == null) {
					System.out.println("Password is null");
					continue;
				}
				String p = new String(passwd);
				String c = new String(confirm);

				if (!p.equals(c)) {
					System.out.println("Passwords doesnt match");
					continue;
				}
				password = p;
			}
		}
		return password;
	}

}
