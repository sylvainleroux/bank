package com.sleroux.bank;

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

}
