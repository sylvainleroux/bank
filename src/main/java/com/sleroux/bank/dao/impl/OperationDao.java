package com.sleroux.bank.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IOperationDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.AccountBalance;
import com.sleroux.bank.model.CalcResult;
import com.sleroux.bank.model.Operation;

@Repository
public class OperationDao extends AbstractHibernateDao<Operation> implements IOperationDao {

	public OperationDao() {
		super();

		setClazz(Operation.class);
	}

	@Override
	public void doBackup() {

		StringBuilder sql = new StringBuilder("insert into operation_backup");
		sql.append("(compte, date_operation, date_valeur, libelle, montant, catego, year, month_bank, month_adjusted) ");
		sql.append("select compte, date_operation, date_valeur, libelle, montant, catego, year, month_bank, month_adjusted from operation");

		Query query = getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();

	}

	@Override
	@Transactional
	public int insertIgnore(Operation _o) {

		String sql = "INSERT IGNORE into operation (compte, date_operation, date_valeur, libelle, montant)";
		sql += "VALUES (:compte, :date_operation, :date_valeur, :libelle, :montant)";

		Query query = getCurrentSession().createSQLQuery(sql);
		query.setParameter("compte", _o.getCompte());
		query.setParameter("date_operation", _o.getDateOperation());
		query.setParameter("date_valeur", _o.getDateValeur());
		query.setParameter("libelle", _o.getLibelle());
		query.setParameter("montant", _o.getMontant());
		
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Operation> findUncategorized() {
		return getCurrentSession().createQuery("from Operation where catego is null").list();
	}

	@Override
	public List<String> getCategoriesDebit() {
		Query query = getCurrentSession().createQuery("select distinct catego from Operation where montant < 0");
		@SuppressWarnings("unchecked")
		List<String> list = query.list();
		return list;
	}

	@Override
	public List<String> getCategoriesCredit() {
		Query query = getCurrentSession().createQuery("select distinct catego from Operation where montant > 0");
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
	public List<CalcResult> getCalcForMonth(int _year, int _month) {
		Query query = getCurrentSession()
				.createSQLQuery(
						"select catego, CASE WHEN is_credit THEN TRUE ELSE FALSE END as credit, if (is_credit, ops_credit, ops_debit) as ops, if (is_credit, bud_credit, bud_debit) as bud from diff where year = :year and month = :month order by catego");
		query.setParameter("month", _month);
		query.setParameter("year", _year);
		query.setResultTransformer(Transformers.aliasToBean(CalcResult.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountBalance> getSoldes() {
		Query query = getCurrentSession().createSQLQuery("select * from bank.soldes").setResultTransformer(
				Transformers.aliasToBean(AccountBalance.class));
		return query.list();
	}

}
