package com.sleroux.bank.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.util.Config;

@Lazy
@Controller
public class Version extends AbstractController {

	@Override
	public void run() throws Exception {
		System.out.println(Config.getVersion() + "-SLR");
	}

}
