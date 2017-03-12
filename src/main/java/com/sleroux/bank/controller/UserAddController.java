package com.sleroux.bank.controller;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.service.UserService;

@Controller
public class UserAddController extends BusinessServiceAbstract {

	@Autowired
	UserService userService;

	@Override
	@Transactional
	public void run() throws Exception {
		System.out.println("");

		String username = null;
		String cmbLogin = null;
		String password = null;
		
		try {
			username = prompt("Username: ");
			cmbLogin = prompt("login cmb.fr: ");
			password = prompt("Password: ");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		userService.addUser(username, password, cmbLogin);

	}
	
	private String prompt(String _prompt) {
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
