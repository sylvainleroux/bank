package com.sleroux.bank.dao.impl;

import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.ICompteDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.Compte;

@Repository
public class CompteDao extends AbstractHibernateDao<Compte> implements ICompteDao {

	public CompteDao() {
		super();

		setClazz(Compte.class);
	}

}
