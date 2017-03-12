package com.sleroux.bank.dao.impl;

import org.springframework.stereotype.Repository;

import com.sleroux.bank.dao.IUserDao;
import com.sleroux.bank.dao.common.AbstractHibernateDao;
import com.sleroux.bank.model.User;

@Repository
public class UserDao extends AbstractHibernateDao<User> implements IUserDao {

	public UserDao() {
		super();
		setClazz(User.class);
	}

	@Override
	public User getUserByUsername(String _username) {
		return (User) getCurrentSession().createQuery("from User where username = :username")
				.setParameter("username", _username).uniqueResult();
	}

}
