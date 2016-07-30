package com.sleroux.bank.dao.impl;

import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IBalanceDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.AccountBalance;

@Repository
public class BalanceDao extends AbstractHibernateDao<AccountBalance> implements IBalanceDao {
	public BalanceDao() {
		super();

		setClazz(AccountBalance.class);
	}
}
