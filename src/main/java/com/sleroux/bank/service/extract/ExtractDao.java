package com.sleroux.bank.service.extract;

import java.util.List;

import com.sleroux.bank.model.extract.ExtractDocument;

public interface ExtractDao {

	List<ExtractDocument> getAll();

	void markAsProcessed(ExtractDocument _extract);

}
