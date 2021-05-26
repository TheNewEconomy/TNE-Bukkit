package net.tnemc.core.common.api;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.tnemc.core.TNE;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 8/3/2017.
 */
public class Economy_TheNewEconomy implements Economy {

  private TNE plugin = null;
  private TNEAPI api = null;

  public Economy_TheNewEconomy(TNE plugin) {
    this.plugin = plugin;
    this.api = plugin.api();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getName() {
    return "TheNewEconomy";
  }

  @Override
  public boolean hasBankSupport() {
    return TNE.loader().hasModule("Banks");
  }

  @Override
  public int fractionalDigits() {
    return 2;
  }

  @Override
  public String format(double amount) {
    return api.format(new BigDecimal(amount + ""), plugin.defaultWorld);
  }

  @Override
  public String currencyNamePlural() {
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld).plural();
  }

  @Override
  public String currencyNameSingular() {
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name();
  }

  @Override
  public boolean hasAccount(String username) {
    TNE.debug("Economy_TheNewEconomy.hasAccount:" + username + " Has?" + api.hasAccount(username));
    return api.hasAccount(username);
  }

  @Override
  public boolean hasAccount(OfflinePlayer offlinePlayer) {
    return api.hasAccount(offlinePlayer.getUniqueId());
  }

  @Override
  public boolean hasAccount(String username, String world) {
    return api.hasAccount(username);
  }

  @Override
  public boolean hasAccount(OfflinePlayer offlinePlayer, String world) {
    return api.hasAccount(offlinePlayer.getUniqueId());
  }

  @Override
  public double getBalance(String username) {
    return getBalance(username, TNE.instance().defaultWorld);
  }

  @Override
  public double getBalance(OfflinePlayer offlinePlayer) {
    TNE.debug("Economy_TheNewEconomy.getBalance(offlinePlayer)");
    TNE.debug("username: " + offlinePlayer.getName());
    TNE.debug("id: " + offlinePlayer.getUniqueId().toString());
    return api.getHoldings(offlinePlayer.getUniqueId().toString(), TNE.instance().defaultWorld).doubleValue();
  }

  @Override
  public double getBalance(String username, String world) {
    return api.getHoldings(username, world).doubleValue();
  }

  @Override
  public double getBalance(OfflinePlayer offlinePlayer, String world) {
    TNE.debug("Economy_TheNewEconomy.getBalance(offlinePlayer, world)");
    TNE.debug("username: " + offlinePlayer.getName());
    TNE.debug("id: " + offlinePlayer.getUniqueId().toString());
    TNE.debug("world: " + world);

    if(offlinePlayer.getName().contains("faction-")) {
      return getBalance(offlinePlayer.getName(), world);
    }

    return api.getHoldings(offlinePlayer.getUniqueId().toString(), world).doubleValue();
  }

  @Override
  public boolean has(String username, double amount) {
    TNE.debug("Economy_TheNewEconomy.has(username, amount)");
    TNE.debug("username: " + username);
    TNE.debug("Amount: " + amount);

    return has(username, TNE.instance().defaultWorld, amount);
  }

  @Override
  public boolean has(OfflinePlayer offlinePlayer, double amount) {
    TNE.debug("Economy_TheNewEconomy.has(offlinePlayer, amount)");
    TNE.debug("username: " + offlinePlayer.getName());
    TNE.debug("Amount: " + amount);
    String world = TNE.instance().defaultWorld;
    if(offlinePlayer.isOnline()) {
      world = offlinePlayer.getPlayer().getWorld().getName();
    }

    if(offlinePlayer.getName().contains("faction-")) {
      return has(offlinePlayer.getName(), world, amount);
    }

    return has(offlinePlayer.getUniqueId().toString(), world, amount);
  }

  @Override
  public boolean has(String username, String world, double amount) {
    TNE.debug("Economy_TheNewEconomy.has(username, world, amount)");
    TNE.debug("username: " + username);
    TNE.debug("world: " + world);
    TNE.debug("Amount: " + amount);
    return api.hasHoldings(username, new BigDecimal(amount + ""), world);
  }

  @Override
  public boolean has(OfflinePlayer offlinePlayer, String world, double amount) {
    TNE.debug("Economy_TheNewEconomy.has(offlinePlayer, world, amount)");
    TNE.debug("username: " + offlinePlayer.getName());
    TNE.debug("id: " + offlinePlayer.getUniqueId().toString());
    TNE.debug("world: " + world);
    TNE.debug("Amount: " + amount);

    if(offlinePlayer.getName().contains("faction-")) {
      return has(offlinePlayer.getName(), world, amount);
    }
    return has(offlinePlayer.getUniqueId().toString(), world, amount);
  }

  @Override
  public EconomyResponse withdrawPlayer(String username, double amount) {
    String world = TNE.instance().defaultWorld;
    final Player player = Bukkit.getPlayer(username);
    if(player != null) {
      world = player.getWorld().getName();
    }
    TNE.debug("Economy_TheNewEconomy.withdrawPlayer(username, amount)");
    TNE.debug("username: " + username);
    TNE.debug("world: " + world);
    TNE.debug("Amount: " + amount);
    return withdrawPlayer(username, world, amount);
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
    String world = TNE.instance().defaultWorld;
    if(offlinePlayer.isOnline()) {
      world = offlinePlayer.getPlayer().getWorld().getName();
    }

    TNE.debug("Economy_TheNewEconomy.withdrawPlayer(offlinePlayer, amount)");
    TNE.debug("username: " + offlinePlayer.getName());
    TNE.debug("id: " + offlinePlayer.getUniqueId().toString());
    TNE.debug("world: " + world);
    TNE.debug("Amount: " + amount);

    if(offlinePlayer.getName().contains("faction-")) {
      return withdrawPlayer(offlinePlayer.getName(), world, amount);
    }

    return withdrawPlayer(offlinePlayer.getUniqueId().toString(), world, amount);
  }

