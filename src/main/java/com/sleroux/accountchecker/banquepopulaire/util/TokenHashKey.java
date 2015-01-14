package com.sleroux.accountchecker.banquepopulaire.util;

import java.util.List;

public class TokenHashKey {

	public static String getTokenHashKey(String _token, List<Integer> _hash) {
		StringBuilder sb = new StringBuilder(_token);
		for (Integer i : _hash) {
			sb.append(_token.charAt(i));
		}
		return sb.toString();
	}
}
