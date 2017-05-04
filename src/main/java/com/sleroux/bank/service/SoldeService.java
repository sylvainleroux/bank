package com.sleroux.bank.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.AccountBalance;

@Service
public class SoldeService {

	@Autowired
	IOperationDao operationDao;

	public void run() {

		DecimalFormat formater = new DecimalFormat("+ ###,###.00;-#");
		BigDecimal total = new BigDecimal(0);
		List<AccountBalance> accounts = operationDao.getSoldes();
		for (AccountBalance c : accounts) {
			if (! (c.getSolde().compareTo(BigDecimal.ZERO) > 0)) {
				continue;
			}

			System.out.printf("%-20s: %14s\n", c.getCompte(), formater.format(c.getSolde()));
			total = total.add(c.getSolde());
		}
		System.out.println("------------------------------------");
		System.out.printf("%-20s: %14s\n", "Total", formater.format(total));

	}

}
