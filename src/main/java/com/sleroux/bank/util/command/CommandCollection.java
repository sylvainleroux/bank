package com.sleroux.bank.util.command;

import java.util.ArrayList;
import java.util.List;

public class CommandCollection {

	private List<Command>	commands	= new ArrayList<Command>();

	public List<Command> getCommands() {
		return commands;
	}

	public void setCommands(List<Command> _commands) {
		commands = _commands;
	}

	public boolean contains(String _appCredentials) {
		for (Command c : commands) {
			if (c.getCommand().equals(_appCredentials))
				return true;
		}
		return false;
	}

	public void add(String _command) {
		Command c = new Command();
		c.setCommand(_command);
		commands.add(c);
	}

	public Command get(String _appTest) {
		for (Command c : commands) {
			if (c.getCommand().equals(_appTest))
				return c;
		}
		return null;
	}

	public static CommandCollection getCommand(String[] _args, List<String> _apps) {
		CommandCollection commandCollection = new CommandCollection();
		Command current = null;
		for (String s : _args) {
			if (_apps.contains(s)) {
				current = new Command();
				current.setCommand(s);
				commandCollection.getCommands().add(current);
			} else {
				if (current != null) {
					current.addParameter(s);
				}
			}
		}
		return commandCollection;
	}
}
