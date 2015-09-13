package com.sleroux.bank;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import com.sleroux.bank.business.app.Period;
import com.sleroux.bank.business.extract.FileImport;
import com.sleroux.bank.business.tool.Console;
import com.sleroux.bank.business.tool.Setup;
import com.sleroux.bank.business.tool.Test;
import com.sleroux.bank.business.tool.UpdatePassword;
import com.sleroux.bank.business.tool.Version;
import com.sleroux.bank.evo.Adjust;
import com.sleroux.bank.evo.Calc;
import com.sleroux.bank.evo.Catego;
import com.sleroux.bank.evo.Import;
import com.sleroux.bank.evo.ReadBudget;
import com.sleroux.bank.evo.WriteBudget;
import com.sleroux.bank.persistence.PersistenceContext;
import com.sleroux.bank.util.command.CommandCollection;

public class Bank {

	private static Bank					instance;
	private static int					terminalWidth		= 80;
	//
	public final static String			APP_ALL				= "all";
	public final static String			APP_PASSWORD		= "pwd";
	public final static String			APP_SETUP			= "setup";
	public final static String			APP_TEST			= "test";
	public static final String			APP_CALC			= "calc";
	public static final String			APP_CATEGO			= "catego";
	public static final String			APP_FILEIMPORT		= "fileimport";
	public static final String			APP_IMPORT			= "import";
	public static final String			APP_VERSION			= "version";
	public static final String			APP_PERIOD			= "period";
	public static final String			APP_WRITE_BUDGET	= "write";
	public static final String			APP_READ_BUDGET		= "read";
	public static final String			APP_ADJUST			= "adjust";
	//
	private final static List<String>	apps				= Arrays.asList(APP_IMPORT, APP_FILEIMPORT, APP_CATEGO, APP_CALC, APP_PASSWORD,
																	APP_SETUP, APP_TEST, APP_ALL, APP_VERSION, APP_PERIOD,
																	APP_WRITE_BUDGET, APP_READ_BUDGET, APP_ADJUST);

	/**
	 * @param args
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		// Header.printHeader();
		if (args.length == 1) {
			System.out.println("Usage : bank <command> ");
			System.out.println("Commands : ");
			String f = "\t%-10s %s";
			System.out.printf(f, APP_ALL, "Run import,catego and calc commands\n");
			System.out.printf(f, APP_FILEIMPORT, "Import CSV files from disk\n");
			System.out.printf(f, APP_IMPORT, "Import data from Cyberplus\n");
			System.out.printf(f, APP_CATEGO, "Launch operations categorization\n");
			System.out.printf(f, APP_CALC, "Do calculations and print report\n");
			System.out.printf(f, APP_PERIOD, "Start a new 'adjusted month'\n");
			System.out.println();
			System.out.println("Maintenance commands : ");
			System.out.printf(f, APP_VERSION, "Print version\n");
			System.out.printf(f, APP_PASSWORD, "Set/update password, stored with encryption\n");
			System.out.printf(f, APP_SETUP, "Initial setup\n");
			System.out.printf(f, APP_TEST, "Test configuration\n");
			System.out.printf("\t  %-8s %s", "-f", "Force for more tests\n");
			System.out.printf("\t  %-8s %s", "-h", "Print health report\n");
			System.exit(1);
		}
		PersistenceContext context = PersistenceContext.getStandardInstance();
		getInstance().run(args, context);
		context.finalizeContext();
	}

	public static Bank getInstance() {

		if (instance == null)
			instance = new Bank();
		return instance;
	}

	// Loader
	private void run(String[] _args, PersistenceContext _context) {
		String args[];
		String terminalWidthString = (_args.length > 0) ? _args[_args.length - 1] : "";
		if (terminalWidthString.startsWith("columns:")) {
			args = new String[_args.length - 1];
			for (int i = 0; i < _args.length - 1; i++) {
				args[i] = _args[i];
			}
			String width = terminalWidthString.substring("columns:".length()).trim();
			try {
				terminalWidth = Integer.parseInt(width);
			} catch (NumberFormatException e) {
				// e.printStackTrace();
				// ignore
			}
		} else {
			args = _args;
		}
		final CommandCollection commands = CommandCollection.getCommand(args, apps);

		if (commands.contains(APP_ALL)) {
			commands.add(APP_IMPORT);
			commands.add(APP_CATEGO);
			commands.add(APP_CALC);
		}

		if (commands.contains(APP_SETUP)) {
			_context.exec(new Setup());
		}

		if (commands.contains(APP_PASSWORD)) {
			_context.exec(new UpdatePassword());
		}

		if (commands.contains(APP_IMPORT)) {
			_context.exec(new Import());
		}

		if (commands.contains(APP_FILEIMPORT)) {
			_context.exec(new FileImport());
		}

		if (commands.contains(APP_CATEGO)) {
			_context.exec(new Catego());
		}

		if (commands.contains(APP_ADJUST)) {
			_context.exec(new Adjust());
		}

		if (commands.contains(APP_CALC)) {
			_context.exec(new Calc());
		}

		if (commands.contains(APP_TEST)) {
			_context.exec(new Test(commands.get(APP_TEST).getParameters().contains("-f")));
		}

		if (commands.contains(APP_VERSION)) {
			_context.exec(new Version());
		}

		if (commands.getCommands().isEmpty()) {
			_context.exec(new Console());
		}

		if (commands.contains(APP_PERIOD)) {
			_context.exec(new Period());
		}

		if (commands.contains(APP_WRITE_BUDGET)) {
			_context.exec(new WriteBudget());
		}
		if (commands.contains(APP_READ_BUDGET)) {
			_context.exec(new ReadBudget());
		}

	}

	public static List<String> getApps() {
		return apps;
	}

	public int getTerminalWidth() {
		return terminalWidth;
	}

}
