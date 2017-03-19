package com.sleroux.bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.junit.Test;

import com.sleroux.bank.util.Encryption;

public class TestEncrypt {

	@Test
	public void testEncrypt() throws GeneralSecurityException, IOException {
		String a = "avion120972%";
		String s = Encryption.encrypt(a);
		System.out.println(s);
		String t = Encryption.decrypt(s);
		assertTrue(a.equals(t));
	}

	@Test
	public void testAES() throws Exception {

		String password = "fakePassword987873";
		String key = Encryption.sha1(password + "salt989873");
		String content = "Lorem ipsum dolor sit amet";
		String enc = Encryption.mysqlAesEncrypt(content, key);
		String result = Encryption.mysqlAesDecrypt(enc, key);
		assertEquals(content, result);
	}

}
