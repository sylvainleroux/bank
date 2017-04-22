package com.sleroux.bank.controller;

import org.springframework.stereotype.Controller;

@Controller
public class BankController extends AbstractController {

	@Override
	public void run() throws Exception {

		System.out.println("Bank version: xxx");

		while (true) {
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("    1:login");
			System.out.println("    2:new account");
			System.out.println("    3:version");
			System.out.println("    0:exit");
			System.out.println("--------------------------------------------------------------------------------");
			System.out.print("bank[1]> ");
			String prompt = prompt();
			if (prompt.equals("")) {
				prompt = "1";
			}
			if (prompt.equals("1")) {
				run(LoginController.class);
			}
			if (prompt.equals("2")) {
				run(NewAccountController.class);
			}
			if (prompt.equals("3")) {
				run(Version.class);
			}
			if (prompt.equals("0")) {
				break;
			}
		}

	}

}
