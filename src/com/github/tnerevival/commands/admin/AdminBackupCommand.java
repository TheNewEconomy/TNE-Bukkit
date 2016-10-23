package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class AdminBackupCommand extends TNECommand {

  public AdminBackupCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "backup";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.backup";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    try {
      if(this.plugin.saveManager.backupDatabase()) {
        this.plugin.saveManager.save();
        sender.sendMessage("Successfully backed up all TNE Data!");
        return true;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public String getHelp() {
    return "/theneweconomy backup - Saves & back ups the TNE Database file.(currently only FlatFile and SQLITE)";
  }
}