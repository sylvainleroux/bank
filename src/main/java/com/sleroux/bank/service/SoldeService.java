package com.sleroux.bank.service;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.AccountBalance;

@Service
public class SoldeService {

	@Autowired
	IOperationDao	operationDao;

	public void run() {
		
		DecimalFormat formater = new DecimalFormat("###,###.##");

		List<AccountBalance> accounts = operationDao.getSoldes();
		for (AccountBalance c : accounts) {
			System.out.printf("%10s: %14s\n", c.getCompte(), formater.format(c.getSolde()));
		}

	}

}
