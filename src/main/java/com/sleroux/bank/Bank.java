package com.sleroux.bank;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import asg.cliche.CLIException;
import asg.cliche.Command;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.business.Solde;
import com.sleroux.bank.business.tool.Setup;
import com.sleroux.bank.business.tool.Test;
import com.sleroux.bank.business.tool.UpdatePassword;
import com.sleroux.bank.business.tool.Version;
import com.sleroux.bank.evo.Calc;
import com.sleroux.bank.evo.Catego;
import com.sleroux.bank.evo.DBToFile;
import com.sleroux.bank.evo.FileToDB;
import com.sleroux.bank.evo.Import;
import com.sleroux.bank.util.Config;

public class Bank {

	private static Bank	instance;
	private static int	terminalWidth	= 80;

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException, CLIException {

		Config.loadProperties();

		if (args.length == 2 && args[1].equals("version")) {
			System.out.println(Config.getVersion());
			return;
		}

		Shell shell = ShellFactory.createConsoleShell("bank", "bank", new Bank());

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

	public Bank() throws IOException {
		Config.loadProperties();
		instance = this;
	}

	@Command(name = "setup", description = "run intial setup")
	public void setup() {
		run(new Setup());
	}

	@Command(name = "password", abbrev = "pwd", description = "Configure/update password")
	public void updatePassword() {
		run(new UpdatePassword());
	}

	@Command(name = "calc", abbrev = "c", description = "Calculate monthly summary")
	public void calc() {
		run(new Calc());
	}

	@Command(name = "catego", abbrev = "ct", description = "Categorize operations")
	public void catego() {
		run(new Catego());
	}

	@Command(name = "solde", abbrev = "s", description = "Display current soldes")
	public void solde() {
		run(new Solde());
	}

	@Command(name = "import", description = "import from bank website")
	public void bankImport() {
		run(new Import());
	}

	@Command(name = "file2db", description = "Store Excel document into MySQL")
	public void file2db() {
		run(new FileToDB());
	}

	@Command(name = "db2file", description = "Write MySQL data to Excel document")
	public void db2file() {
		run(new DBToFile());
	}

	@Command(name = "version", description = "Display current version")
	public void version() {
		run(new Version());
	}

	@Command(name = "test-config", description = "Test configuration")
	public void testConfig() {
		run(new Test(true));
	}

	private void run(BusinessServiceAbstract _service) {
		try {
			_service.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getTerminalWidth() {
		return terminalWidth;
	}

}
