package com.sleroux.bank.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.AccountBalance;
import com.sleroux.bank.model.Operation;

@Repository
public class OperationDao extends AbstractHibernateDao<Operation> implements IOperationDao {

	public OperationDao() {
		super();

		setClazz(Operation.class);
	}

	@Override
	public int insertIgnore(Operation _o) {

		String sql = "INSERT IGNORE into operation (compte, date_operation, date_valeur, libelle, credit, debit)";
		sql += "VALUES (:compte, :date_operation, :date_valeur, :libelle, :credit, :debit)";

		Query query = getCurrentSession().createSQLQuery(sql);
		query.setParameter("compte", _o.getCompte());
		query.setParameter("date_operation", _o.getDateOperation());
		query.setParameter("date_valeur", _o.getDateValeur());
		query.setParameter("libelle", _o.getLibelle());
		query.setParameter("credit", _o.getCredit());
		query.setParameter("debit", _o.getDebit());

		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Operation> findUncategorized(long _userID) {
		return getCurrentSession().createQuery("from Operation where catego is null and user_id = :user_id")
				.setParameter("user_id", _userID).list();
	}

	@Override
	public List<String> getCategoriesDebit(long _userID) {
		Query query = getCurrentSession()
				.createQuery("select distinct catego from Operation where debit > 0 and user_id = :user_id")
				.setParameter("user_id", _userID);
		@SuppressWarnings("unchecked")
		List<String> list = query.list();
		return list;
	}

	@Override
	public List<String> getCategoriesCredit(long _userID) {
		Query query = getCurrentSession()
				.createQuery("select distinct catego from Operation where credit > 0 and user_id = :user_id")
				.setParameter("user_id", _userID);
		@SuppressWarnings("unchecked")
		List<String> list = query.list();
		return list;
	}

	@Override
	public List<String> getSuggestionsFor(final Operation _o) {

		CategorizationProcStockWork work = new CategorizationProcStockWork(_o);
		Session session = getCurrentSession();
		session.doWork(work);
		return work.getSuggestions();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountBalance> getSoldes(long _userID) {
		Query query = getCurrentSession().createSQLQuery("select * from bank.soldes where user_id = :user_id")
				.setParameter("user_id", _userID).setResultTransformer(Transformers.aliasToBean(AccountBalance.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AggregatedOperations> findAggregatedYearMonth(int _year, int _month, long _userID) {
		Query query = getCurrentSession().createSQLQuery(
				"select year, month, compte as account, catego, sum(credit) as credit, sum(debit) as debit from operation where year = :year and month = :month and user_id = :user_id group by year, month, compte, catego");
		query.setParameter("year", _year);
		query.setParameter("month", _month);
		query.setParameter("user_id", _userID);
		query.setResultTransformer(Transformers.aliasToBean(AggregatedOperations.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Operation> findByCategoYearMonth(Integer _year, Integer _month, String _catego, long _userID) {
		return getCurrentSession()
				.createQuery(
						"from Operation where year=:year and month=:month and catego=:catego and user_id = :user_id")
				.setParameter("year", _year).setParameter("month", _month).setParameter("catego", _catego)
				.setParameter("user_id", _userID).list();

	}

}
