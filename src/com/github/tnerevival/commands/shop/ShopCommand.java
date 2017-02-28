package com.github.tnerevival.commands.shop;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

public class ShopCommand extends TNECommand {

  public ShopCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new ShopAddCommand(plugin));
    subCommands.add(new ShopBlacklistCommand(plugin));
    subCommands.add(new ShopBrowseCommand(plugin));
    subCommands.add(new ShopCloseCommand(plugin));
    subCommands.add(new ShopCreateCommand(plugin));
    subCommands.add(new ShopRemoveCommand(plugin));
    subCommands.add(new ShopShareCommand(plugin));
    subCommands.add(new ShopStockCommand(plugin));
    subCommands.add(new ShopToggleCommand(plugin));
    subCommands.add(new ShopWhitelistCommand(plugin));
  }

  @Override
  public String getName() {
    return "shop";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "s" };
  }

  @Override
  public String getNode() {
    return "tne.shop.command";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public Boolean activated(String world, String player) {
    return TNE.instance().api().getBoolean("Core.Shops.Enabled", world, player);
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    return super.execute(sender, command, arguments);
  }
}