package com.sleroux.accountchecker.banquepopulaire.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class PostData {

	public class NameValue {
		private String	name;
		private String	value;
		public NameValue(String _name, String _value) {
			name = _name;
			value = _value;
		}
	}

	private List<NameValue>	data	= new ArrayList<>();
	public PostData add(String _name, String _value) {
		data.add(new NameValue(_name, _value));
		return this;
	}

	public String getQueryString() throws UnsupportedEncodingException {
		StringBuilder result = null;
		for (NameValue nameValue : data) {
			if (result == null) {
				result = new StringBuilder();
			} else {
				result.append("&");
			}
			result.append(URLEncoder.encode(nameValue.name, "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(nameValue.value, "UTF-8"));
		}
		return result.toString();
	}
}
