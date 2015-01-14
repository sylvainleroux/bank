package com.sleroux.bank.persistence.dao.extract;

import com.sleroux.bank.model.fileimport.ExtractOperation;
import com.sleroux.bank.model.statement.Operation;

public class ExtractOperationTransformer {

	public Operation transform(ExtractOperation _o) {
		Operation o = new Operation();
		o.setAccountID(_o.getAccountID());
		o.setDateCompta(_o.getDateCompta());
		o.setDateOperation(_o.getDateOperation());
		o.setLibelle(_o.getLibelle());
		o.setReference(_o.getReference());
		o.setDateValeur(_o.getDateValeur());
		o.setMontant(_o.getMontant());
		return o;
	}

}
