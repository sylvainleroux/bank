package com.sleroux.bank.persistence;

import com.sleroux.bank.business.BusinessServiceAbstract;
import com.sleroux.bank.persistence.dao.book.BookDao;
import com.sleroux.bank.persistence.dao.book.BookDaoImpl;
import com.sleroux.bank.persistence.storage.xls.BookStorage;
import com.sleroux.bank.util.Config;

public class PersistenceContext {

	private boolean	configLoaded	= false;
	private BookDao	bookDao;
	private boolean	readOnly		= false;

	public static PersistenceContext getStandardInstance() {
		PersistenceContext context = new PersistenceContext();
		context.setBookDao(new BookDaoImpl());
		return context;
	}

	public void setReadOnly(boolean _readOnly) {
		readOnly = _readOnly;
	}

	public static PersistenceContext getTestExecutionContext() {
		PersistenceContext context = new PersistenceContext();
		context.setBookDao(new BookDaoImpl());
		context.setReadOnly(true);
		return context;
	}

	private void setBookDao(BookDaoImpl _bookDaoImpl) {
		bookDao = _bookDaoImpl;
	}

	public void exec(BusinessServiceAbstract _executionContextAware) {
		try {
			if (_executionContextAware.requireConfig()) {
				if (!configLoaded) {
					Config.loadProperties();
					configLoaded = true;
				}
			}
			_executionContextAware.setBookDao(bookDao);
			_executionContextAware.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void finalizeContext() {
		if (readOnly) {
			System.out.println("Read only context : do not save");
			return;
		}
		try {
			BookStorage.closeInstanceIfExits();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
