package com.sleroux.bank;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.sleroux.bank.controller.AbstractController;
import com.sleroux.bank.controller.BankController;
import com.sleroux.bank.util.Config;

import asg.cliche.CLIException;

@Component
public class Console {

	private static AbstractController	console;
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
		console = applicationContext.getBean(BankController.class);
		t.cancel();

		try {
			console.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
