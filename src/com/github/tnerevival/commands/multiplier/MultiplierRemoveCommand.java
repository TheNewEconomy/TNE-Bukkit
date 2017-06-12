package com.github.tnerevival.commands.multiplier;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * Created by creatorfromhell on 6/12/2017.
 * All rights reserved.
 **/
public class MultiplierRemoveCommand extends TNECommand {

  public MultiplierRemoveCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "remove";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.multiplier.remove";
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
        "/multiplier remove <priority> <type(player,server,world)> [identifier(player/server/world name)]",
        "- Removes the multiplier with the specified priority",
        "- The identifier argument isn't required for the server or world types, but may be added"
    };
  }
}