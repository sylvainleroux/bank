package com.sleroux.accountchecker.banquepopulaire.util.notifier;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsoleNotifier implements Notifier {

	private final static long			DELAY	= 250;
	private ScheduledExecutorService	executorService;
	private Future<Void>				future;
	
	private int							totalActions;
	private int							countActions;
	private String						name;

	@Override
	public void startAction(String _actionName) {
		if (executorService != null) {
			try {
				executorService.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		executorService = Executors.newSingleThreadScheduledExecutor();
		System.out.printf("[" + name +  "] " + _actionName + " (%d/%d) ", ++countActions, totalActions);
		executorService.schedule(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				System.out.print(".");
				future = executorService.schedule(this, DELAY, TimeUnit.MILLISECONDS);
				return null;
			}
		}, 0, TimeUnit.MILLISECONDS);
	}

	@Override
	public void finishAction(String _content) {
		try {
			future.cancel(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print(" ok.\n");
	}

	@Override
	public void finalize() {
		try {
			future.cancel(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fail(Exception _e) {
		System.out.print(" fail.\n");
		System.out.print(_e);
		finalize();
	}


	@Override
	public void initCounter(String _name, int _totalActions) {
		name = _name;
		totalActions = _totalActions;
		countActions = 0;
	}
}
