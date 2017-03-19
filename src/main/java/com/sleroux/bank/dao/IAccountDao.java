package com.sleroux.bank.dao;

import java.util.List;

import com.sleroux.bank.dao.common.IOperations;
import com.sleroux.bank.model.Account;

public interface IAccountDao extends IOperations<Account> {

	List<Account> findByUserID(long _userID);

}
