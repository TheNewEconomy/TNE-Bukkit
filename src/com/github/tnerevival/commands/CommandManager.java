package com.github.tnerevival.commands;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.admin.AdminCommand;
import com.github.tnerevival.commands.bank.BankCommand;
import com.github.tnerevival.commands.credit.CreditCommand;
import com.github.tnerevival.commands.money.MoneyCommand;
import com.github.tnerevival.commands.packages.PackageCommand;
import com.github.tnerevival.commands.pin.PinCommand;
import com.github.tnerevival.commands.shop.ShopCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
	
	public List<TNECommand> commands = new ArrayList<TNECommand>();
	
	public CommandManager() {
		commands.add(new BankCommand(TNE.instance));
		commands.add(new AdminCommand(TNE.instance));
		commands.add(new MoneyCommand(TNE.instance));
		commands.add(new PackageCommand(TNE.instance));
		commands.add(new CreditCommand(TNE.instance));
		commands.add(new PinCommand(TNE.instance));
		commands.add(new ShopCommand(TNE.instance));
	}

	public TNECommand Find(String name) {
		for(TNECommand c : commands) {
		  if(name.equalsIgnoreCase("pay") && c.getName().equalsIgnoreCase("money") && TNE.instance.api.getBoolean("Core.Commands.PayShort")) return c;
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