package com.sleroux.bank.util.command;

import java.util.ArrayList;
import java.util.List;

public class Command {

	String			command;
	List<String>	parameters	= new ArrayList<String>();

	public String getCommand() {
		return command;
	}

	public void addParameter(String _s) {
		parameters.add(_s);
	}

	public void setCommand(String _command) {
		command = _command;
	}

	public List<String> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		String params = null;
		for (String s : parameters) {
			if (params == null) {
				params = s;
			} else {
				params += "," + s;
			}
		}
		return "[" + command + "|" + params + "]";
	}

}