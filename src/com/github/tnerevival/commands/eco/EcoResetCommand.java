package com.github.tnerevival.commands.eco;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Created by creatorfromhell on 5/30/2017.
 * All rights reserved.
 **/
public class EcoResetCommand extends TNECommand {

  public EcoResetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "reset";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.eco.reset";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Map<String, String> parsed = getArguments(arguments);
    String world = (parsed.containsKey("world"))? parsed.get("world") : "all";
    String currency = (parsed.containsKey("currency"))? parsed.get("currency") : "all";
    String player = (parsed.containsKey("player"))? parsed.get("player") : "all";

    TNE.instance().manager.reset(world, currency, player);

    Message reset = new Message("Messages.Admin.Reset");
    reset.addVariable("$world", world);
    reset.addVariable("$currency", currency);
    reset.addVariable("$player", player);
    reset.translate(getWorld(sender), sender);
    return true;
  }

  @Override
  public String[] getHelpLines() {
    return new String[]{
        "/eco reset [world:name] [currency:name] [player:name] - Resets all balances to their defaults.",
        "[world:name] - Resets the balances only in the world known as name",
        "[currency:name] - Resets the balances of the currency known as name",
        "[player:name] - Resets the balances of player with display name of name"
    };
  }
}