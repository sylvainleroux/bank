package com.sleroux.bank.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkDay {

	public static boolean isWorkingDay(Calendar date) {
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
