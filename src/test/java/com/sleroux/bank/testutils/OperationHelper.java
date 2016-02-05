package com.sleroux.bank.testutils;

import java.math.BigDecimal;
import java.util.Date;

import com.sleroux.bank.model.Operation;

public class OperationHelper {

	// private static DateFormat formatter = new SimpleDateFormat("dd/MM/yy");

	public static Operation createCreditOperation() {
		Operation o = new Operation();
		o.setCompte("TEST");
		o.setDateOperation(new Date());
		o.setDateValeur(new Date());
		o.setLibelle("RANDOM CREDIT OPERATION " + Math.round(Math.random() * 100000) + "_" + new Date().getTime());
		o.setMontant(new BigDecimal(1 + Math.round(Math.random() * 100)));
		return o;
	}

	public static Operation createDebitOperation() {
		Operation o = new Operation();
		o.setCompte("TEST");
		o.setDateOperation(new Date());
		o.setDateValeur(new Date());
		o.setLibelle("RANDOM CREDIT OPERATION " + Math.round(Math.random() * 100000) + "_" + new Date().getTime());
		o.setMontant(new BigDecimal(1 + Math.round(Math.random() * 100)).negate());
		return o;
	}

}
