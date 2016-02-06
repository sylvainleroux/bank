package com.sleroux.bank;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import asg.cliche.CLIException;
import asg.cliche.Command;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.business.tool.Setup;
import com.sleroux.bank.business.tool.Test;
import com.sleroux.bank.business.tool.UpdatePassword;
import com.sleroux.bank.business.tool.Version;
import com.sleroux.bank.controller.CalcController;
import com.sleroux.bank.controller.CategoController;
import com.sleroux.bank.controller.DBToFile;
import com.sleroux.bank.controller.ExtractController;
import com.sleroux.bank.controller.FileToDB;
import com.sleroux.bank.controller.ImportController;
import com.sleroux.bank.controller.SoldeController;
import com.sleroux.bank.util.Config;

@Component
public class Bank {

	private static Bank					instance;
	private static ApplicationContext	applicationContext;

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException, CLIException {

		Config.loadProperties();

		if (args.length == 2 && args[1].equals("version")) {
			System.out.println(Config.getVersion());
			return;
		}
		
		System.out.println("Loading ...");
		
		applicationContext = new AnnotationConfigApplicationContext(BankConfig.class);
		instance = applicationContext.getBean(Bank.class);

		Shell shell = ShellFactory.createConsoleShell("bank", "bank", instance);

		if (args.length > 1) {
			StringBuffer line = new StringBuffer();
			for (int i = 0; i < args.length - 1; i++) {
				if (i > 0) {
					line.append(" ");
				}

				line.append(args[i]);
			}
			shell.processLine(line.toString());
		} else {
			shell.commandLoop();
		}
	}

	public static Bank getInstance() {
		return instance;
	}

	@Command(name = "setup", description = "run intial setup")
	public void setup() {
		run(Setup.class);
	}

	@Command(name = "password", abbrev = "pwd", description = "Configure/update password")
	public void updatePassword() {
		run(UpdatePassword.class);
	}

	@Command(name = "calc", abbrev = "c", description = "Calculate monthly summary")
	public void calc() {
		run(CalcController.class);
	}

	@Command(name = "catego", abbrev = "ct", description = "Categorize operations")
	public void catego() {
		run(CategoController.class);
	}

	@Command(name = "extract", description = "extract from bank website")
	public void bankExtract() {
		run(ExtractController.class);
	}

	@Command(name = "import", description = "import from bank website")
	public void bankImport() {
		run(ImportController.class);
	}

	@Command(name = "file2db", description = "Store Excel document into MySQL")
	public void file2db() {
		run(FileToDB.class);
	}

	@Command(name = "db2file", description = "Write MySQL data to Excel document")
	public void db2file() {
		run(DBToFile.class);
	}

	@Command(name = "version", description = "Display current version")
	public void version() {
		run(Version.class);
	}

	@Command(name = "test-config", description = "Test configuration")
	public void testConfig() {
		run(Test.class);
	}

	@Command(name = "solde", abbrev = "s", description = "Display balance for all accounts")
	public void balance() {
		run(SoldeController.class);
	}

	private void run(Class<? extends BusinessServiceAbstract> _clazz) {

		BusinessServiceAbstract service = applicationContext.getBean(_clazz);
		try {
			service.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
