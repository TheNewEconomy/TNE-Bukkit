package com.github.tnerevival.commands.multiplier;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.commands.packages.PackageBuyCommand;
import com.github.tnerevival.commands.packages.PackageListCommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;

/**
 * Created by creatorfromhell on 6/6/2017.
 * All rights reserved.
 **/
public class MultiplierCommand extends TNECommand {

  public MultiplierCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new PackageListCommand(plugin));
    subCommands.add(new PackageBuyCommand(plugin));
  }

  @Override
  public String getName() {
    return "multiplier";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "mult"
    };
  }

  @Override
  public String getNode() {
    return "tne.multiplier";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public Boolean confirm() { return true; }

  @Override
  public Boolean locked() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(MISCUtils.ecoDisabled(getWorld(sender))) {
      Message disabled = new Message("Messages.General.Disabled");
      disabled.translate(getWorld(sender), sender);
      return false;
    }
    return super.execute(sender, command, arguments);
  }
}