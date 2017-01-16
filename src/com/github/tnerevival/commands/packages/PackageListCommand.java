package com.github.tnerevival.commands.packages;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.objects.TNEAccessPackage;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PackageListCommand extends TNECommand {

  public PackageListCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "list";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.package.list";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length == 1) {
      Player player = (Player)sender;
      List<TNEAccessPackage> packages = TNE.configurations.getObjectConfiguration().getInventoryPackages(arguments[0], MISCUtils.getWorld(player), IDFinder.getID(player).toString());
      sender.sendMessage(ChatColor.WHITE + "Name ~ Cost ~ Seconds Provided");
      sender.sendMessage(ChatColor.WHITE + "==============================");
      if(packages.size() > 0) {
        for(TNEAccessPackage p : packages) {
          sender.sendMessage(ChatColor.WHITE + p.getName() + " ~ " + CurrencyFormatter.format(MISCUtils.getWorld(player), p.getCost()) + ChatColor.WHITE + " ~ " + p.getTime());
        }
        return true;
      } else {
        Message insufficient = new Message("Messages.Package.Empty");
        insufficient.addVariable("$type",  arguments[0]);
        insufficient.translate(MISCUtils.getWorld(player), player);
        return false;
      }
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/package list <type> - List all packages for the specified inventory <type>";
  }
}