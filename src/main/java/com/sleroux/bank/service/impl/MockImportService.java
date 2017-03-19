package com.sleroux.bank.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.model.Operation;

@Service
public class MockImportService {

	@Autowired
	IOperationDao operationDao;

	@Transactional
	public void run(int _userID) {

		Calendar c = Calendar.getInstance();
		c.set(2017, 3, 11);

		Operation o = new Operation();
		o.setLibelle("CB: AVION POMME");
		o.setDebit(new BigDecimal("19.29"));
		o.setDateOperation(c.getTime());
		o.setDateValeur(c.getTime());
		o.setCompte("BANK.ACCOUNT");
		o.setUserID(_userID);
		o.setYear(2017);
		o.setMonthAdjusted(3);
			
		operationDao.create(o);

	}

}
