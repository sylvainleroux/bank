package com.sleroux.bank.controller.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.controller.AbstractController;
import com.sleroux.bank.domain.SessionData;
import com.sleroux.bank.model.Account;
import com.sleroux.bank.service.AccountService;

@Lazy
@Controller
public class AccountList extends AbstractController {

	@Autowired
	SessionData		sessionData;

	@Autowired
	AccountService	accountService;

	@Override
	public void run() throws Exception {
		System.out.println("Existing accounts:");
		try {
			List<Account> list = accountService.getAccountsByUserID(sessionData.getUserID());
			list.forEach(account -> {
				System.out.println("    " + account.getType() + ": " + account.getLogin());
			});
			if (list.size() == 0) {
				System.out.println("    No Account");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}
