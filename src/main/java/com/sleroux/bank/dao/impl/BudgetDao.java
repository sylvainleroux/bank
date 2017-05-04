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

@SuppressWarnings("unchecked")
@Repository
public class BudgetDao extends AbstractHibernateDao<Budget> implements IBudgetDao {

	public BudgetDao() {
		super();

		setClazz(Budget.class);
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
		if (result == null) {
			return new BigDecimal(0);
		}
		return result;
	}

}
