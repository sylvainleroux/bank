package com.sleroux.bank.model.extract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sleroux.bank.service.impl.BPOOperation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractData {

	@JsonProperty("export_type")
	private String				exportType	= "";
	private List<String>		files		= new ArrayList<>();
	private String				cookie		= "";
	private String				referer		= "";
	private List<Balance>		balance		= new ArrayList<>();
	private List<BPOOperation>	content		= new ArrayList<>();

	private String				rawOutput	= "";

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> _files) {
		files = _files;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String _cookie) {
		cookie = _cookie;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String _referer) {
		referer = _referer;
	}

	public List<Balance> getBalance() {
		return balance;
	}

	public void setBalance(List<Balance> _balance) {
		balance = _balance;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String _exportType) {
		exportType = _exportType;
	}

	public List<BPOOperation> getContent() {
		return content;
	}

	public void setContent(List<BPOOperation> _content) {
		content = _content;
	}

	public void setRawOutput(String _rawOutput) {
		rawOutput = _rawOutput;
	}

	public String getRawOutput() {
		return rawOutput;
	}

}
