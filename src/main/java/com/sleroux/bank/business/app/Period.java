package com.sleroux.bank.business.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.RootLogger;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.model.statement.Book;
import com.sleroux.bank.model.statement.Operation;
import com.sleroux.bank.model.statement.Year;
import com.sleroux.bank.util.formats.OperationFormater;

public class Period extends BusinessServiceAbstract {

	private Logger						logger				= RootLogger.getLogger(Period.class);

	private final static List<String>	NEW_MONTH_CATEGO	= Arrays.asList("SALAIRE", "TELEPHONE", "FRAIS.BANQUE");

	private SimpleDateFormat			dateFormat			= new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void run() throws Exception {

		Book book = bookDao.getBook();

		Operation suggestedOperation = null;
		Operation suggestedNewAdjustedMonth = null;

		for (Year year : book.getYears()) {
			logger.info("----- " + year.getYear());
			int currentMonth = 0;
			Operation previous = null;
			for (Operation o : year.getOperations()) {
				Date date = o.getDateValeur();
				if (date == null) {
					continue;
				}
				int monthBank = o.getMonth();
				int monthCompa = getMonth(date) + 1;
				//int dayOfWeek = getDayOfWeek(date);
				if (currentMonth == 0 && monthCompa == 12) {
					// previous year, jump
					continue;
				}
				if (previous != null && previous.getMonth() != o.getMonth()) {
					logger.info("reset");
					suggestedOperation = null;
				}
				if (currentMonth < monthCompa && isWorkingDay(date)) {
					// System.out.println("SHOULD ITERATE " +
					// dateFormat.format(o.getDateCompta()) + " " + dayOfWeek);
					int previousMonth = 0;
					if (previous != null) {
						previousMonth = previous.getMonth();
					}
					currentMonth = monthCompa;
					logger.info("Iterate to : " + currentMonth + ", " + dateFormat.format(date) + " in doc : " + previousMonth + ">"
							+ monthBank + " " + ((previousMonth == monthBank - 1) ? "MATCH" : ""));

					suggestedOperation = o;
				}

				previous = o;
			}
		}

		// New Adjusted MONTH
		for (Year year : book.getYears()) {
			boolean searchEnabled = false;
			int currentMonth = 0;
			for (Operation o : year.getOperations()) {
				if (o.getDateCompta() == null) {
					continue;
				}
				Calendar d = Calendar.getInstance();
				d.setTime(o.getDateValeur());
				// Activate trigger at half month
				int operationMonth = d.get(Calendar.MONTH) + 1;
				logger.info(operationMonth + "/" + currentMonth);
				if (operationMonth != currentMonth) {
					if (d.get(Calendar.DAY_OF_MONTH) > 15) {
						currentMonth = operationMonth;
						searchEnabled = true;
					}
				}
				if (searchEnabled) {
					if (NEW_MONTH_CATEGO.contains(o.getCatego())) {
						logger.info(OperationFormater.toStringLight(o));
						suggestedNewAdjustedMonth = o;
						// Disable search until the end of the next month
						searchEnabled = false;
					}
				}
			}
		}

		// Scan MONTH BANK
		if (suggestedOperation != null) {
			System.out.println("---------------------------------------------------------");
			System.out.println("New Bank month detected");
			System.out.println("---------------------------------------------------------");
			Operation[] operations = new Operation[10];
			main: for (Year year : book.getYears()) {
				int countAfter = 0;
				for (Operation op : year.getOperations()) {
					for (int i = 1; i < 10; i++) {
						operations[i - 1] = operations[i];
					}
					operations[9] = op;
					if (op.equals(suggestedOperation)) {
						countAfter++;
					}
					if (countAfter > 0) {
						countAfter++;
					}
					if (countAfter > 5) {
						break main;
					}
				}
			}
			for (int i = 0; i < 10; i++) {
				System.out.println(OperationFormater.selector(operations[i], i + 1, operations[i].equals(suggestedOperation)));
			}
			Integer value = null;
			while (value == null) {
				value = requestMonthBank();
			}
			if (value != null) {
				Operation toUpdate = null;
				if (value.intValue() == 0) {
					toUpdate = suggestedOperation;
				} else {
					if (value.intValue() > 0 && value.intValue() <= 10) {
						toUpdate = operations[value.intValue() - 1];
					}
				}
				if (toUpdate != null) {
					toUpdate.setMonth(toUpdate.getMonth() + 1);
				}
			}
		}
		// Scan MONTH ADJUSTED
		if (suggestedNewAdjustedMonth != null) {
			System.out.println("---------------------------------------------------------");
			System.out.println("New Adjusted month detected");
			System.out.println("---------------------------------------------------------");
			Operation[] operations = new Operation[10];
			main: for (Year year : book.getYears()) {
				int countAfter = 0;
				for (Operation op : year.getOperations()) {
					for (int i = 1; i < 10; i++) {
						operations[i - 1] = operations[i];
					}
					operations[9] = op;
					if (op.equals(suggestedNewAdjustedMonth)) {
						countAfter++;
					}
					if (countAfter > 0) {
						countAfter++;
					}
					if (countAfter > 5) {
						break main;
					}
				}
			}
			for (int i = 0; i < 10; i++) {
				System.out.println(OperationFormater.selector(operations[i], i + 1, operations[i].equals(suggestedNewAdjustedMonth)));
			}
		}
	}

