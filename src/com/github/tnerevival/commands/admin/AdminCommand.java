package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

public class AdminCommand extends TNECommand {

  public AdminCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new AdminBackupCommand(plugin));
    subCommands.add(new AdminBalanceCommand(plugin));
    subCommands.add(new AdminBankCommand(plugin));
    subCommands.add(new AdminCreateCommand(plugin));
    subCommands.add(new AdminDeleteCommand(plugin));
    subCommands.add(new AdminHistoryCommand(plugin));
    subCommands.add(new AdminIDCommand(plugin));
    subCommands.add(new AdminPurgeCommand(plugin));
    subCommands.add(new AdminRecreateCommand(plugin));
    subCommands.add(new AdminReloadCommand(plugin));
    subCommands.add(new AdminPinCommand(plugin));
    subCommands.add(new AdminSaveCommand(plugin));
    subCommands.add(new AdminStatusCommand(plugin));
  }

  @Override
  public String getName() {
    return "theneweconomy";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "tne" };
  }

  @Override
  public String getNode() {
    return "tne.admin";
  }

  @Override
  public boolean console() {
    return true;
  }
}