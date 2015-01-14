package com.sleroux.cyberplusclient.util;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.sleroux.accountchecker.banquepopulaire.util.TokenHashKey;

public class TestGetTokenHashKey {

	@Test
	public void test() {
		String token = TokenHashKey.getTokenHashKey("dc0fc5422f214b68b0e7bc15b0d965f7", Arrays.asList(6));
		Assert.assertEquals("dc0fc5422f214b68b0e7bc15b0d965f74", token);
	}

}
