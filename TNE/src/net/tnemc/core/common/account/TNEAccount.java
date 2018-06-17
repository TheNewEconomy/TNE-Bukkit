package net.tnemc.core.common.account;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.handlers.HoldingsHandler;
import net.tnemc.core.common.account.history.AccountHistory;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.module.injectors.InjectMethod;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.Account;
import net.tnemc.core.economy.currency.Currency;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 12/7/2017.
 */

public class TNEAccount implements Account {
  private Map<UUID, AccountAccessor> accessors = new HashMap<>();
  private Map<String, WorldHoldings> holdings = new HashMap<>();
  private AccountHistory history;

  private int accountNumber = 0;
  private UUID id;
  private String displayName;
  private AccountStatus status;
  private String language;
  private boolean player;
  private long joined;
  private long lastOnline;
  
  public TNEAccount(UUID id, String displayName) {
    this.id = id;
    this.displayName = displayName;
    this.status = AccountStatus.NORMAL;
    this.language = "Default";
    this.player = true;
    this.joined = new Date().getTime();
    this.lastOnline = new Date().getTime();
    history = new AccountHistory();
  }

  public void log(TNETransaction transaction) {
    history.log(transaction);
  }

  public BigDecimal addAll(String world) {
    BigDecimal total = BigDecimal.ZERO;

    if(!world.equalsIgnoreCase("all")) {
      if(holdings.containsKey(world)) {
        WorldHoldings worldHoldings = holdings.get(world);
        for(Map.Entry<String, BigDecimal> entry : worldHoldings.getHoldings().entrySet()) {
          total = total.add(TNE.manager().currencyManager().convert(TNE.manager().currencyManager().get(world, entry.getKey()), 1.0, entry.getValue()));
        }
      }
    } else {
      for(Map.Entry<String, WorldHoldings> entry : holdings.entrySet()) {
        WorldHoldings worldHoldings = entry.getValue();

        for(Map.Entry<String, BigDecimal> balEntry : worldHoldings.getHoldings().entrySet()) {
          total = total.add(TNE.manager().currencyManager().convert(TNE.manager().currencyManager().get(entry.getKey(), balEntry.getKey()), 1.0, balEntry.getValue()));
        }
      }
    }
    return total;
  }

  public void setHoldings(String world, String currency, BigDecimal newHoldings) {
    TNE.debug("=====START Account.setHoldings(3) =====");
    TNE.debug("Holdings: " + newHoldings.toPlainString());
    setHoldings(world, currency, newHoldings, false);
    TNE.debug("=====END Account.setHoldings =====");
  }

  public void setHoldings(String world, String currency, BigDecimal newHoldings, boolean skipInventory) {
    world = TNE.instance().getWorldManager(world).getBalanceWorld();
    TNE.debug("=====START Account.setHoldings(4) =====");
    TNE.debug("Holdings: " + newHoldings.toPlainString());

    InjectMethod injector = new InjectMethod("TNEAccount.setHoldings", new HashMap<>());
    injector.setParameter("currency", currency);
    injector.setParameter("holdings", newHoldings);
    TNE.loader().call(injector);
    newHoldings = (BigDecimal)injector.getParameter("holdings");

    TNECurrency cur = TNE.manager().currencyManager().get(world, currency);

    TNE.debug("Currency: " + cur.name());
    if(skipInventory || !cur.isItem() || !MISCUtils.isOnline(id, world)) {
      WorldHoldings worldHoldings = holdings.containsKey(world) ? holdings.get(world) : new WorldHoldings(world);
      worldHoldings.setHoldings(currency, newHoldings);
      holdings.put(world, worldHoldings);
      TNE.manager().addAccount(this);
    } else {
      TNE.debug("Skip: " + skipInventory);
      TNE.debug("Online: " + MISCUtils.isOnline(id, world));
      TNE.debug("Currency Item: " + cur.isItem());
      if (cur.isItem()) {
        ItemCalculations.setItems(cur, newHoldings, getPlayer().getInventory(), false);
      }
    }
    TNE.debug("=====END Account.setHoldings =====");
  }

