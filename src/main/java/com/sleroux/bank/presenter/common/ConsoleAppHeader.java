package com.sleroux.bank.presenter.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleAppHeader {

	private static int	terminalWidth	= 80;

	public static void printHeader() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(ConsoleAppHeader.class.getResourceAsStream("header.ascii")));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printAppHeader(String _s) {
		String line = "";
		for (int i = 0; i < terminalWidth; i++) {
			line += "-";
		}
		int part = terminalWidth - 5;
		System.out.println(line);
		System.out.printf("-- %-" + part + "s--\n", _s);
		System.out.println(line);
	}

	public static void printLine() {
		String line = "";
		for (int i = 0; i < terminalWidth; i++) {
			line += "-";
		}
		System.out.println(line);
	}

}
