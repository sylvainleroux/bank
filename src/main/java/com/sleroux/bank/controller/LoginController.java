package com.sleroux.bank.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.domain.SessionData;
import com.sleroux.bank.model.User;
import com.sleroux.bank.service.AuthenticationService;

@Controller
public class LoginController extends BusinessServiceAbstract {

	@Autowired
	AuthenticationService		authenticationService;

	private ApplicationContext	context;

	@Autowired
	private SessionData			sessionData;

	@Override
	public void run(ApplicationContext _applicationContext) throws Exception {
		context = _applicationContext;
		String username = null;
		String password = null;

		int retry = 3;

		User user = null;

		while (retry-- > 0) {

			username = prompt("Username: ");
			password = prompt("Password: ");

			try {
				user = authenticationService.tryAuthentication(username, password);

				if (user != null) {
					sessionData.setUserID(user.getId());
					sessionData.setUsername(user.getUsername());
					sessionData.setPassword(password);
					sessionData.setCmbLogin(user.getCmbLogin());
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
			System.out.println("    9:exit");
			System.out.println("--------------------------------------------------------------------------------");

			int suggestedAction = 1;
			// _username + "@bank[1]>
			String cmd = prompt(String.format("%s@bank[%d]> ", sessionData.getUsername(), suggestedAction));

			if (cmd.equals("1")) {
				run(SoldeController.class);
			}

			if (cmd.equals("2")) {
				run(CalcController.class);
			}

			if (cmd.equals("3")) {
				run(ExtractController.class);
			}

			if (cmd.equals("9")) {
				break;
			}

		}

	}

	private String prompt(String _prompt) {
		System.out.print(_prompt);
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String p = null;
		try {
			p = bufferRead.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	private void run(Class<? extends BusinessServiceAbstract> _clazz) {

		BusinessServiceAbstract service = context.getBean(_clazz);
		try {
			service.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() throws Exception {
		// Do not implement
	}

	public class Command {
		public String	name;
		public int		index;
		public String	option;
	}
}