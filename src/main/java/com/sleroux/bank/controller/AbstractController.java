package com.sleroux.bank.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class AbstractController {

	public abstract void run() throws Exception;

	public boolean requireConfig() {
		return true;
	}

	@Autowired
	private ApplicationContext applicationContext;

	protected void run(Class<? extends AbstractController> _clazz) {

		AbstractController service = applicationContext.getBean(_clazz);
		try {
			service.run();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	protected static String prompt() {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String p = null;
		try {
			p = bufferRead.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	protected String prompt(String _prompt) {
		System.out.print(_prompt);
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String p = null;
		try {
			p = bufferRead.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
}
