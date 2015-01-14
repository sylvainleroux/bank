package com.sleroux.bank.business.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.presentation.ConsoleAppHeader;
import com.sleroux.bank.util.Config;

public class Test extends BusinessServiceAbstract {

	boolean	parameterForce;

	public Test(boolean _force) {
		parameterForce = _force;
	}

	@Override
	public void run() throws Exception {
		run(parameterForce);
	}

	public void run(boolean _force) {
		ConsoleAppHeader.printAppHeader("TEST");
		System.out.println("Properties:");
		Properties p = Config.getProperties();
		Set<Object> keys = p.keySet();
		String[] strkeys = keys.toArray(new String[keys.size()]);
		Arrays.sort(strkeys);
		for (String key : strkeys) {
			System.out.printf("    %-25s : %s\n", key, Config.getProperty(key));
		}
		if (_force) {
			System.out.println("Credentials : ");
			System.out.println("   Username : " + Config.getLogin());
			System.out.println("   Password : " + Config.getPassword());
		}
		testData();
	}

	private void testData() {
		Book book = getBookDao().getBook();
		for (Year y : book.getYears()) {
			System.out.println(y.getYear());
			int month = -1;
			int monthAdjusted = -1;
			List<String> catego = new ArrayList<String>();
			String bank = "";
			String adjusted = "";
			for (Operation o : y.getOperations()) {
				if (o.getMonth() > month) {
					month = o.getMonth();
					bank += "[" + month + "]";
				}
				if (o.getMonthAdjusted() > monthAdjusted) {
					monthAdjusted = o.getMonthAdjusted();
					adjusted += "[" + monthAdjusted + "]";
				}
				if (!catego.contains(o.getCatego())) {
					if (o.getCatego() != null)
						catego.add(o.getCatego());
				}
			}
			System.out.println("Bank  " + bank);
			System.out.println("Ajusted " + adjusted);

			Collections.sort(catego);
			@SuppressWarnings("unused")
			String ct = "";
			for (String c : catego) {
				ct += "[" + c + "]";
			}
			// System.out.println(ct);
		}
	}
}
