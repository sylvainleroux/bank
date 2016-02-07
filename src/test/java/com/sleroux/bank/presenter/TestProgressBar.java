package com.sleroux.bank.presenter;

import org.junit.Test;

public class TestProgressBar {

	@Test
	public void testProgress() throws InterruptedException {

		System.out.print("[###                 ]\r");
		Thread.sleep(1000);
		
		System.out.print("[######              ]\r");
		Thread.sleep(1000);
		System.out.print("[##########          ]\r");
	}

}
