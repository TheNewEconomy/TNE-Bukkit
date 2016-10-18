package com.github.tnerevival.commands;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.admin.AdminCommand;
import com.github.tnerevival.commands.auction.AuctionCommand;
import com.github.tnerevival.commands.bank.BankCommand;
import com.github.tnerevival.commands.credit.CreditCommand;
import com.github.tnerevival.commands.money.MoneyCommand;
import com.github.tnerevival.commands.packages.PackageCommand;
import com.github.tnerevival.commands.pin.PinCommand;
import com.github.tnerevival.commands.shop.ShopCommand;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommandManager {

  public Map<String[], TNECommand> commands = new HashMap<>();
	//public List<TNECommand> commands = new ArrayList<>();
  private Field commandMap = null;
  private Field knownCommands = null;
	
	public CommandManager() {
		commands.put(new String[] { "theneweconomy", "tne" }, new AdminCommand(TNE.instance));
		commands.put(new String[] { "auction" }, new AuctionCommand(TNE.instance));
    commands.put(new String[] { "bank" }, new BankCommand(TNE.instance));
    commands.put(new String[] { "credit" }, new CreditCommand(TNE.instance));
		commands.put(new String[] { "money", "bal", "balance" }, new MoneyCommand(TNE.instance));
		commands.put(new String[] { "package" }, new PackageCommand(TNE.instance));
		commands.put(new String[] { "pin" }, new PinCommand(TNE.instance));
		commands.put(new String[] { "shop" }, new ShopCommand(TNE.instance));
    registerCommands();
	}

	private void registerCommands() {
    try {
      commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
      commandMap.setAccessible(true);
      knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
      knownCommands.setAccessible(true);
    } catch (Exception e) {
			/* do nothing */
    }

    if(commandMap != null && knownCommands != null) {

      Iterator<Map.Entry<String[], TNECommand>> i = commands.entrySet().iterator();

      while(i.hasNext()) {
        Map.Entry<String[], TNECommand> entry = i.next();

        for (String s : entry.getKey()) {
          if(entry.getValue().activated("", "")) {
            if(registered(s)) {
              try {
                ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(s);
              } catch(Exception e) {
                //nothing to see here;
              }
            }
            try {
              PluginCommand command = null;
              Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
              c.setAccessible(true);
              command = c.newInstance(s, TNE.instance);
              if(command != null) {
                ((SimpleCommandMap) commandMap.get(Bukkit.getServer())).register(s, command);
                MISCUtils.debug("Registered command " + s);
              }
            } catch(Exception e) {
              //nothing to see here;
            }
          }
        }
      }
    }
  }

  private void unregister() {

  }

  private Boolean registered(String command) {
    try {
      return ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).containsKey(command);
    } catch(Exception e) {
      //nothing to see here;
    }
    return false;
  }

	public TNECommand Find(String name) {
		for(TNECommand c : commands.values()) {
		  if(name.equalsIgnoreCase("pay") && c.getName().equalsIgnoreCase("money") && TNE.instance.api.getBoolean("Core.Commands.PayShort")) return c;
		  if(name.equalsIgnoreCase("balance") && c.getName().equalsIgnoreCase("money") && TNE.instance.api.getBoolean("Core.Commands.PayShort")) return c;
		  if(name.equalsIgnoreCase("bal") && c.getName().equalsIgnoreCase("money") && TNE.instance.api.getBoolean("Core.Commands.PayShort")) return c;
			if(c.getName().equalsIgnoreCase(name)) {
				return c;
			}
		}
		for(TNECommand c : commands.values()) {
			for(String s : c.getAliases()) {
				if(s.equalsIgnoreCase(name)) {
					return c;
				}
			}
		}
		return null;
	}
}