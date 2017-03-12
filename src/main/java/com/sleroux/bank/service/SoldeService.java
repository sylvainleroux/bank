package com.sleroux.bank.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.domain.SessionData;
import com.sleroux.bank.model.AccountBalance;

@Service
public class SoldeService {

	@Autowired
	IOperationDao	operationDao;

	@Autowired
	SessionData		sessionData;

	public void run() {

		DecimalFormat formater = new DecimalFormat("+ ###,###.00;-#");
		BigDecimal total = new BigDecimal(0);
		List<AccountBalance> accounts = operationDao.getSoldes(sessionData.getUserID());
		for (AccountBalance c : accounts) {
			System.out.printf("%-10s: %14s\n", c.getCompte(), formater.format(c.getSolde()));
			total = total.add(c.getSolde());
		}
		System.out.println("--------------------------");
		System.out.printf("%-10s: %14s\n", "Total", formater.format(total));

	}

}
