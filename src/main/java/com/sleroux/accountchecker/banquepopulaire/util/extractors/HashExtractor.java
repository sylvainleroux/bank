package com.sleroux.accountchecker.banquepopulaire.util.extractors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashExtractor implements ContentExtractor<List<Integer>> {

	private List<Integer>		hash	= new ArrayList<>();
	public final static String	NAME	= "hash";

	@Override
	public boolean extract(String _line) {
		if (hash.size() > 0) {
			return true;
		}
		if (_line.contains("ipsff(function(){TW().ipthk")) {
			Pattern p = Pattern.compile("([0-9]*)[, ]*");
			Matcher m = p.matcher(_line);
			while (m.find()) {
				if (m.group(1) != null && !m.group(1).equals("")) {
					hash.add(new Integer(m.group(1)));
				}
			}
		}
		return hash.size() > 0;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<Integer> getValue() {
		return hash;
	}

}
