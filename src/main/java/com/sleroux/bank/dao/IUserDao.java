package com.sleroux.bank.dao;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.model.User;

public interface IUserDao extends IOperations<User> {
	
	User getUserByUsername(String _username);

}
