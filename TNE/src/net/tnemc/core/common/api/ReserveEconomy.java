package net.tnemc.core.common.api;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import net.tnemc.core.economy.Account;
import net.tnemc.core.economy.ExtendedEconomyAPI;
import net.tnemc.core.economy.currency.Currency;
import net.tnemc.core.economy.currency.Tier;
import net.tnemc.core.economy.transaction.Transaction;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.economy.transaction.type.TransactionType;

import java.math.BigDecimal;
import java.util.*;

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
 * Created by Daniel on 8/17/2017.
 */
public class ReserveEconomy implements ExtendedEconomyAPI {

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
    return "0.1.0.0";
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
  public Currency getDefault() {
    return TNE.instance().api().getDefault();
  }

  @Override
  public Currency getDefault(String world) {
    return TNE.instance().api().getDefault(world);
  }

  @Override
  public Set<Currency> getCurrencies() {
    return new HashSet<>(TNE.instance().getWorldManager(TNE.instance().defaultWorld).getCurrencies());
  }

  @Override
  public Set<Currency> getCurrencies(String world) {
    return new HashSet<>(TNE.instance().getWorldManager(world).getCurrencies());
  }

  @Override
  public Currency getCurrency(String currency) {
    for(TNECurrency cur : TNE.manager().currencyManager().getCurrencies()) {
      if(cur.name().equalsIgnoreCase(currency)) return cur;
    }
    return null;
  }

  @Override
  public Currency getCurrency(String currency, String world) {
    return TNE.manager().currencyManager().get(world, currency);
  }

  @Override
  public boolean hasTier(String name, Currency currency) {
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld, currency.name()).hasTier(name);
  }

  @Override
  public boolean hasTier(String name, Currency currency, String world) {
    return TNE.manager().currencyManager().get(world, currency.name()).hasTier(name);
  }

  @Override
  public Set<Tier> getTiers(Currency currency) {
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld, currency.name()).getTiers();
  }

  @Override
  public boolean hasAccount(String identifier) {
    return TNE.instance().api().hasAccount(identifier);
  }

  @Override
  public boolean hasAccount(UUID identifier) {
    return TNE.instance().api().hasAccount(identifier);
  }

  @Override
  public Account getAccount(String identifier) {
    return TNE.instance().api().getAccount(identifier);
  }

  @Override
  public Account getAccount(UUID identifier) {
    return TNE.instance().api().getAccount(identifier);
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
  public boolean deleteAccount(String identifier) {
    return TNE.manager().deleteAccount(IDFinder.getID(identifier));
  }

  @Override
  public boolean deleteAccount(UUID identifier) {
    return TNE.manager().deleteAccount(identifier);
  }

  @Override
  public Account createIfNotExists(String identifier) {
    if(!hasAccount(identifier)) createAccount(identifier);
    return getAccount(identifier);
  }

  @Override
  public Account createIfNotExists(UUID uuid) {
    if(!hasAccount(uuid)) createAccount(uuid);
    return getAccount(uuid);
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
  public boolean purgeAccounts() {
    return false;
  }

  @Override
  public boolean purgeAccountsUnder(BigDecimal bigDecimal) {
    return false;
  }

  @Override
  public String format(BigDecimal amount, Currency currency) {
    return CurrencyFormatter.format(currency, TNE.instance().defaultWorld, amount);
  }

  @Override
  public String format(BigDecimal amount, Currency currency, String world) {
    return CurrencyFormatter.format(currency, world, amount);
  }

  @Override
  public boolean supportTransactions() {
    return true;
  }

  @Override
  public TransactionResult performTransaction(Transaction transaction) {
    return null;
  }

  @Override
  public boolean voidTransaction(UUID uuid) {
    return TNE.instance().api().voidTransaction(uuid);
  }

  @Override
  public Set<TransactionType> getTransactionTypes() {
    return TNE.instance().api().getTransactionTypes();
  }

  @Override
  public boolean registerTransactionType(TransactionType transactionType) {
    return TNE.instance().api().registerTransactionType(transactionType);
  }

  @Override
  public boolean registerTransactionResult(TransactionResult transactionResult) {
    return TNE.instance().api().registerTransactionResult(transactionResult);
  }

  @Override
  public boolean registerCurrency(Currency currency) {
    return TNE.instance().api().registerCurrency(TNECurrency.fromReserve(currency));
  }

  @Override
  public boolean registerCurrency(Currency currency, String world) {
    return TNE.instance().api().registerCurrency(TNECurrency.fromReserve(currency), world);
  }

  @Override
  public boolean registerTier(Tier tier, Currency currency) {
    return TNE.instance().api().registerTier(TNETier.fromReserve(tier), TNECurrency.fromReserve(currency));
  }

  @Override
  public boolean registerTier(Tier tier, Currency currency, String world) {
    return TNE.instance().api().registerTier(TNETier.fromReserve(tier), TNECurrency.fromReserve(currency), world);
  }

  @Override
  public Map<UUID, Transaction> getTransactions(String identifier) {
    return TNE.instance().api().getTransactions(identifier);
  }

  @Override
  public Map<UUID, Transaction> getTransactions() {
    return TNE.instance().api().getTransactions();
  }

  @Override
  public Optional<Transaction> getTransaction(UUID uuid) {
    return TNE.instance().api().getTransaction(uuid);
  }
}