package net.tnemc.core.common.api;

import net.tnemc.core.TNE;
import net.tnemc.core.economy.EconomyAPI;
import net.tnemc.core.economy.response.AccountResponse;
import net.tnemc.core.economy.response.EconomyResponse;
import net.tnemc.core.economy.response.GeneralResponse;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 8/17/2017.
 */
public class ReserveEconomy implements EconomyAPI {

  private TNE plugin;

  public ReserveEconomy(TNE plugin) {
    this.plugin = plugin;
  }

  @Override
  public String name() {
    return "TheNewEconomy";
  }

  @Override
  public String version() {
    return "0.1.5.4";
  }

  @Override
  public boolean enabled() {
    return true;
  }

  @Override
  public String currencyDefaultPlural() {
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name();
  }

  @Override
  public String currencyDefaultSingular() {
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name();
  }

  @Override
  public String currencyDefaultPlural(String world) {
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name();
  }

  @Override
  public String currencyDefaultSingular(String world) {
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name();
  }

  @Override
  public boolean hasCurrency(String name) {
    return TNE.instance().api().hasCurrency(name);
  }

  @Override
  public boolean hasCurrency(String name, String world) {
    return TNE.instance().api().hasCurrency(name, world);
  }

  @Override
  public EconomyResponse hasAccountDetail(String identifier) {
    if(TNE.instance().api().hasAccount(identifier)) return GeneralResponse.SUCCESS;
    return AccountResponse.DOESNT_EXIST;
  }

  @Override
  public EconomyResponse hasAccountDetail(UUID identifier) {
    if(TNE.instance().api().hasAccount(identifier)) return GeneralResponse.SUCCESS;
    return AccountResponse.DOESNT_EXIST;
  }

  @Override
  public boolean createAccount(String identifier) {
    return TNE.instance().api().createAccount(identifier);
  }

  @Override
  public boolean createAccount(UUID identifier) {
    return TNE.instance().api().createAccount(identifier);
  }

  @Override
  public EconomyResponse createAccountDetail(String identifier) {
    if(hasAccount(identifier)) return AccountResponse.ALREADY_EXISTS;
    if(TNE.instance().api().createAccount(identifier)) return AccountResponse.CREATED;
    return AccountResponse.CREATION_FAILED;
  }

  @Override
  public EconomyResponse createAccountDetail(UUID identifier) {
    if(hasAccount(identifier)) return AccountResponse.ALREADY_EXISTS;
    if(TNE.instance().api().createAccount(identifier)) return AccountResponse.CREATED;
    return AccountResponse.CREATION_FAILED;
  }

  @Override
  public EconomyResponse deleteAccountDetail(String identifier) {
    if(TNE.manager().deleteAccount(IDFinder.getID(identifier))) return GeneralResponse.SUCCESS;
    return GeneralResponse.FAILED;
  }

  @Override
  public EconomyResponse deleteAccountDetail(UUID identifier) {
    if(TNE.manager().deleteAccount(identifier)) return GeneralResponse.SUCCESS;
    return GeneralResponse.FAILED;
  }

  @Override
  public boolean isAccessor(String s, String s1) {
    return false;
  }

  @Override
  public boolean isAccessor(String s, UUID uuid) {
    return false;
  }

  @Override
  public boolean isAccessor(UUID uuid, String s) {
    return false;
  }

  @Override
  public boolean isAccessor(UUID uuid, UUID uuid1) {
    return false;
  }

  @Override
  public EconomyResponse canWithdrawDetail(String s, String s1) {
    return null;
  }

  @Override
  public EconomyResponse canWithdrawDetail(String s, UUID uuid) {
    return null;
  }

  @Override
  public EconomyResponse canWithdrawDetail(UUID uuid, String s) {
    return null;
  }

  @Override
  public EconomyResponse canWithdrawDetail(UUID uuid, UUID uuid1) {
    return null;
  }

  @Override
  public EconomyResponse canDepositDetail(String s, String s1) {
    return null;
  }

  @Override
  public EconomyResponse canDepositDetail(String s, UUID uuid) {
    return null;
  }

  @Override
  public EconomyResponse canDepositDetail(UUID uuid, String s) {
    return null;
  }

  @Override
  public EconomyResponse canDepositDetail(UUID uuid, UUID uuid1) {
    return null;
  }

  @Override
  public BigDecimal getHoldings(String s) {
    return null;
  }

  @Override
  public BigDecimal getHoldings(UUID uuid) {
    return null;
  }

  @Override
  public BigDecimal getHoldings(String s, String s1) {
    return null;
  }

