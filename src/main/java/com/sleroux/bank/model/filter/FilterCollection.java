package com.sleroux.bank.model.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class FilterCollection implements Serializable {

	private List<Filter>	filters	= new ArrayList<>();

	public FilterCollection() {
		// Empty
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> _filters) {
		filters = _filters;
	}

	public void merge(FilterCollection _custom) {
		filters.addAll(_custom.getFilters());
	}

}