  public void removeHoldings(BigDecimal amount, String world, String currency, boolean core) {
    BigDecimal leftOver = amount;
    for(Map.Entry<Integer, List<HoldingsHandler>> entry : TNE.manager().getHoldingsHandlers().descendingMap().entrySet()) {
      if(leftOver.compareTo(BigDecimal.ZERO) <= 0) break;
      for(HoldingsHandler handler : entry.getValue()) {
        if(leftOver.compareTo(BigDecimal.ZERO) <= 0) break;
        if(!core || handler.coreHandler()) {
          if(!handler.userContains().equalsIgnoreCase("") ||
              displayName().contains(handler.userContains())) {
            leftOver = handler.removeHoldings(identifier(), world, TNE.manager().currencyManager().get(world, currency), leftOver);
          }
        }
      }
    }
  }

  private boolean hasHoldings(String world, String currency) {
    TNECurrency cur = TNE.manager().currencyManager().get(world, currency);
    world = TNE.instance().getWorldManager(world).getBalanceWorld();
    if(!cur.isItem() || !MISCUtils.isOnline(id, world)) {
      if (holdings.containsKey(world)) {
        return holdings.get(world).hasHoldings(currency);
      }
    } else {
      return ItemCalculations.getCurrencyItems(cur, getPlayer().getInventory()).compareTo(BigDecimal.ZERO) > 0;
    }
    return false;
  }

  public BigDecimal getHoldings(String world, String currency, boolean core, boolean database) {
    BigDecimal holdings = BigDecimal.ZERO;
    for(Map.Entry<Integer, List<HoldingsHandler>> entry : TNE.manager().getHoldingsHandlers().descendingMap().entrySet()) {
      for(HoldingsHandler handler : entry.getValue()) {
        if(!core || handler.coreHandler()) {
          if(handler.userContains().equalsIgnoreCase("") ||
              displayName().contains(handler.userContains())) {
            holdings = holdings.add(handler.getHoldings(identifier(), world, TNE.manager().currencyManager().get(world, currency), database));
          }
        }
      }
    }
    return holdings;
  }

  public void saveItemCurrency(String world) {
    saveItemCurrency(world, true);
  }

  public void saveItemCurrency(String world, boolean save) {
    saveItemCurrency(world, save, getPlayer().getInventory());
  }

  public void saveItemCurrency(String world, boolean save, PlayerInventory inventory) {
    TNE.debug("saveItemCurrency for world : " + world + " Save: " + save);
    List<String> currencies = TNE.instance().getWorldManager(world).getItemCurrencies();
    WorldHoldings worldHoldings = holdings.containsKey(world)? holdings.get(world) : new WorldHoldings(world);

    currencies.forEach((currency)->{
      TNE.debug("Currency: " + currency);
      TNECurrency cur = TNE.manager().currencyManager().get(world, currency);
      worldHoldings.setHoldings(currency, ItemCalculations.getCurrencyItems(cur, inventory));
    });
    holdings.put(world, worldHoldings);
    if(save) TNE.manager().addAccount(this);
  }

  public Map<String, WorldHoldings> getWorldHoldings() {
    return holdings;
  }

  public WorldHoldings getWorldHoldings(String world) {
    return holdings.get(world);
  }

  public static TNEAccount getAccount(String identifier) {
    return TNE.manager().getAccount(IDFinder.getID(identifier));
  }

  public void initializeHoldings(String world) {
    TNE.manager().currencyManager().getWorldCurrencies(world).forEach((currency)->{
      if(currency.defaultBalance().compareTo(BigDecimal.ZERO) > 0 &&!hasHoldings(world, currency.name())) {
        addHoldings(currency.defaultBalance(), currency, world);
      }
    });
  }

  public Player getPlayer() {
    return IDFinder.getPlayer(displayName);
  }

  public AccountHistory getHistory() {
    return history;
  }

  public void setHistory(AccountHistory history) {
    this.history = history;
  }

