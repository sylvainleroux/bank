package com.sleroux.bank.dao.impl;

import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.Budget;

@Repository
public class BudgetDao extends AbstractHibernateDao<Budget> implements IBudgetDao {

	public BudgetDao() {
		super();

		setClazz(Budget.class);
	}

}
