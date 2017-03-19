package com.sleroux.bank.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.controller.AbstractController;
import com.sleroux.bank.domain.SessionData;
import com.sleroux.bank.service.AccountService;

@Lazy
@Controller
public class AccountAdd extends AbstractController {

	@Autowired
	SessionData		sessionData;

	@Autowired
	AccountService	accountService;

	@Override
	public void run() throws Exception {
		System.out.println("UserService");

		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("Add Account :");
		System.out.println("    1:CMB");
		System.out.println("    2:BPO");
		System.out.println("    3:TEST");
		System.out.println("    0:return");
		System.out.println("--------------------------------------------------------------------------------");

		int suggestedAction = 1;
		// _username + "@bank[1]>
		String cmd = prompt(String.format("%s@bank/account/add[%d]> ", sessionData.getUsername(), suggestedAction));

		if (cmd.equals("")) {
			cmd = "1";
		}

		if (cmd.equals("1")) {
			System.out.println("Add CMB account");
			String username = prompt("Username: ");
			String password = prompt("Password: ");

			accountService.addAccount(username, password, "CMB", sessionData.getUserID());
		}
		
		if (cmd.equals("2")) {
			System.out.println("Add BPO account");
			String username = prompt("Username: ");
			String password = prompt("Password: ");

			accountService.addAccount(username, password, "BPO", sessionData.getUserID());
		}
		
		if (cmd.equals("3")) {
			System.out.println("Add TEST account");
			String username = prompt("Username: ");
			String password = prompt("Password: ");
			
			accountService.addAccount(username, password, "TEST", sessionData.getUserID());
		}
	}

}
