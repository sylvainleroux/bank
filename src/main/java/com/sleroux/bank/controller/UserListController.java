package com.sleroux.bank.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.service.UserService;

@Controller
public class UserListController extends BusinessServiceAbstract {

	@Autowired
	UserService userService;

	@Override
	@Transactional
	public void run() throws Exception {
		userService.listUsers();
	}

}
