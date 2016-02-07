package com.sleroux.bank.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IExtractHistoryDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.ExtractHistory;

@Repository
public class ExtactHistoryDao extends AbstractHibernateDao<ExtractHistory> implements IExtractHistoryDao {

	public ExtactHistoryDao() {
		super();

		setClazz(ExtractHistory.class);
	}

	@Override
	public Date getLastExtractDate() {

		@SuppressWarnings("unchecked")
		List<ExtractHistory> list = getCurrentSession().createQuery("from ExtractHistory order by extractDate desc").setMaxResults(1).list();

		if (list.size() == 0) {
			return null;
		}

		return list.get(0).getExtractDate();

	}

}
