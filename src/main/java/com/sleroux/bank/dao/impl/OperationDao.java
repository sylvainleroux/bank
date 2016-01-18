package com.sleroux.bank.dao.impl;

import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.Operation;

@Repository
public class OperationDao extends AbstractHibernateDao<Operation> implements IOperationDao {

	public OperationDao() {
		super();

		setClazz(Operation.class);
	}

}