  @Override
  public BigDecimal getHoldings(UUID uuid, String s) {
    return null;
  }

  @Override
  public BigDecimal getHoldings(String s, String s1, String s2) {
    return null;
  }

  @Override
  public BigDecimal getHoldings(UUID uuid, String s, String s1) {
    return null;
  }

  @Override
  public boolean hasHoldings(String s, BigDecimal bigDecimal) {
    return false;
  }

  @Override
  public boolean hasHoldings(UUID uuid, BigDecimal bigDecimal) {
    return false;
  }

  @Override
  public boolean hasHoldings(String s, BigDecimal bigDecimal, String s1) {
    return false;
  }

  @Override
  public boolean hasHoldings(UUID uuid, BigDecimal bigDecimal, String s) {
    return false;
  }

  @Override
  public boolean hasHoldings(String s, BigDecimal bigDecimal, String s1, String s2) {
    return false;
  }

  @Override
  public boolean hasHoldings(UUID uuid, BigDecimal bigDecimal, String s, String s1) {
    return false;
  }

  @Override
  public EconomyResponse setHoldingsDetail(String s, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse setHoldingsDetail(UUID uuid, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse setHoldingsDetail(String s, BigDecimal bigDecimal, String s1) {
    return null;
  }

  @Override
  public EconomyResponse setHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s) {
    return null;
  }

  @Override
  public EconomyResponse setHoldingsDetail(String s, BigDecimal bigDecimal, String s1, String s2) {
    return null;
  }

  @Override
  public EconomyResponse setHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s, String s1) {
    return null;
  }

  @Override
  public EconomyResponse addHoldingsDetail(String s, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse addHoldingsDetail(UUID uuid, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse addHoldingsDetail(String s, BigDecimal bigDecimal, String s1) {
    return null;
  }

  @Override
  public EconomyResponse addHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s) {
    return null;
  }

  @Override
  public EconomyResponse addHoldingsDetail(String s, BigDecimal bigDecimal, String s1, String s2) {
    return null;
  }

  @Override
  public EconomyResponse addHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s, String s1) {
    return null;
  }

  @Override
  public EconomyResponse canAddHoldingsDetail(String s, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse canAddHoldingsDetail(UUID uuid, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse canAddHoldingsDetail(String s, BigDecimal bigDecimal, String s1) {
    return null;
  }

  @Override
  public EconomyResponse canAddHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s) {
    return null;
  }

  @Override
  public EconomyResponse canAddHoldingsDetail(String s, BigDecimal bigDecimal, String s1, String s2) {
    return null;
  }

  @Override
  public EconomyResponse canAddHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s, String s1) {
    return null;
  }

  @Override
  public EconomyResponse removeHoldingsDetail(String s, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse removeHoldingsDetail(UUID uuid, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse removeHoldingsDetail(String s, BigDecimal bigDecimal, String s1) {
    return null;
  }

  @Override
  public EconomyResponse removeHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s) {
    return null;
  }

  @Override
  public EconomyResponse removeHoldingsDetail(String s, BigDecimal bigDecimal, String s1, String s2) {
    return null;
  }

  @Override
  public EconomyResponse removeHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s, String s1) {
    return null;
  }

  @Override
  public EconomyResponse canRemoveHoldingsDetail(String s, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse canRemoveHoldingsDetail(UUID uuid, BigDecimal bigDecimal) {
    return null;
  }

  @Override
  public EconomyResponse canRemoveHoldingsDetail(String s, BigDecimal bigDecimal, String s1) {
    return null;
  }

  @Override
  public EconomyResponse canRemoveHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s) {
    return null;
  }

  @Override
  public EconomyResponse canRemoveHoldingsDetail(String s, BigDecimal bigDecimal, String s1, String s2) {
    return null;
  }

  @Override
  public EconomyResponse canRemoveHoldingsDetail(UUID uuid, BigDecimal bigDecimal, String s, String s1) {
    return null;
  }

  @Override
  public String format(BigDecimal amount) {
    return TNE.instance().api().format(amount, TNE.instance().defaultWorld);
  }

  @Override
  public String format(BigDecimal amount, String world) {
    return TNE.instance().api().format(amount, world);
  }

  @Override
  public String format(BigDecimal bigDecimal, String s, String s1) {
    return null;
  }

  @Override
  public boolean purgeAccounts() {
    return false;
  }

  @Override
  public boolean purgeAccountsUnder(BigDecimal bigDecimal) {
    return false;
  }

  @Override
  public boolean supportTransactions() {
    return true;
  }
}