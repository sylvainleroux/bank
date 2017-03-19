package com.sleroux.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.controller.account.AccountController;
import com.sleroux.bank.domain.SessionData;
import com.sleroux.bank.model.User;
import com.sleroux.bank.service.AuthenticationService;

@Controller
public class LoginController extends AbstractController {

	@Autowired
	AuthenticationService	authenticationService;

	@Autowired
	private SessionData		sessionData;

	@Override
	public void run() throws Exception {

		String username = null;
		String password = null;

		int retry = 3;

		User user = null;

		while (retry-- > 0) {

			if (retry == 2 && sessionData.getUsername() != null) {
				username = sessionData.getUsername();
				password = sessionData.getPassword();
			} else {
				username = prompt("Username: ");
				password = prompt("Password: ");
			}

			try {
				user = authenticationService.tryAuthentication(username, password);

				if (user != null) {
					sessionData.setUserID(user.getId());
					sessionData.setUsername(user.getUsername());
					sessionData.setPassword(password);
					break;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		if (user != null) {
			run(SummaryController.class);
			loggedUserPrompt();
		}

	}

	private void loggedUserPrompt() {

		while (true) {
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("    1:soldes");
			System.out.println("    2:calc");
			System.out.println("    3:extract");
			System.out.println("    4:catego");
			System.out.println("    5:check");
			System.out.println("    6:db2file");
			System.out.println("    7:file2db");
			System.out.println("    8:dev-token");
			System.out.println("    9:comptes");
			System.out.println("    0:logout");
			System.out.println("--------------------------------------------------------------------------------");

			int suggestedAction = 1;
			// _username + "@bank[1]>
			String cmd = prompt(String.format("%s@bank[%d]> ", sessionData.getUsername(), suggestedAction));

			if (cmd.equals("")) {
				cmd = "1";
			}

			if (cmd.equals("1")) {
				run(SoldeController.class);
			}

			if (cmd.equals("2")) {
				run(CalcController.class);
			}

			if (cmd.equals("3")) {
				run(ExtractController.class);
			}

			if (cmd.equals("4")) {
				run(CategoController.class);
			}

			if (cmd.equals("5")) {
				run(HealthCheckController.class);
			}

			if (cmd.equals("8")) {
				run(DevToken.class);
			}

			if (cmd.equals("9")) {
				run(AccountController.class);
			}

			if (cmd.equals("0")) {
				break;
			}

		}

	}

	public class Command {
		public String	name;
		public int		index;
		public String	option;
	}
}