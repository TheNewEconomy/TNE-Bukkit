package com.github.tnerevival.commands.multiplier;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * Created by creatorfromhell on 6/11/2017.
 * All rights reserved.
 **/
public class MultiplierSetCommand extends TNECommand {

  public MultiplierSetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "set";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.multiplier.set";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    return true;
  }
  @Override
  public String getHelp() {
    return "/multiplier set <type(player,server,world)> <identifier(player/server/world name)> <time period/date> <multiplier> - Set the multiplier";
  }
}