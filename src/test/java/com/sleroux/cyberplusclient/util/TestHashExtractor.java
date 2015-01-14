package com.sleroux.cyberplusclient.util;

import org.junit.Test;

import com.sleroux.accountchecker.banquepopulaire.util.extractors.HashExtractor;

public class TestHashExtractor {

	@Test
	public void testHashExtractor() {
		HashExtractor hashExtractor = new HashExtractor();
		hashExtractor.extract("ipsff(function(){TW().ipthk([10, 5, 16, 7, 4, 30, 14, 14, 0]);});");
	}
}
