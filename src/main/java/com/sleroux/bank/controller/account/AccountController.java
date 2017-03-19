package com.sleroux.bank.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.controller.AbstractController;
import com.sleroux.bank.domain.SessionData;

@Lazy
@Controller
public class AccountController extends AbstractController {

	@Autowired
	SessionData sessionData;

	@Override
	public void run() throws Exception {

		while (true) {
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("    1:list");
			System.out.println("    2:add");
			System.out.println("    3:remove");
			System.out.println("    0:return");
			System.out.println("--------------------------------------------------------------------------------");

			int suggestedAction = 1;
			// _username + "@bank[1]>
			String cmd = prompt(String.format("%s@bank/account[%d]> ", sessionData.getUsername(), suggestedAction));

			if (cmd.equals("")) {
				cmd = "1";
			}

			if (cmd.equals("1")) {
				run(AccountList.class);
			}

			if (cmd.equals("2")) {
				run(AccountAdd.class);
			}

			if (cmd.equals("0")) {
				break;
			}
		}
	}

}
