package com.sleroux.bank.persistence.dao.filter;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleroux.bank.model.filter.FilterCollection;
import com.sleroux.bank.util.Config;

public class FiltersDaoImpl implements FiltersDao {

	private ObjectMapper	mapper	= new ObjectMapper();

	public FiltersDaoImpl() {

	}

	@Override
	public FilterCollection getAll() throws Exception {

		URL url = this.getClass().getResource("filters.json");
		File filters = new File(url.getFile());
		if (filters.exists()) {
			try {
				FilterCollection filterCollection = mapper.readValue(filters, FilterCollection.class);
				File customFilters = new File(Config.getMainDocumentPath() + File.separator + Config.getFilterFileName());
				if (!customFilters.exists()) {
					Logger.getRootLogger().warn("No filter config found in \"" + filters.getAbsolutePath() + "\".");
				} else {
					FilterCollection custom = mapper.readValue(customFilters, FilterCollection.class);
					filterCollection.merge(custom);
				}
				return filterCollection;
			} catch (Exception e) {
				throw new Exception("Unable to load filters", e);
			}
		}
		return new FilterCollection();
	}
}
