package com.sleroux.bank.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sleroux.bank.model.AnalysisFact;

@Repository
public class AnalysisDao {

	@Autowired
	private SessionFactory	sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<AnalysisFact> getFacts() {

		Query query = getCurrentSession()
				.createSQLQuery(
						"select catego, CASE WHEN is_credit THEN TRUE ELSE FALSE END as credit, if (is_credit, ops_credit, ops_debit) as ops, if (is_credit, bud_credit, bud_debit) as bud from diff where year = :year and month = :month order by catego");
		query.setResultTransformer(Transformers.aliasToBean(AnalysisFact.class));
		return query.list();

	}

}
