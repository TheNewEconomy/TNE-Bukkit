package net.tnemc.core.common.api;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.tnemc.core.TNE;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    return api.hasAccount(IDFinder.getID(offlinePlayer).toString());
  }

  @Override
  public boolean hasAccount(String username, String world) {
    return api.hasAccount(username);
  }

  @Override
  public boolean hasAccount(OfflinePlayer offlinePlayer, String world) {
    return api.hasAccount(IDFinder.getID(offlinePlayer).toString());
  }

  @Override
  public double getBalance(String username) {
    return getBalance(username, TNE.instance().defaultWorld);
  }

  @Override
  public double getBalance(OfflinePlayer offlinePlayer) {
    return getBalance(IDFinder.getUsername(IDFinder.getID(offlinePlayer).toString()), TNE.instance().defaultWorld);
  }

  @Override
  public double getBalance(String username, String world) {
    return api.getHoldings(username, world).doubleValue();
  }

  @Override
  public double getBalance(OfflinePlayer offlinePlayer, String world) {
    return getBalance(IDFinder.getUsername(IDFinder.getID(offlinePlayer).toString()), world);
  }

  @Override
  public boolean has(String username, double amount) {
    TNE.debug("Economy_TheNewEconomy.has(username, amount)");
    TNE.debug("Amount: " + amount);
    return has(username, TNE.instance().defaultWorld, amount);
  }

  @Override
  public boolean has(OfflinePlayer offlinePlayer, double amount) {
    return has(IDFinder.getUsername(IDFinder.getID(offlinePlayer).toString()), TNE.instance().defaultWorld, amount);
  }

  @Override
  public boolean has(String username, String world, double amount) {
    TNE.debug("Economy_TheNewEconomy.has(username, world, amount)");
    TNE.debug("Amount: " + amount);
    return api.hasHoldings(username, new BigDecimal(amount + ""), world);
  }

  @Override
  public boolean has(OfflinePlayer offlinePlayer, String world, double amount) {
    return has(IDFinder.getUsername(IDFinder.getID(offlinePlayer).toString()), world, amount);
  }

  @Override
  public EconomyResponse withdrawPlayer(String username, double amount) {
    return withdrawPlayer(username, TNE.instance().defaultWorld, amount);
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
    return withdrawPlayer(IDFinder.getUsername(IDFinder.getID(offlinePlayer).toString()), TNE.instance().defaultWorld, amount);
  }

  @Override
  public EconomyResponse withdrawPlayer(String username, String world, double amount) {
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
    return withdrawPlayer(IDFinder.getUsername(IDFinder.getID(offlinePlayer).toString()), world, amount);
  }

  @Override
  public EconomyResponse depositPlayer(String username, double amount) {
    return depositPlayer(username, TNE.instance().defaultWorld, amount);
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
    return depositPlayer(IDFinder.getUsername(IDFinder.getID(offlinePlayer).toString()), TNE.instance().defaultWorld, amount);
  }

  @Override
  public EconomyResponse depositPlayer(String username, String world, double amount) {
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
    return depositPlayer(IDFinder.getUsername(IDFinder.getID(offlinePlayer).toString()), world, amount);
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
    return api.createAccount(IDFinder.getID(offlinePlayer).toString());
  }

  @Override
  public boolean createPlayerAccount(String username, String world) {
    return api.createAccount(username);
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String world) {
    return api.createAccount(IDFinder.getID(offlinePlayer).toString());
  }
}