	private Integer requestMonthBank() {
		System.out.print("[IGNORE/y/number]>");
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();
			if (s == null || s.equals("")) {
				return null;
			}
			try {
				int value = Integer.parseInt(s);
				System.out.println(value);
				return new Integer(value);
			} catch (NumberFormatException e) {
				System.out.println("Incorrect value");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isWorkingDay(Date _date) {
		Calendar c = Calendar.getInstance();
		c.setTime(_date);
		return isWorkingDay(c);
	}

	@SuppressWarnings("unused")
	private int getDayOfWeek(Date _dateCompta) {
		Calendar c = Calendar.getInstance();
		c.setTime(_dateCompta);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	private int getMonth(Date _dateCompta) {
		Calendar c = Calendar.getInstance();
		c.setTime(_dateCompta);
		return c.get(Calendar.MONTH);
	}

	public boolean isWorkingDay(Calendar date) {
		final List<Calendar> holidays = new ArrayList<Calendar>();
		final Calendar calc = (Calendar) date.clone();
		calc.set(calc.get(Calendar.YEAR), Calendar.JANUARY, 1);
		holidays.add((Calendar) calc.clone());
		calc.set(calc.get(Calendar.YEAR), Calendar.MAY, 1);
		holidays.add((Calendar) calc.clone());
		calc.set(calc.get(Calendar.YEAR), Calendar.MAY, 8);
		holidays.add((Calendar) calc.clone());
		calc.set(calc.get(Calendar.YEAR), Calendar.JULY, 14);
		holidays.add((Calendar) calc.clone());
		calc.set(calc.get(Calendar.YEAR), Calendar.AUGUST, 15);
		holidays.add((Calendar) calc.clone());
		calc.set(calc.get(Calendar.YEAR), Calendar.NOVEMBER, 1);
		holidays.add((Calendar) calc.clone());
		calc.set(calc.get(Calendar.YEAR), Calendar.NOVEMBER, 11);
		holidays.add((Calendar) calc.clone());
		calc.set(calc.get(Calendar.YEAR), Calendar.DECEMBER, 25);
		holidays.add((Calendar) calc.clone());
		final int intGoldNumber = date.get(Calendar.YEAR) % 19;
		final int intAnneeDiv100 = (int) (date.get(Calendar.YEAR) / 100);
		final int intEpacte = (intAnneeDiv100 - intAnneeDiv100 / 4 - (8 * intAnneeDiv100 + 13) / 25 + (19 * intGoldNumber) + 15) % 30;
		final int intDaysEquinoxeToMoonFull = intEpacte - (intEpacte / 28)
				* (1 - (intEpacte / 28) * (29 / (intEpacte + 1)) * ((21 - intGoldNumber) / 11));
		final int intWeekDayMoonFull = (date.get(Calendar.YEAR) + date.get(Calendar.YEAR) / 4 + intDaysEquinoxeToMoonFull + 2
				- intAnneeDiv100 + intAnneeDiv100 / 4) % 7;
		final int intDaysEquinoxeBeforeFullMoon = intDaysEquinoxeToMoonFull - intWeekDayMoonFull;
		final int intMonthPaques = 3 + (intDaysEquinoxeBeforeFullMoon + 40) / 44;
		final int intDayPaques = intDaysEquinoxeBeforeFullMoon + 28 - 31 * (intMonthPaques / 4);
		calc.set(date.get(Calendar.YEAR), intMonthPaques - 1, intDayPaques + 1);
		final Calendar lundiDePaque = (Calendar) calc.clone();
		holidays.add(lundiDePaque);
		final Calendar ascension = (Calendar) lundiDePaque.clone();
		ascension.add(Calendar.DATE, 38);
		holidays.add(ascension);
		final Calendar lundiPentecote = (Calendar) lundiDePaque.clone();
		lundiPentecote.add(Calendar.DATE, 49);
		holidays.add(lundiPentecote);
		if (holidays.contains(date) || date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
				|| date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			return false;
		}
		return true;
	}
}
