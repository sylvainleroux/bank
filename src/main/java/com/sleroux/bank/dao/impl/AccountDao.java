package com.sleroux.bank.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IAccountDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.Account;

@Repository
public class AccountDao extends AbstractHibernateDao<Account> implements IAccountDao {
	public AccountDao() {
		super();
		setClazz(Account.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> findByUserID(long _userID) {
		return getCurrentSession().createQuery("from Account where user_id = :user_id").setParameter("user_id", _userID)
				.list();
	}
}
