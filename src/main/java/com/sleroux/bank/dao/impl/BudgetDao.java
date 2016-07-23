package com.sleroux.bank.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IBudgetDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.domain.AggregatedOperations;
import com.sleroux.bank.model.Budget;
import com.sleroux.bank.model.budget.Changes;

@SuppressWarnings("unchecked")
@Repository
public class BudgetDao extends AbstractHibernateDao<Budget> implements IBudgetDao {

	public BudgetDao() {
		super();

		setClazz(Budget.class);
	}

	@Override
	public List<String> getCredits() {
		Query q = getCurrentSession().createQuery(
				"select distinct catego from Budget where credit > 0 and compte = :compte order by catego");
		q.setParameter("compte", "COURANT");
		return q.list();
	}

	@Override
	public List<String> getDebits() {
		Query q = getCurrentSession()
				.createQuery("select distinct catego from Budget where debit > 0 and compte = :compte order by catego");
		q.setParameter("compte", "COURANT");
		return q.list();
	}

	@Override
	public List<Integer> getYears() {
		String sql = "select distinct year from Budget order by year";
		return getCurrentSession().createQuery(sql).list();
	}

	@Override
	public List<String> getComptesEpargne() {
		String sql = "select distinct compte from Budget where compte <> 'COURANT' order by compte";
		return getCurrentSession().createQuery(sql).list();
	}

	@Override
	public List<Budget> getMonthDebits(Integer _year, int _month) {
		Query q = getCurrentSession().createSQLQuery("call bank.getMonthDebits(:year,:month)");
		q.setParameter("year", _year);
		q.setParameter("month", _month);
		q.setResultTransformer(Transformers.aliasToBean(Budget.class));

		return q.list();
	}

	@Override
	public List<Budget> getMonthCredits(Integer _year, int _month) {
		Query q = getCurrentSession().createSQLQuery("call bank.getMonthCredits(:year,:month)");
		q.setParameter("year", _year);
		q.setParameter("month", _month);
		q.setResultTransformer(Transformers.aliasToBean(Budget.class));
		return q.list();
	}

	@Override
	public Budget getBudgetForCompte(String _compte, Integer _year, int _month) {
		Query q = getCurrentSession().createSQLQuery(
				"select sum(debit) debit,sum(credit) credit from budget where compte = :compte and year= :year and month = :month");
		q.setParameter("compte", _compte);
		q.setParameter("year", _year);
		q.setParameter("month", _month);
		q.setResultTransformer(Transformers.aliasToBean(Budget.class));
		return (Budget) q.list().get(0);
	}

	@Override
	public void backupAndTruncate() {
		getCurrentSession()
				.createSQLQuery(
						"insert into bank.budget_backup(year, month, catego, debit, credit, notes, compte) select year,month, catego, debit, credit, notes, compte from bank.budget")
				.executeUpdate();

		getCurrentSession().createSQLQuery("truncate table bank.budget").executeUpdate();

	}

	@Override
	public List<Changes> getAdded() {
		String sql = "select a.id as id, a.year as year, a.month as month, a.catego as catego, a.debit as debit, a.credit as credit, a.compte as compte, a.notes as notes from budget a left join (select * from budget_backup inner join (select max(timestamp) last_backup from budget_backup) t on t.last_backup = timestamp) b on a.year = b.year and a.month = b.month and a.catego = b.catego and a.compte = b.compte where  b.timestamp is null";
		Query q = getCurrentSession().createSQLQuery(sql);
		q.setResultTransformer(Transformers.aliasToBean(Changes.class));
		return q.list();
	}

	@Override
	public List<Changes> getUpdated() {
		String sql = "select a.year year, a.month month, a.catego catego, a.compte compte, a.debit debit, a.credit credit, a.notes, b.debit oldDebit, b.credit oldCredit from budget a inner join budget_backup b on a.year = b.year and a.month = b.month and a.catego = b.catego and a.compte = b.compte inner join (select max(timestamp) last_backup from budget_backup) t on b.timestamp = t.last_backup where a.debit <> b.debit or a.credit <> b.credit;";
		Query q = getCurrentSession().createSQLQuery(sql);
		q.setResultTransformer(Transformers.aliasToBean(Changes.class));
		return q.list();
	}

	@Override
	public List<Changes> getDeleted() {
		String sql = "select a.year, a.month, a.catego, a.compte, a.debit, a.credit, a.notes from budget_backup a inner join (select max(timestamp) last_backup from budget_backup) t on a.timestamp = t.last_backup left join budget b on a.year = b.year and a.month = b.month and a.catego = b.catego and a.compte = b.compte where  b.id is null";
		Query q = getCurrentSession().createSQLQuery(sql);
		q.setResultTransformer(Transformers.aliasToBean(Changes.class));
		return q.list();
	}

	@Override
	public void saveChange(Changes _change) {

		StringBuilder sql = new StringBuilder();
		sql.append(
				"insert into bank.budget_changes (year, month, catego, compte, type, prev_value, new_value) values (");
		sql.append(_change.getYear());
		sql.append(",");
		sql.append(_change.getMonth());
		sql.append(",");
		sql.append("\"");
		sql.append(_change.getCatego());
		sql.append("\"");
		sql.append(",");
		sql.append("\"");
		sql.append(_change.getCompte());
		sql.append("\"");
		sql.append(",");
		sql.append("\"");
		sql.append((_change.isCredit() ? "CREDIT" : "DEBIT"));
		sql.append("\"");
		sql.append(",");
		sql.append(_change.isCredit() ? _change.getOldCredit() : _change.getOldDebit());
		sql.append(",");
		sql.append(_change.isCredit() ? _change.getCredit() : _change.getDebit());
		sql.append(")");

		getCurrentSession().createSQLQuery(sql.toString()).executeUpdate();

	}

	@Override
	public Budget findByYearMonthCatego(int _year, int _month, String _catego, String _compte) {
		return (Budget) getCurrentSession()
				.createQuery(
						"from Budget where year = :year and month = :month and catego = :catego and compte=:compte")
				.setParameter("compte", _compte).setParameter("year", _year).setParameter("month", _month)
				.setParameter("catego", _catego).uniqueResult();
	}

	@Override
	public List<AggregatedOperations> findBudgetForMonth(int _year, int _month) {
		Query query = getCurrentSession().createSQLQuery(
				"select year, month, compte as account, catego, sum(credit) as credit, sum(debit) as debit from budget where year = :year and month = :month group by year, month, compte, catego");
		query.setParameter("year", _year);
		query.setParameter("month", _month);
		query.setResultTransformer(Transformers.aliasToBean(AggregatedOperations.class));
		return query.list();
	}

	@Override
	public BigDecimal getEstimatedEndOfMonthBalance(int _year, int _month) {
		Query query = getCurrentSession().createSQLQuery(
				"select sum(credit) - sum(debit)  from budget where compte = 'COURANT' and date(concat(year,'-', month,'-',1)) <=  date(concat(:year,'-',:month,'-',1))");
		query.setParameter("year", _year);
		query.setParameter("month", _month);
		BigDecimal result = (BigDecimal) query.uniqueResult();
		if (result == null){
			return new BigDecimal(0);
		}
		return result;
	}

}
