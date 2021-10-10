package net.tnemc.core.common.api;

import net.tnemc.core.TNE;
import net.tnemc.core.common.CurrencyManager;
import net.tnemc.core.economy.EconomyAPI;
import net.tnemc.core.economy.response.AccountResponse;
import net.tnemc.core.economy.response.EconomyResponse;
import net.tnemc.core.economy.response.GeneralResponse;
import net.tnemc.core.economy.response.HoldingsResponse;
import org.bukkit.World;

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

  private final TNE plugin;

  private final BigDecimal minBalance = BigDecimal.ZERO;

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
    return TNE.manager().currencyManager().get(plugin.defaultWorld).name();
  }

  @Override
  public String currencyDefaultSingular() {
    return TNE.manager().currencyManager().get(plugin.defaultWorld).name();
  }

  @Override
  public String currencyDefaultPlural(String world) {
    return TNE.manager().currencyManager().get(plugin.defaultWorld).name();
  }

  @Override
  public String currencyDefaultSingular(String world) {
    return TNE.manager().currencyManager().get(plugin.defaultWorld).name();
  }

  @Override
  public boolean hasCurrency(String name) {
    return plugin.api().hasCurrency(name);
  }

  @Override
  public boolean hasCurrency(String name, String world) {
    return plugin.api().hasCurrency(name, world);
  }

  @Override
  public EconomyResponse hasAccountDetail(String identifier) {
    if(plugin.api().hasAccount(identifier)) return GeneralResponse.SUCCESS;
    return AccountResponse.DOESNT_EXIST;
  }

  @Override
  public EconomyResponse hasAccountDetail(UUID identifier) {
    if(plugin.api().hasAccount(identifier)) return GeneralResponse.SUCCESS;
    return AccountResponse.DOESNT_EXIST;
  }

  @Override
  public boolean createAccount(String identifier) {
    return plugin.api().createAccount(identifier);
  }

  @Override
  public boolean createAccount(UUID identifier) {
    return plugin.api().createAccount(identifier);
  }

  @Override
  public EconomyResponse createAccountDetail(String identifier) {
    if(hasAccount(identifier)) return AccountResponse.ALREADY_EXISTS;
    if(plugin.api().createAccount(identifier)) return AccountResponse.CREATED;
    return AccountResponse.CREATION_FAILED;
  }

  @Override
  public EconomyResponse createAccountDetail(UUID identifier) {
    if(hasAccount(identifier)) return AccountResponse.ALREADY_EXISTS;
    if(plugin.api().createAccount(identifier)) return AccountResponse.CREATED;
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

  /**
   * Determines whether or not a player is able to access this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return Whether or not the player is able to access this account.
   */
  @Override
  public boolean isAccessor(String identifier, String accessor) {
    return identifier.equals(accessor);
  }

  /**
   * Determines whether or not a player is able to access this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return Whether or not the player is able to access this account.
   */
  @Override
  public boolean isAccessor(String identifier, UUID accessor) {
    return isAccessor(identifier, accessor.toString());
  }

  /**
   * Determines whether or not a player is able to access this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return Whether or not the player is able to access this account.
   */
  @Override
  public boolean isAccessor(UUID identifier, String accessor) {
    return isAccessor(identifier.toString(), accessor);
  }

  /**
   * Determines whether or not a player is able to access this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return Whether or not the player is able to access this account.
   */
  @Override
  public boolean isAccessor(UUID identifier, UUID accessor) {
    return isAccessor(identifier.toString(), accessor.toString());
  }

  /**
   * Determines whether or not a player is able to withdraw holdings from this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canWithdrawDetail(String identifier, String accessor) {
    if (identifier.equals(accessor)) {
      return GeneralResponse.SUCCESS;
    }
    return GeneralResponse.FAILED;
  }

  /**
   * Determines whether or not a player is able to withdraw holdings from this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canWithdrawDetail(String identifier, UUID accessor) {
    return canWithdrawDetail(identifier, accessor.toString());
  }

  /**
   * Determines whether or not a player is able to withdraw holdings from this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canWithdrawDetail(UUID identifier, String accessor) {
    return canWithdrawDetail(identifier.toString(), accessor);
  }

  /**
   * Determines whether or not a player is able to withdraw holdings from this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canWithdrawDetail(UUID identifier, UUID accessor) {
    return canWithdrawDetail(identifier.toString(), accessor.toString());
  }

  /**
   * Determines whether or not a player is able to deposit holdings into this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canDepositDetail(String identifier, String accessor) {
    if (identifier.equals(accessor)) {
      return GeneralResponse.SUCCESS;
    }
    return GeneralResponse.FAILED;
  }

  /**
   * Determines whether or not a player is able to deposit holdings into this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canDepositDetail(String identifier, UUID accessor) {
    return canDepositDetail(identifier, accessor.toString());
  }

  /**
   * Determines whether or not a player is able to deposit holdings into this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canDepositDetail(UUID identifier, String accessor) {
    return canDepositDetail(identifier.toString(), accessor);
  }

  /**
   * Determines whether or not a player is able to deposit holdings into this account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param accessor The identifier of the user attempting to access this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canDepositDetail(UUID identifier, UUID accessor) {
    return canDepositDetail(identifier.toString(), accessor.toString());
  }

  /**
   * Used to get the balance of an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @return The balance of the account.
   */
  @Override
  public BigDecimal getHoldings(String identifier) {
    if (hasAccount(identifier)) {

      return TNE.manager().getAccount(IDFinder.getID(identifier)).getHoldings();
    }
    return BigDecimal.ZERO;
  }

  /**
   * Used to get the balance of an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @return The balance of the account.
   */
  @Override
  public BigDecimal getHoldings(UUID identifier) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(identifier).getHoldings();
    }
    return BigDecimal.ZERO;
  }

  /**
   * Used to get the balance of an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param world The name of the {@link World} associated with the balance.
   * @return The balance of the account.
   */
  @Override
  public BigDecimal getHoldings(String identifier, String world) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(IDFinder.getID(identifier)).getHoldings(world);
    }
    return BigDecimal.ZERO;
  }

  /**
   * Used to get the balance of an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param world The name of the {@link World} associated with the balance.
   * @return The balance of the account.
   */
  @Override
  public BigDecimal getHoldings(UUID identifier, String world) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(identifier).getHoldings(world);
    }
    return BigDecimal.ZERO;
  }

  /**
   * Used to get the balance of an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param world The name of the {@link World} associated with the balance.
   * @param currency The name of the currency associated with the balance.
   * @return The balance of the account.
   */
  @Override
  public BigDecimal getHoldings(String identifier, String world, String currency) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(IDFinder.getID(identifier))
          .getHoldings(world, TNE.manager().currencyManager().get(world, currency));
    }
    return BigDecimal.ZERO;
  }

  /**
   * Used to get the balance of an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param world The name of the {@link World} associated with the balance.
   * @param currency The name of the currency associated with the balance.
   * @return The balance of the account.
   */
  @Override
  public BigDecimal getHoldings(UUID identifier, String world, String currency) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(identifier)
          .getHoldings(world, TNE.manager().currencyManager().get(world, currency));
    }
    return BigDecimal.ZERO;
  }

  /**
   * Used to determine if an account has at least an amount of funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to use for this check.
   * @return True if the account has at least the specified amount of funds, otherwise false.
   */
  @Override
  public boolean hasHoldings(String identifier, BigDecimal amount) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(IDFinder.getID(identifier)).hasHoldings(amount);
    }
    return false;
  }

  /**
   * Used to determine if an account has at least an amount of funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to use for this check.
   * @return True if the account has at least the specified amount of funds, otherwise false.
   */
  @Override
  public boolean hasHoldings(UUID identifier, BigDecimal amount) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(identifier).hasHoldings(amount);
    }
    return false;
  }

  /**
   * Used to determine if an account has at least an amount of funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to use for this check.
   * @param world The name of the {@link World} associated with the amount.
   * @return True if the account has at least the specified amount of funds, otherwise false.
   */
  @Override
  public boolean hasHoldings(String identifier, BigDecimal amount, String world) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(IDFinder.getID(identifier)).hasHoldings(amount, world);
    }
    return false;
  }

  /**
   * Used to determine if an account has at least an amount of funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to use for this check.
   * @param world The name of the {@link World} associated with the amount.
   * @return True if the account has at least the specified amount of funds, otherwise false.
   */
  @Override
  public boolean hasHoldings(UUID identifier, BigDecimal amount, String world) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(identifier).hasHoldings(amount, world);
    }
    return false;
  }

  /**
   * Used to determine if an account has at least an amount of funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to use for this check.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return True if the account has at least the specified amount of funds, otherwise false.
   */
  @Override
  public boolean hasHoldings(String identifier, BigDecimal amount, String world, String currency) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(IDFinder.getID(identifier))
          .hasHoldings(amount, TNE.manager().currencyManager().get(world, currency), world);
    }
    return false;
  }

  /**
   * Used to determine if an account has at least an amount of funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to use for this check.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return True if the account has at least the specified amount of funds, otherwise false.
   */
  @Override
  public boolean hasHoldings(UUID identifier, BigDecimal amount, String world, String currency) {
    if (hasAccount(identifier)) {
      return TNE.manager().getAccount(identifier)
          .hasHoldings(amount, TNE.manager().currencyManager().get(world, currency), world);
    }
    return false;
  }

  /**
   * Used to set the funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to set this accounts's funds to.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse setHoldingsDetail(String identifier, BigDecimal amount) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;

    if(amount.compareTo(minBalance) < 0) {
      return HoldingsResponse.MIN_HOLDINGS;
    }

    if(amount.compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(IDFinder.getID(identifier)).setHoldings(amount);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to set the funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to set this accounts's funds to.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse setHoldingsDetail(UUID identifier, BigDecimal amount) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;

    if(amount.compareTo(minBalance) < 0) {
      return HoldingsResponse.MIN_HOLDINGS;
    }

    if(amount.compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(identifier).setHoldings(amount);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to set the funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to set this accounts's funds to.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse setHoldingsDetail(String identifier, BigDecimal amount, String world) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;

    if(amount.compareTo(minBalance) < 0) {
      return HoldingsResponse.MIN_HOLDINGS;
    }

    if(amount.compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(IDFinder.getID(identifier)).setHoldings(amount, world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to set the funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to set this accounts's funds to.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse setHoldingsDetail(UUID identifier, BigDecimal amount, String world) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;

    if(amount.compareTo(minBalance) < 0) {
      return HoldingsResponse.MIN_HOLDINGS;
    }

    if(amount.compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(identifier).setHoldings(amount, world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to set the funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to set this accounts's funds to.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse setHoldingsDetail(String identifier, BigDecimal amount, String world, String currency) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;

    if(amount.compareTo(minBalance) < 0) {
      return HoldingsResponse.MIN_HOLDINGS;
    }

    if(amount.compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(IDFinder.getID(identifier))
          .setHoldings(amount, TNE.manager().currencyManager().get(world, currency), world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to set the funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to set this accounts's funds to.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse setHoldingsDetail(UUID identifier, BigDecimal amount, String world, String currency) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;

    if(amount.compareTo(minBalance) < 0) {
      return HoldingsResponse.MIN_HOLDINGS;
    }

    if(amount.compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(identifier)
          .setHoldings(amount, TNE.manager().currencyManager().get(world, currency), world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to add funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse addHoldingsDetail(String identifier, BigDecimal amount) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).add(amount).compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(IDFinder.getID(identifier)).addHoldings(amount);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to add funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse addHoldingsDetail(UUID identifier, BigDecimal amount) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).add(amount).compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(identifier).addHoldings(amount);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to add funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse addHoldingsDetail(String identifier, BigDecimal amount, String world) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).add(amount).compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(IDFinder.getID(identifier)).addHoldings(amount, world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to add funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse addHoldingsDetail(UUID identifier, BigDecimal amount, String world) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).add(amount).compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(identifier).addHoldings(amount, world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to add funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse addHoldingsDetail(String identifier, BigDecimal amount, String world, String currency) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).add(amount).compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(IDFinder.getID(identifier))
          .addHoldings(amount, TNE.manager().currencyManager().get(world, currency), world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to add funds to an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse addHoldingsDetail(UUID identifier, BigDecimal amount, String world, String currency) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).add(amount).compareTo(CurrencyManager.largestSupported) > 0) {
      return HoldingsResponse.MAX_HOLDINGS;
    }

    try {
      TNE.manager().getAccount(identifier)
          .addHoldings(amount, TNE.manager().currencyManager().get(world, currency), world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to determine if a call to the corresponding addHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canAddHoldingsDetail(String identifier, BigDecimal amount) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).add(amount).compareTo(CurrencyManager.largestSupported) > 0)
      return HoldingsResponse.MAX_HOLDINGS;
    return GeneralResponse.SUCCESS;
  }

  /**
   * Used to determine if a call to the corresponding addHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canAddHoldingsDetail(UUID identifier, BigDecimal amount) {
    return canAddHoldingsDetail(identifier.toString(), amount);
  }

  /**
   * Used to determine if a call to the corresponding addHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canAddHoldingsDetail(String identifier, BigDecimal amount, String world) {
    return canAddHoldingsDetail(identifier, amount);
  }

  /**
   * Used to determine if a call to the corresponding addHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canAddHoldingsDetail(UUID identifier, BigDecimal amount, String world) {
    return canAddHoldingsDetail(identifier.toString(), amount);
  }

  /**
   * Used to determine if a call to the corresponding addHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canAddHoldingsDetail(String identifier, BigDecimal amount, String world, String currency) {
    return canAddHoldingsDetail(identifier, amount);
  }

  /**
   * Used to determine if a call to the corresponding addHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to add to this account.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse canAddHoldingsDetail(UUID identifier, BigDecimal amount, String world, String currency) {
    return canAddHoldingsDetail(identifier.toString(), amount);
  }


  /**
   * Used to remove funds from an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse removeHoldingsDetail(String identifier, BigDecimal amount) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).subtract(amount).compareTo(minBalance) < 0)
      return HoldingsResponse.MIN_HOLDINGS;

    try {
      TNE.manager().getAccount(IDFinder.getID(identifier)).removeHoldings(amount);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to remove funds from an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse removeHoldingsDetail(UUID identifier, BigDecimal amount) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).subtract(amount).compareTo(minBalance) < 0)
      return HoldingsResponse.MIN_HOLDINGS;

    try {
      TNE.manager().getAccount(identifier).removeHoldings(amount);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to remove funds from an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse removeHoldingsDetail(String identifier, BigDecimal amount, String world) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).subtract(amount).compareTo(minBalance) < 0)
      return HoldingsResponse.MIN_HOLDINGS;

    try {
      TNE.manager().getAccount(IDFinder.getID(identifier)).removeHoldings(amount, world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to remove funds from an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse removeHoldingsDetail(UUID identifier, BigDecimal amount, String world) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).subtract(amount).compareTo(minBalance) < 0)
      return HoldingsResponse.MIN_HOLDINGS;

    try {
      TNE.manager().getAccount(identifier).removeHoldings(amount, world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to remove funds from an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse removeHoldingsDetail(String identifier, BigDecimal amount, String world, String currency) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).subtract(amount).compareTo(minBalance) < 0)
      return HoldingsResponse.MIN_HOLDINGS;

    try {
      TNE.manager().getAccount(IDFinder.getID(identifier))
          .removeHoldings(amount, TNE.manager().currencyManager().get(world, currency), world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to remove funds from an account.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} for this action.
   */
  @Override
  public EconomyResponse removeHoldingsDetail(UUID identifier, BigDecimal amount, String world, String currency) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).subtract(amount).compareTo(minBalance) < 0)
      return HoldingsResponse.MIN_HOLDINGS;

    try {
      TNE.manager().getAccount(identifier)
          .removeHoldings(amount, TNE.manager().currencyManager().get(world, currency), world);
      return GeneralResponse.SUCCESS;
    } catch (Exception ignore) {
      return GeneralResponse.FAILED;
    }
  }

  /**
   * Used to determine if a call to the corresponding removeHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @return The {@link EconomyResponse} that would be returned with the corresponding removeHoldingsDetail method.
   */
  @Override
  public EconomyResponse canRemoveHoldingsDetail(String identifier, BigDecimal amount) {
    if (!hasAccount(identifier) && !createAccount(identifier)) return AccountResponse.CREATION_FAILED;
    if (getHoldings(identifier).subtract(amount).compareTo(BigDecimal.ZERO) < 0)
      return HoldingsResponse.MIN_HOLDINGS;
    return GeneralResponse.SUCCESS;
  }

  /**
   * Used to determine if a call to the corresponding removeHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @return The {@link EconomyResponse} that would be returned with the corresponding removeHoldingsDetail method.
   */
  @Override
  public EconomyResponse canRemoveHoldingsDetail(UUID identifier, BigDecimal amount) {
    return canRemoveHoldingsDetail(identifier.toString(), amount);
  }

  /**
   * Used to determine if a call to the corresponding removeHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} that would be returned with the corresponding removeHoldingsDetail method.
   */
  @Override
  public EconomyResponse canRemoveHoldingsDetail(String identifier, BigDecimal amount, String world) {
    return canRemoveHoldingsDetail(identifier, amount);
  }

  /**
   * Used to determine if a call to the corresponding removeHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @param world The name of the {@link World} associated with the amount.
   * @return The {@link EconomyResponse} that would be returned with the corresponding removeHoldingsDetail method.
   */
  @Override
  public EconomyResponse canRemoveHoldingsDetail(UUID identifier, BigDecimal amount, String world) {
    return canRemoveHoldingsDetail(identifier.toString(), amount);
  }

  /**
   * Used to determine if a call to the corresponding removeHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} that would be returned with the corresponding removeHoldingsDetail method.
   */
  @Override
  public EconomyResponse canRemoveHoldingsDetail(String identifier, BigDecimal amount, String world, String currency) {
    return canRemoveHoldingsDetail(identifier, amount);
  }

  /**
   * Used to determine if a call to the corresponding removeHoldings method would be successful. This method does not
   * affect an account's funds.
   * @param identifier The identifier of the account that is associated with this call.
   * @param amount The amount you wish to remove from this account.
   * @param world The name of the {@link World} associated with the amount.
   * @param currency The name of the currency associated with the balance.
   * @return The {@link EconomyResponse} that would be returned with the corresponding removeHoldingsDetail method.
   */
  @Override
  public EconomyResponse canRemoveHoldingsDetail(UUID identifier, BigDecimal amount, String world, String currency) {
    return canRemoveHoldingsDetail(identifier.toString(), amount);
  }

  /**
   * Formats a monetary amount into a more text-friendly version.
   * @param amount The amount of currency to format.
   * @return The formatted amount.
   */
  @Override
  public String format(BigDecimal amount) {
    return plugin.api().format(amount, plugin.defaultWorld);
  }

  /**
   * Formats a monetary amount into a more text-friendly version.
   * @param amount The amount of currency to format.
   * @param world The {@link World} in which this format operation is occurring.
   * @return The formatted amount.
   */
  @Override
  public String format(BigDecimal amount, String world) {
    return plugin.api().format(amount, world);
  }

  /**
   * Formats a monetary amount into a more text-friendly version.
   * @param amount The amount of currency to format.
   * @param world The {@link World} in which this format operation is occurring.
   * @param currency The name of the currency associated with the balance.
   * @return The formatted amount.
   */
  @Override
  public String format(BigDecimal amount, String world, String currency) {
    return plugin.api().format(amount, TNE.manager().currencyManager().get(world, currency), world);
  }

  /**
   * Purges the database of accounts with the default balance.
   * @return True if the purge was completed successfully.
   */
  @Override
  public boolean purgeAccounts() {
    return false;
  }

  /**
   * Purges the database of accounts with a balance under the specified one.
   * @param amount The amount that an account's balance has to be under in order to be removed.
   * @return True if the purge was completed successfully.
   */
  @Override
  public boolean purgeAccountsUnder(BigDecimal amount) {
    return false;
  }

  @Override
  public boolean supportTransactions() {
    return true;
  }
}