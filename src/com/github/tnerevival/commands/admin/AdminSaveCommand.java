package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

public class AdminSaveCommand extends TNECommand {

  public AdminSaveCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "save";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.save";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    this.plugin.saveManager.save();
    sender.sendMessage("Successfully saved all TNE Data!");
    return true;
  }

  @Override
  public String getHelp() {
    return "/theneweconomy save - force saves all TNE data";
  }
}