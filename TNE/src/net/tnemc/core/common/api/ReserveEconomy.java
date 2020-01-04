package net.tnemc.core.common.api;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.economy.Account;
import net.tnemc.core.economy.ExtendedEconomyAPI;
import net.tnemc.core.economy.currency.Currency;
import net.tnemc.core.economy.currency.Tier;
import net.tnemc.core.economy.response.AccountResponse;
import net.tnemc.core.economy.response.EconomyResponse;
import net.tnemc.core.economy.response.GeneralResponse;
import net.tnemc.core.economy.tax.TaxEntry;
import net.tnemc.core.economy.tax.TaxType;
import net.tnemc.core.economy.transaction.Transaction;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.economy.transaction.type.TransactionType;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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
    return "0.1.4.6";
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
    return CurrencyFormatter.format(TNECurrency.fromReserve(currency), TNE.instance().defaultWorld, amount, "");
  }

  @Override
  public String format(BigDecimal amount, Currency currency, String world) {
    return CurrencyFormatter.format(TNECurrency.fromReserve(currency), world, amount, "");
  }

  @Override
  public boolean supportTransactions() {
    return true;
  }

  @Override
  public TransactionResult performTransaction(Transaction transaction) {
    return transaction.perform();
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
  public Optional<TransactionResult> findTransactionResult(String name) {
    return TNE.instance().api().findTransactionResult(name);
  }

  @Override
  public boolean removeTaxException(String identifier, String transactionType) {
    return TNE.instance().api().removeTaxException(identifier, transactionType);
  }

  @Override
  public boolean registerTaxException(String identifier, String transactionType, TaxEntry taxEntry) {
    return TNE.instance().api().registerTaxException(identifier, transactionType, taxEntry);
  }

  @Override
  public boolean registerTaxType(TaxType taxType) {
    return TNE.instance().api().registerTaxType(taxType);
  }

  @Override
  public Optional<TaxType> findTaxType(String name) {
    return TNE.instance().api().findTaxType(name);
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