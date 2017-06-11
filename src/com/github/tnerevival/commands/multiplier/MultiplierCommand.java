package com.github.tnerevival.commands.multiplier;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;

/**
 * Created by creatorfromhell on 6/11/2017.
 * All rights reserved.
 **/
public class MultiplierCommand extends TNECommand {

  public MultiplierCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "multiplier";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.multiplier.command";
  }

  @Override
  public boolean console() {
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