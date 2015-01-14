package com.sleroux.bank.persistence.dao.filter;

import com.sleroux.bank.model.filter.FilterCollection;

public interface FiltersDao {

	public FilterCollection getAll() throws Exception;

}
