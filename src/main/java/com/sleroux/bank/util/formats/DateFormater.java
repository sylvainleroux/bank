package com.sleroux.bank.util.formats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater {
	private final static SimpleDateFormat	formatter	= new SimpleDateFormat("dd/MM/yy");

	public static String f(Date d) {
		if (d == null)
			return "null";
		return formatter.format(d);
	}

	public static Date parse(String s) {
		try {
			return formatter.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
