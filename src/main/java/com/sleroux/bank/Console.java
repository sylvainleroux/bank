package com.sleroux.bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.console.Command;
import com.sleroux.bank.controller.LoginController;
import com.sleroux.bank.controller.UserAddController;
import com.sleroux.bank.util.Config;

import asg.cliche.CLIException;

@Component
public class Console {

	private static Console				instance;
	private static ApplicationContext	applicationContext;

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, CLIException {

		Config.loadProperties();

		if (args.length == 2 && args[1].equals("version")) {
			System.out.println(Config.getVersion());
			return;
		}

		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {

			String	l	= "|/—\\|/-\\";
			int		i	= 0;

			@Override
			public void run() {
				System.out.printf("Loading %c\r", l.charAt(i % 8));
				i++;

			}
		}, 0, 100);

		try {
			applicationContext = new AnnotationConfigApplicationContext(BankConfig.class);
		} catch (Exception e) {

		}
		instance = applicationContext.getBean(Console.class);
		t.cancel();

		// Application is loaded, start prompt
		firstPrompt();

	}

	private static void firstPrompt() {

		System.out.println("Bank version: xxx");

		while (true) {
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("    1:login");
			System.out.println("    2:create new user");
			System.out.println("    3:exit");
			System.out.println("--------------------------------------------------------------------------------");
			System.out.print("bank[1]> ");
			String prompt = prompt();
			if (prompt.equals("")) {
				prompt = "1";
			}
			if (prompt.equals("1")) {
				run(LoginController.class);
				// exit app when completed
				break;
			}
			if (prompt.equals("2")) {
				run(UserAddController.class);
			}
			if (prompt.equals("3")) {
				break;
			}

		}

	}

	private static String prompt() {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String p = null;
		try {
			p = bufferRead.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	public static Console getInstance() {
		return instance;
	}

	private List<Command> defineCommands() {

		List<Command> list = new ArrayList<>();

		Command exit = new Command();
		exit.setName("exit");
		exit.setCallback(() -> exit());
		list.add(exit);

		return list;

	}

	private final void exit() {
		// Terminate application
	}

	private static void run(Class<? extends BusinessServiceAbstract> _clazz) {

		BusinessServiceAbstract service = applicationContext.getBean(_clazz);
		try {
			service.run(applicationContext);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
