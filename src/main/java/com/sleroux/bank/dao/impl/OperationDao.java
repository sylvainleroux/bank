package com.sleroux.bank.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.balance.AccountBalance;
import com.sleroux.bank.model.operation.Operation;

@Repository
public class OperationDao extends AbstractHibernateDao<Operation> implements IOperationDao {

	public OperationDao() {
		super();

		setClazz(Operation.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Operation> findByYearMonth(int _year, int _month) {
		return getCurrentSession().createQuery("from Operation where year = :year and month = :month")
				.setParameter("year", _year).setParameter("month", _month).list();
	}

	@Override
	public int insertIgnore(Operation _o) {

		String sql = "INSERT IGNORE into operation (compte, date_operation, date_valeur, libelle, credit, debit)";
		sql += "VALUES (:compte, :date_operation, :date_valeur, :libelle, :credit, :debit)";

		NativeQuery<Operation> query = getCurrentSession().createNativeQuery(sql, Operation.class);
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
	public List<Operation> findUncategorized() {
		return getCurrentSession().createQuery("from Operation where catego is null").list();
	}

	@Override
	public List<String> getCategoriesDebit() {
		Query query = getCurrentSession().createQuery("select distinct catego from Operation where debit > 0");
		@SuppressWarnings("unchecked")
		List<String> list = query.list();
		return list;
	}

	@Override
	public List<String> getCategoriesCredit() {
		Query query = getCurrentSession().createQuery("select distinct catego from Operation where credit > 0");
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
	public List<AccountBalance> getSoldes() {
		Query<AccountBalance> query = getCurrentSession().createSQLQuery("select * from bank.soldes")
				.setResultTransformer(Transformers.aliasToBean(AccountBalance.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Operation> findByCategoYearMonth(Integer _year, Integer _month, String _catego, String _compte) {
		return getCurrentSession()
				.createQuery("from Operation where year=:year and month=:month and catego=:catego and compte=:compte")
				.setParameter("year", _year).setParameter("month", _month).setParameter("catego", _catego)
				.setParameter("compte", _compte).list();

	}

}
