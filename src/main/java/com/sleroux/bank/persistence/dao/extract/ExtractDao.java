package com.sleroux.bank.persistence.dao.extract;

import java.util.List;

import com.sleroux.bank.model.fileimport.ExtractDocument;

public interface ExtractDao {

	List<ExtractDocument> getAll();

	void markAsProcessed(ExtractDocument _extract);

}
