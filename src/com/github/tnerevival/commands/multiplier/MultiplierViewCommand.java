package com.github.tnerevival.commands.multiplier;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * Created by creatorfromhell on 6/12/2017.
 * All rights reserved.
 **/
public class MultiplierViewCommand extends TNECommand {

  public MultiplierViewCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "view";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.multiplier.view";
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
  public String[] getHelpLines() {
    return new String[] {
        "/multiplier view <type(player,server,world)> [identifier(player/server/world name)]",
        "- Shows a list of multipliers set for the specified type"
    };
  }
}