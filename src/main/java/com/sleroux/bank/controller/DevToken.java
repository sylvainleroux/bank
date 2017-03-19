package com.sleroux.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.service.AuthTokenService;

@Lazy
@Controller
public class DevToken extends AbstractController {

	@Autowired
	AuthTokenService authTokenService;

	@Override
	public void run() throws Exception {
		System.out.println(authTokenService.createAuthencationToken("",""));
	}

}
