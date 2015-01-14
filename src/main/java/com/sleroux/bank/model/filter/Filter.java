package com.sleroux.bank.model.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Filter implements Serializable {

	public Filter() {
		// Empty
	}

	// debit or credit
	private String			type		= "undefined";
	private List<String>	startsWith	= new ArrayList<>();
	private List<String>	contains	= new ArrayList<>();
	private List<String>	reference	= new ArrayList<>();
	private String			catego;

	public List<String> getReference() {
		return reference;
	}

	public String getType() {
		return type;
	}

	public void setType(String _type) {
		type = _type;
	}

	public List<String> getStartsWith() {
		return startsWith;
	}

	public void setStartsWith(List<String> _startsWith) {
		startsWith = _startsWith;
	}

	public List<String> getContains() {
		return contains;
	}

	public void setContains(List<String> _contains) {
		contains = _contains;
	}

	public String getCatego() {
		return catego;
	}

	public void setCatego(String _catego) {
		catego = _catego;
	}

	public void setReference(List<String> _reference) {
		reference = _reference;
	}

	@Override
	public String toString() {
		return "[" + startsWith + "," + contains + "," + reference + "," + catego + "]";
	}

}
