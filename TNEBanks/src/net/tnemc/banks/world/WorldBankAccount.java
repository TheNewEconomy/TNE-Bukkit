package net.tnemc.banks.world;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/2/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class WorldBankAccount {

  Map<String, BigDecimal> currencyHoldings = new HashMap<>();

  private String world;
  private UUID account;
  private UUID accountHolder;

  private String accountDisplay;
  private BankAccountStatus status = BankAccountStatus.OPEN;


  public WorldBankAccount(String world, UUID account, UUID accountHolder, String accountDisplay) {
    this.world = world;
    this.account = account;
    this.accountHolder = accountHolder;
    this.accountDisplay = accountDisplay;
  }

  public Map<String, BigDecimal> getCurrencyHoldings() {
    return currencyHoldings;
  }

  public void setCurrencyHoldings(Map<String, BigDecimal> currencyHoldings) {
    this.currencyHoldings = currencyHoldings;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public UUID getAccount() {
    return account;
  }

  public void setAccount(UUID account) {
    this.account = account;
  }

  public UUID getAccountHolder() {
    return accountHolder;
  }

  public void setAccountHolder(UUID accountHolder) {
    this.accountHolder = accountHolder;
  }

  public String getAccountDisplay() {
    return accountDisplay;
  }

  public void setAccountDisplay(String accountDisplay) {
    this.accountDisplay = accountDisplay;
  }

  public BankAccountStatus getStatus() {
    return status;
  }

  public void setStatus(BankAccountStatus status) {
    this.status = status;
  }
}