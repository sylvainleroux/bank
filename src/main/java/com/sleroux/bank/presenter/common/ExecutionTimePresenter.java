package com.sleroux.bank.presenter.common;

import java.util.Date;

public class ExecutionTimePresenter {

	public static void show(Date _start, Date _date) {
		long diffMs = _date.getTime() - _start.getTime();
		
		System.out.println("Completed in : " + diffMs + "ms");
	}

}
