package com.sleroux.bank.service.impl;

import java.util.ArrayList;

public class BPOReport {

	private String					exportType;

	private ArrayList<BPOOperation>	content	= new ArrayList<>();

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String _exportType) {
		exportType = _exportType;
	}

	public ArrayList<BPOOperation> getContent() {
		return content;
	}

	public void setContent(ArrayList<BPOOperation> _content) {
		content = _content;
	}

}
