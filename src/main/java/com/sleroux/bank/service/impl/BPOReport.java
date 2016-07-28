package com.sleroux.bank.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BPOReport {

	private String					exportType;

	private BigDecimal				solde;

	private ArrayList<BPOOperation>	content	= new ArrayList<>();

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String _exportType) {
		exportType = _exportType;
	}

	public BigDecimal getSolde() {
		return solde;
	}

	public void setSolde(BigDecimal _solde) {
		solde = _solde;
	}

	public ArrayList<BPOOperation> getContent() {
		return content;
	}

	public void setContent(ArrayList<BPOOperation> _content) {
		content = _content;
	}
	
}
