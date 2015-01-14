package com.sleroux.accountchecker.banquepopulaire.util.notifier;

public interface Notifier {
	public void startAction(String _name);

	public void finishAction(String _content);

	public void finalize();

	public void fail(Exception _e);

	public void initCounter(String _name, int _totalActions);
}
