package com.sleroux.bank.dao;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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

	@SuppressWarnings("rawtypes")
	public List<AnalysisFact> getFacts() {

		Query query = getCurrentSession().createNativeQuery("select * from analysis");
		List<Object[]> records = query.getResultList();
		List<AnalysisFact> facts = new LinkedList<>();

		for (Object[] tuple : records) {
			AnalysisFact fact = new AnalysisFact();
			fact.setYear(((BigDecimal) tuple[0]).intValue());
			fact.setMonth(((BigDecimal) tuple[1]).intValue());
			fact.setCompte((String) tuple[2]);
			fact.setCatego((String) tuple[3]);
			fact.setCredit_ops((BigDecimal) tuple[4]);
			fact.setDebit_ops((BigDecimal) tuple[5]);
			fact.setCredit_bud((BigDecimal) tuple[6]);
			fact.setDebit_bud((BigDecimal) tuple[7]);
			fact.setFlag((Integer)tuple[8]);

			facts.add(fact);
		}
		return facts;

	}

	public static <T> T map(Class<T> type, Object[] tuple) {
		List<Class<?>> tupleTypes = new ArrayList<>();
		for (Object field : tuple) {
			if (field == null) {
				continue;
			}
			tupleTypes.add(field.getClass());
		}
		try {
			Constructor<T> ctor = type.getConstructor(tupleTypes.toArray(new Class<?>[tuple.length]));
			return ctor.newInstance(tuple);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
