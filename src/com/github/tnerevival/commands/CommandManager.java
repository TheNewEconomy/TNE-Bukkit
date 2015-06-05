package com.github.tnerevival.commands;

import java.util.ArrayList;
import java.util.List;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.bank.BankCommand;
import com.github.tnerevival.commands.company.CompanyCommand;
import com.github.tnerevival.commands.core.CoreCommand;
import com.github.tnerevival.commands.money.MoneyCommand;

public class CommandManager {
	
	public List<TNECommand> commands = new ArrayList<TNECommand>();
	
	public CommandManager() {
		commands.add(new BankCommand(TNE.instance));
		commands.add(new CompanyCommand(TNE.instance));
		commands.add(new CoreCommand(TNE.instance));
		commands.add(new MoneyCommand(TNE.instance));
	}
	
	public TNECommand Find(String name) {
		for(TNECommand c : commands) {
			if(c.getName().equalsIgnoreCase(name)) {
				return c;
			}
		}
		for(TNECommand c : commands) {
			for(String s : c.getAliases()) {
				if(s.equalsIgnoreCase(name)) {
					return c;
				}
			}
		}
		return null;
	}
}