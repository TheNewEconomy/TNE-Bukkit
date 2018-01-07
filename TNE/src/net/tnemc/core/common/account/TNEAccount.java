package net.tnemc.core.common.account;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.history.AccountHistory;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.Account;
import net.tnemc.core.economy.currency.Currency;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
  private boolean player;
  private long joined;
  private long lastOnline;
  
  public TNEAccount(UUID id, String displayName) {
    this.id = id;
    this.displayName = displayName;
    this.status = AccountStatus.NORMAL;
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
    TNE.debug("=====START Account.setHoldings(4) =====");
    TNE.debug("Holdings: " + newHoldings.toPlainString());
    WorldHoldings worldHoldings = holdings.containsKey(world)? holdings.get(world) : new WorldHoldings(world);
    worldHoldings.setHoldings(currency, newHoldings);

    TNE.debug("Currency: " + currency);
    if(!skipInventory && MISCUtils.isOnline(id)) {
      TNE.debug("Skip: " + skipInventory);
      TNE.debug("Online: " + MISCUtils.isOnline(id));
      TNECurrency cur = TNE.manager().currencyManager().get(world, currency);
      TNE.debug("Currency Item: " + cur.isItem());
      if(cur.isItem()) {
        ItemCalculations.setItems(this, cur, newHoldings);
      }
    }

    holdings.put(world, worldHoldings);
    TNE.debug("=====END Account.setHoldings =====");
  }

  public boolean hasHoldings(String world, String currency) {
    if(holdings.containsKey(world)) {
      return holdings.get(world).hasHoldings(currency);
    }
    return false;
  }

  public BigDecimal getHoldings(String world, String currency) {
    TNE.debug("=====START Account.getHoldings =====");
    TNE.debug("Account: " + identifier());
    TNE.debug("World: " + world);
    TNE.debug("Currency: " + currency);
    WorldHoldings worldHoldings = holdings.containsKey(world)? holdings.get(world) : new WorldHoldings(world);
    TNE.debug("Holdings: " + worldHoldings.getHoldings(currency));
    TNE.debug("=====END Account.getHoldings =====");
    return worldHoldings.getHoldings(currency);
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
      if(!hasHoldings(world, currency.name())) {
        setHoldings(world, currency.name(), currency.defaultBalance());
      }
    });
  }

  public Player getPlayer() {
    return IDFinder.getPlayer(identifier().toString());
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
    return getHoldings(world, currency.name());
  }

  @Override
  public BigDecimal getHoldings(String world) {
    Currency currency = TNE.manager().currencyManager().get(world);
    return getHoldings(world, currency.name());
  }

  @Override
  public BigDecimal getHoldings(String world, Currency currency) {
    return getHoldings(world, currency.name());
  }

  @Override
  public BigDecimal getHoldings(Currency currency) {
    return getHoldings(TNE.instance().defaultWorld, currency.name());
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
  public boolean addHoldings(BigDecimal amount) {
    String world = TNE.instance().defaultWorld;
    Currency currency = TNE.manager().currencyManager().get(world);
    setHoldings(world, currency.name(), getHoldings(world, currency).add(amount));
    return true;
  }

  @Override
  public boolean addHoldings(BigDecimal amount, String world) {
    Currency currency = TNE.manager().currencyManager().get(world);
    setHoldings(world, currency.name(), getHoldings(world, currency).add(amount));
    return true;
  }

  @Override
  public boolean addHoldings(BigDecimal amount, Currency currency) {
    String world = TNE.instance().defaultWorld;
    setHoldings(world, currency.name(), getHoldings(world, currency).add(amount));
    return true;
  }

  @Override
  public boolean addHoldings(BigDecimal amount, Currency currency, String world) {
    setHoldings(world, currency.name(), getHoldings(world, currency).add(amount));
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
    if(hasHoldings(amount)) {
      String world = TNE.instance().defaultWorld;
      Currency currency = TNE.manager().currencyManager().get(world);
      setHoldings(world, currency.name(), getHoldings(world, currency).subtract(amount));
      return true;
    }
    return false;
  }

  @Override
  public boolean removeHoldings(BigDecimal amount, String world) {
    if(hasHoldings(amount)) {
      Currency currency = TNE.manager().currencyManager().get(world);
      setHoldings(world, currency.name(), getHoldings(world, currency).subtract(amount));
      return true;
    }
    return false;
  }

  @Override
  public boolean removeHoldings(BigDecimal amount, Currency currency) {
    if(hasHoldings(amount)) {
      String world = TNE.instance().defaultWorld;
      setHoldings(world, currency.name(), getHoldings(world, currency).subtract(amount));
      return true;
    }
    return false;
  }

  @Override
  public boolean removeHoldings(BigDecimal amount, Currency currency, String world) {
    if(hasHoldings(amount)) {
      setHoldings(world, currency.name(), getHoldings(world, currency).subtract(amount));
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