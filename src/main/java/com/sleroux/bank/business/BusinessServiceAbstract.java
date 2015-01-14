package com.sleroux.bank.business;

import com.sleroux.bank.persistence.dao.book.BookDao;

public abstract class BusinessServiceAbstract {

	public BookDao	bookDao;

	public abstract void run() throws Exception;

	public boolean requireConfig() {
		return true;
	}

	public BookDao getBookDao() {
		return bookDao;
	}

	public void setBookDao(BookDao _bookDao) {
		bookDao = _bookDao;
	}

}