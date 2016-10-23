package com.github.tnerevival.commands.pin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

public class PinCommand extends TNECommand {

  public PinCommand(TNE plugin) {
    super(plugin);

    subCommands.add(new PinSetCommand(plugin));
    subCommands.add(new PinConfirmCommand(plugin));
  }

  @Override
  public String getName() {
    return "pin";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.pin";
  }

  @Override
  public boolean console() {
    return false;
  }


  @Override
  public Boolean activated(String world, String player) {
    return TNE.instance.api.getBoolean("Core.Pins.Enabled", world, player);
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    return super.execute(sender, command, arguments);
  }

}