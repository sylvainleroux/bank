package com.sleroux.bank.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sleroux.bank.model.analysis.AnalysisFact;

@Repository
public class AnalysisDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<AnalysisFact> getFacts() {

		Query query = getCurrentSession().createSQLQuery("select * from analysis");
		query.setResultTransformer(Transformers.aliasToBean(AnalysisFact.class));
		return query.list();

	}
}