  public int getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(int accountNumber) {
    this.accountNumber = accountNumber;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public AccountStatus getStatus() {
    return status;
  }

  public void setStatus(AccountStatus status) {
    this.status = status;
  }

  public void setPlayerAccount(boolean player) {
    this.player = player;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public long getJoined() {
    return joined;
  }

  public void setJoined(long joined) {
    this.joined = joined;
  }

  public long getLastOnline() {
    return lastOnline;
  }

  public void setLastOnline(long lastOnline) {
    this.lastOnline = lastOnline;
  }

  @Override
  public UUID identifier() {
    return id;
  }

  @Override
  public String displayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public boolean playerAccount() {
    return player;
  }

  @Override
  public boolean isAccessor(Account account) {
    return accessors.containsKey(IDFinder.getID(account.identifier().toString()));
  }

  @Override
  public boolean canWithdraw(Account account) {
    if(isAccessor(account)) {
      return accessors.get(IDFinder.getID(account.identifier().toString())).canWithdraw();
    }
    return false;
  }

  @Override
  public boolean canDeposit(Account account) {
    if(isAccessor(account)) {
      return accessors.get(IDFinder.getID(account.identifier().toString())).canDeposit();
    }
    return false;
  }

  @Override
  public boolean canRemoveAccessor(Account account) {
    if(isAccessor(account)) {
      return accessors.get(IDFinder.getID(account.identifier().toString())).canRemoveAccessor();
    }
    return false;
  }

  @Override
  public boolean canAddAccessor(Account account) {
    if(isAccessor(account)) {
      return accessors.get(IDFinder.getID(account.identifier().toString())).canAddAccessor();
    }
    return false;
  }

  @Override
  public BigDecimal getHoldings() {
    String world = TNE.instance().defaultWorld;
    Currency currency = TNE.manager().currencyManager().get(world);
    return getHoldings(world, currency.name(), false, false);
  }

  @Override
  public BigDecimal getHoldings(String world) {
    Currency currency = TNE.manager().currencyManager().get(world);
    return getHoldings(world, currency.name(), false, false);
  }

  @Override
  public BigDecimal getHoldings(String world, Currency currency) {
    TNE.debug("=====START Account.getHoldings w/ World & Currency param =====");
    return getHoldings(world, currency.name(), false, false);
  }

  @Override
  public BigDecimal getHoldings(Currency currency) {
    return getHoldings(TNE.instance().defaultWorld, currency.name(), false, false);
  }

  @Override
  public boolean hasHoldings(BigDecimal amount) {
    return getHoldings().compareTo(amount) >= 0;
  }

  @Override
  public boolean hasHoldings(BigDecimal amount, String world) {
    return getHoldings(world).compareTo(amount) >= 0;
  }

  @Override
  public boolean hasHoldings(BigDecimal amount, Currency currency) {
    return getHoldings(currency).compareTo(amount) >= 0;
  }

  @Override
  public boolean hasHoldings(BigDecimal amount, Currency currency, String world) {
    return getHoldings(world, currency).compareTo(amount) >= 0;
  }

  @Override
  public boolean setHoldings(BigDecimal amount) {
    String world = TNE.instance().defaultWorld;
    setHoldings(world, TNE.manager().currencyManager().get(world).name(), amount);
    return true;
  }

  @Override
  public boolean setHoldings(BigDecimal amount, String world) {
    setHoldings(world, TNE.manager().currencyManager().get(world).name(), amount);
    return true;
  }

  @Override
  public boolean setHoldings(BigDecimal amount, Currency currency) {
    setHoldings(TNE.instance().defaultWorld, currency.name(), amount);
    return true;
  }

  @Override
  public boolean setHoldings(BigDecimal amount, Currency currency, String world) {
    setHoldings(world, currency.name(), amount);
    return true;
  }

  @Override
  public boolean addHoldings(BigDecimal amount) {
    if(amount.equals(BigDecimal.ZERO)) return true;
    String world = TNE.instance().defaultWorld;
    Currency currency = TNE.manager().currencyManager().get(world);
    setHoldings(world, currency.name(), getHoldings(world, currency.name(), true, false).add(amount));
    return true;
  }

  @Override
  public boolean addHoldings(BigDecimal amount, String world) {
    if(amount.equals(BigDecimal.ZERO)) return true;
    Currency currency = TNE.manager().currencyManager().get(world);
    setHoldings(world, currency.name(), getHoldings(world, currency.name(), true, false).add(amount));
    return true;
  }

  @Override
  public boolean addHoldings(BigDecimal amount, Currency currency) {
    if(amount.equals(BigDecimal.ZERO)) return true;
    String world = TNE.instance().defaultWorld;
    setHoldings(world, currency.name(), getHoldings(world, currency.name(), true, false).add(amount));
    return true;
  }

  @Override
  public boolean addHoldings(BigDecimal amount, Currency currency, String world) {
    if(amount.equals(BigDecimal.ZERO)) return true;
    setHoldings(world, currency.name(), getHoldings(world, currency.name(), true, false).add(amount));
    return true;
  }

  @Override
  public boolean canAddHoldings(BigDecimal amount) {
    return true;
  }

  @Override
  public boolean canAddHoldings(BigDecimal amount, String world) {
    return true;
  }

  @Override
  public boolean canAddHoldings(BigDecimal amount, Currency currency) {
    return true;
  }

  @Override
  public boolean canAddHoldings(BigDecimal amount, Currency currency, String world) {
    return true;
  }

  @Override
  public boolean removeHoldings(BigDecimal amount) {
    if(amount.equals(BigDecimal.ZERO)) return true;
    if(hasHoldings(amount)) {
      String world = TNE.instance().defaultWorld;
      Currency currency = TNE.manager().currencyManager().get(world);
      removeHoldings(amount, world, currency.name(), false);
      return true;
    }
    return false;
  }

  @Override
  public boolean removeHoldings(BigDecimal amount, String world) {
    if(amount.equals(BigDecimal.ZERO)) return true;
    if(hasHoldings(amount)) {
      Currency currency = TNE.manager().currencyManager().get(world);
      removeHoldings(amount, world, currency.name(), false);
      return true;
    }
    return false;
  }

  @Override
  public boolean removeHoldings(BigDecimal amount, Currency currency) {
    if(amount.equals(BigDecimal.ZERO)) return true;
    if(hasHoldings(amount)) {
      String world = TNE.instance().defaultWorld;
      removeHoldings(amount, world, currency.name(), false);
      return true;
    }
    return false;
  }

  @Override
  public boolean removeHoldings(BigDecimal amount, Currency currency, String world) {
    if(amount.equals(BigDecimal.ZERO)) return true;
    if(hasHoldings(amount)) {
      removeHoldings(amount, world, currency.name(), false);
      return true;
    }
    return false;
  }

  @Override
  public boolean canRemoveHoldings(BigDecimal amount) {
    return hasHoldings(amount);
  }

  @Override
  public boolean canRemoveHoldings(BigDecimal amount, String world) {
    return hasHoldings(amount, world);
  }

  @Override
  public boolean canRemoveHoldings(BigDecimal amount, Currency currency) {
    return hasHoldings(amount, currency);
  }

  @Override
  public boolean canRemoveHoldings(BigDecimal amount, Currency currency, String world) {
    return hasHoldings(amount, currency, world);
  }/**
   * Used to handle an {@link TransactionCharge}. This is mostly a shorthand method.
   * @param charge The {@link TransactionCharge} to handle.
   * @return True if charge is able to be handled successfully, otherwise false.
   */
  @Override
  public boolean handleCharge(TransactionCharge charge) {
    if(charge.getType().equals(TransactionChargeType.LOSE)) {
      return removeHoldings(charge.getEntry().getAmount(), charge.getCurrency(), charge.getWorld());
    }
    return addHoldings(charge.getEntry().getAmount(), charge.getCurrency(), charge.getWorld());
  }

  /**
   * Used to determine if a call to handleCharge would be successful. This method does not affect an account's funds.
   * @param charge The {@link TransactionCharge} to handle.
   * @return True if a call to handleCharge would return true, otherwise false.
   */
  @Override
  public boolean canCharge(TransactionCharge charge) {
    if(charge.getType().equals(TransactionChargeType.LOSE)) {
      return canRemoveHoldings(charge.getEntry().getAmount(), charge.getCurrency(), charge.getWorld());
    }
    return canAddHoldings(charge.getEntry().getAmount(), charge.getCurrency(), charge.getWorld());
  }
}