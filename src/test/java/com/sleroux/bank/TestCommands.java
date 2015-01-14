package com.sleroux.bank;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.sleroux.bank.util.command.CommandCollection;

public class TestCommands {

	@Test
	public void testCommands() {
		List<String> apps = Bank.getApps();
		{
			String[] args = {};
			CommandCollection c = CommandCollection.getCommand(args, apps);
			assertTrue(c.getCommands().size() == 0);
		}
		{
			String[] args = { "import" };
			CommandCollection commandCollection = CommandCollection.getCommand(args, apps);
			assertTrue(commandCollection.getCommands().size() == 1);
			assertTrue(commandCollection.getCommands().get(0).toString().equals("[import|null]"));
		}
		{
			String[] args = { "import", "-123", "+pomme", "avion" };
			CommandCollection commandCollection = CommandCollection.getCommand(args, apps);
			assertTrue(commandCollection.getCommands().size() == 1);
			assertTrue(commandCollection.getCommands().get(0).toString().equals("[import|-123,+pomme,avion]"));
		}
		{
			String[] args = { "import", "-123", "+pomme", "catego" };
			CommandCollection commandCollection = CommandCollection.getCommand(args, apps);
			assertTrue(commandCollection.getCommands().size() == 2);
		}
	}
}
