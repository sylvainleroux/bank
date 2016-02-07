package com.sleroux.bank.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.ExtractHistory;

@Repository
public class ExtractHistoryDao extends AbstractHibernateDao<ExtractHistory> {

	public ExtractHistoryDao() {
		super();

		setClazz(ExtractHistory.class);
	}

	public Date getLastExtractDate() {

		@SuppressWarnings("unchecked")
		List<ExtractHistory> list = getCurrentSession().createQuery("from ExtractHistory order by extractDate desc").setMaxResults(1)
				.list();

		if (list.size() == 0) {
			return null;
		}

		return list.get(0).getExtractDate();

	}

}
