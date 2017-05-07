package com.sleroux.bank.dao.common;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public abstract class AbstractHibernateDao<T extends Serializable> implements IOperations<T> {
	private Class<T>		clazz;

	@Autowired
	private SessionFactory	sessionFactory;

	// API

	protected final void setClazz(final Class<T> clazzToSet) {
		clazz = clazzToSet;
	}

	@Override
	public final T findOne(final long id) {
		return (T) getCurrentSession().get(clazz, id);
	}

	@Override
	public final List<T> findAll() {
		return getCurrentSession().createQuery("from " + clazz.getName()).list();
	}

	@Override
	public final void create(final T entity) {
		// getCurrentSession().persist(entity);
		getCurrentSession().saveOrUpdate(entity);
	}

	@Override
	public final T update(final T entity) {
		return (T) getCurrentSession().merge(entity);
	}

	@Override
	public final void delete(final T entity) {
		getCurrentSession().delete(entity);
	}

	@Override
	public final void deleteById(final long entityId) {
		final T entity = findOne(entityId);
		delete(entity);
	}

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}