  @Override
  public EconomyResponse withdrawPlayer(String username, String world, double amount) {
    TNE.debug("Economy_TheNewEconomy.withdrawPlayer(username, world, amount)");
    TNE.debug("username: " + username);
    TNE.debug("world: " + world);
    TNE.debug("Amount: " + amount);
    if(TNE.maintenance) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Economy is in maintenance mode.");
    if(!hasAccount(username)) {
      return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That account does not exist!");
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative amounts.");
    }

    if(!has(username, world, amount)) {
      return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Insufficient funds!");
    }
    if(api.removeHoldings(username, new BigDecimal(amount + ""), world)) {
      return new EconomyResponse(amount, getBalance(username, world), EconomyResponse.ResponseType.SUCCESS, "");
    }
    return new EconomyResponse(amount, getBalance(username, world), EconomyResponse.ResponseType.FAILURE, "Unable to complete transaction!");
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
    TNE.debug("Economy_TheNewEconomy.withdrawPlayer(offlinePlayer, world, amount)");
    TNE.debug("username: " + offlinePlayer.getName());
    TNE.debug("id: " + offlinePlayer.getUniqueId().toString());
    TNE.debug("world: " + world);
    TNE.debug("Amount: " + amount);
    return withdrawPlayer(offlinePlayer.getUniqueId().toString(), world, amount);
  }

  @Override
  public EconomyResponse depositPlayer(String username, double amount) {
    TNE.debug("Economy_TheNewEconomy.depositPlayer(username, amount)");
    TNE.debug("username: " + username);
    TNE.debug("Amount: " + amount);
    String world = TNE.instance().defaultWorld;
    final Player player = Bukkit.getPlayer(username);
    if(player != null) {
      world = player.getWorld().getName();
    }
    return depositPlayer(username, world, amount);
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
    String world = TNE.instance().defaultWorld;
    if(offlinePlayer.isOnline()) {
      offlinePlayer.getPlayer().getWorld().getName();
    }
    TNE.debug("Economy_TheNewEconomy.depositPlayer(offlinePlayer, amount)");
    TNE.debug("username: " + offlinePlayer.getName());
    TNE.debug("id: " + offlinePlayer.getUniqueId().toString());
    TNE.debug("world: " + world);
    TNE.debug("Amount: " + amount);

    if(offlinePlayer.getName().contains("faction-")) {
      return depositPlayer(offlinePlayer.getName(), world, amount);
    }

    return depositPlayer(offlinePlayer.getUniqueId(), world, amount);
  }

  @Override
  public EconomyResponse depositPlayer(String username, String world, double amount) {
    TNE.debug("Economy_TheNewEconomy.depositPlayer(username, world, amount)");
    TNE.debug("username: " + username);
    TNE.debug("world: " + world);
    TNE.debug("Amount: " + amount);
    if(TNE.maintenance) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Economy is in maintenance mode.");
    if(!hasAccount(username)) {
      return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That account does not exist!");
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative amounts.");
    }
    if(api.addHoldings(username, new BigDecimal(amount + ""), world)) {
      return new EconomyResponse(amount, getBalance(username, world), EconomyResponse.ResponseType.SUCCESS, "");
    }
    return new EconomyResponse(amount, getBalance(username, world), EconomyResponse.ResponseType.FAILURE, "Unable to complete transaction!");
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
    TNE.debug("Economy_TheNewEconomy.depositPlayer(offlinePlayer, world, amount)");
    TNE.debug("username: " + offlinePlayer.getName());
    TNE.debug("id: " + offlinePlayer.getUniqueId().toString());
    TNE.debug("world: " + world);
    TNE.debug("Amount: " + amount);

    if(offlinePlayer.getName().contains("faction-")) {
      return depositPlayer(offlinePlayer.getName(), world, amount);
    }

    return depositPlayer(offlinePlayer.getUniqueId(), world, amount);
  }

  public EconomyResponse depositPlayer(final UUID id, String world, double amount) {
    if(TNE.maintenance) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Economy is in maintenance mode.");
    if(!api.hasAccount(id)) {
      return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That account does not exist!");
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative amounts.");
    }
    if(api.addHoldings(id.toString(), new BigDecimal(amount + ""), world)) {
      return new EconomyResponse(amount, api.getHoldings(id.toString(), world).doubleValue(), EconomyResponse.ResponseType.SUCCESS, "");
    }
    return new EconomyResponse(amount, api.getHoldings(id.toString(), world).doubleValue(), EconomyResponse.ResponseType.FAILURE, "Unable to complete transaction!");
  }

  @Override
  public EconomyResponse createBank(String name, String world) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse createBank(String name, OfflinePlayer offlinePlayer) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse deleteBank(String name) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse bankBalance(String name) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse bankHas(String name, double amount) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse bankWithdraw(String name, double amount) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse bankDeposit(String name, double amount) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse isBankOwner(String name, String username) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse isBankOwner(String name, OfflinePlayer offlinePlayer) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse isBankMember(String name, String username) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public EconomyResponse isBankMember(String name, OfflinePlayer offlinePlayer) {
    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported yet!");
  }

  @Override
  public List<String> getBanks() {
    return new ArrayList<>();
  }

  @Override
  public boolean createPlayerAccount(String username) {
    return api.createAccount(username);
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
    return api.createAccount(offlinePlayer.getUniqueId(), offlinePlayer.getName());
  }

  @Override
  public boolean createPlayerAccount(String username, String world) {
    return api.createAccount(username);
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String world) {
    return api.createAccount(offlinePlayer.getUniqueId(), offlinePlayer.getName());
  }
}