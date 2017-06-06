package com.github.tnerevival.commands.multiplier;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * Created by creatorfromhell on 6/6/2017.
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
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    return false;
  }

  @Override
  public String getHelp() {
    return "/multiplier set <reward(all, mob, block)> <type(date,timed, month, day)> <time period/date> <multiplier> [associated w/(player, server, world):identifier] - Set reward multiplier.";
  }
}