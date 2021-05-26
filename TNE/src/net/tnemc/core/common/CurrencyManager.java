package net.tnemc.core.common;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.CurrencyLoader;
import net.tnemc.core.common.currency.CurrencyType;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import net.tnemc.core.common.currency.loader.AdvancedCurrencyLoader;
import net.tnemc.core.common.currency.loader.BasicCurrencyLoader;
import net.tnemc.core.common.currency.recipe.CurrencyRecipe;
import net.tnemc.core.common.currency.type.ItemType;
import net.tnemc.core.common.currency.type.VirtualType;
import net.tnemc.core.common.currency.type.XPType;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 10/22/2016.
 */
public class CurrencyManager {
  public static BigDecimal largestSupported;
  private Map<String, TNECurrency> globalCurrencies = new HashMap<>();
  private Map<String, CurrencyType> currencyTypes = new HashMap<>();

  /**
   * A map containing all of the crafting recipes enabled for TNE currencies.
   * Format: <CurrencyName:CurrencyTier, CurrencyRecipe instance></CurrencyName:CurrencyTier,>
   */
  public Map<String, CurrencyRecipe> currencyRecipes = new HashMap<>();

  //Cache-related maps.
  private List<String> globalDisabled = new ArrayList<>();

  //OP
  public static final Permission giveParent = new Permission("tne.money.give.*", "Grants access to /money give for all currencies.", PermissionDefault.OP);
  public static final Permission setParent = new Permission("tne.money.set.*", "Grants access to /money set for all currencies.", PermissionDefault.OP);
  public static final Permission takeParent = new Permission("tne.money.take.*", "Grants access to /money take for all currencies.", PermissionDefault.OP);

  //Non-op
  public static final Permission convertParent = new Permission("tne.money.convert.*", "Grants access to /money convert for all currencies.", PermissionDefault.TRUE);
  public static final Permission noteParent = new Permission("tne.money.note.*", "Grants access to /money note for all currencies.", PermissionDefault.TRUE);
  public static final Permission payParent = new Permission("tne.money.pay.*", "Grants access to /money pay for all currencies.", PermissionDefault.TRUE);

  public CurrencyManager() {
    initPermissions();

    initTypes();
  }

  public void initPermissions() {
    giveParent.addParent("tne.money.*", true);
    setParent.addParent("tne.money.*", true);
    takeParent.addParent("tne.money.*", true);

    convertParent.addParent("tne.money.*", true);
    noteParent.addParent("tne.money.*", true);
    payParent.addParent("tne.money.*", true);

    Bukkit.getServer().getPluginManager().addPermission(giveParent);
    Bukkit.getServer().getPluginManager().addPermission(setParent);
    Bukkit.getServer().getPluginManager().addPermission(takeParent);

    Bukkit.getServer().getPluginManager().addPermission(convertParent);
    Bukkit.getServer().getPluginManager().addPermission(noteParent);
    Bukkit.getServer().getPluginManager().addPermission(payParent);
  }

  public void initTypes() {
    addCurrencyType(new ItemType());
    addCurrencyType(new VirtualType());
    addCurrencyType(new XPType());
  }

  public void loadRecipes() {
    for(CurrencyRecipe recipe : currencyRecipes.values()) {
      recipe.register();
    }
  }

  public void loadCurrencies() {
    largestSupported = new BigDecimal("900000000000000000000000000000000000000000000");

    CurrencyLoader loader;

    if(TNE.instance().api().getBoolean("Core.Currency.Info.Advanced")) {
      loader = new AdvancedCurrencyLoader();
    } else {
      loader = new BasicCurrencyLoader();
    }
    loader.loadCurrencies();


    for(WorldManager manager : TNE.instance().getWorldManagers()) {
      initializeWorld(manager.getWorld());
    }
    largestSupported = null;
  }

  public void addCurrencyType(CurrencyType type) {
    currencyTypes.put(type.name().toLowerCase(), type);
  }

  public CurrencyType getType(String name) {
    return currencyTypes.getOrDefault(name, currencyTypes.get("virtual"));
  }

  public void addCurrency(String world, TNECurrency currency) {
    TNE.debug("[Add]Loading Currency: " + currency.name() + " for world: " + world + " with default balance of " + currency.defaultBalance());
    if(currency.isGlobal()) {
      globalCurrencies.put(currency.name(), currency);
    } else {
      for(String w : currency.getWorlds()) {
        WorldManager manager = TNE.instance().getWorldManager(w);
        if (manager != null) {
          TNE.debug("[Add]Adding Currency: " + currency.name() + " for world: " + w);
          manager.addCurrency(currency);
        }
        TNE.instance().addWorldManager(manager);
      }
    }
  }

  public void disableAll(String currency) {
    globalDisabled.add(currency);
    for(WorldManager manager : TNE.instance().getWorldManagers()) {
      TNE.instance().getWorldManager(manager.getWorld()).disable(currency, true);
    }
  }

  public void initializeWorld(String world) {
    TNE.debug("Initializing World: " + world);
    if(!TNE.instance().getWorldManagersMap().containsKey(world)) {
      WorldManager manager = new WorldManager(world);
      TNE.instance().addWorldManager(manager);
    }

    WorldManager manager = TNE.instance().getWorldManager(world);
    for(TNECurrency currency : globalCurrencies.values()) {
      if(!globalDisabled.contains(currency.name())) {
        if(manager == null) {
          TNE.debug("World Manager for world: " + world + " is null. Skipping.");
          break;
        }
        manager.addCurrency(currency);
      }
    }
    TNE.instance().addWorldManager(manager);
  }

  public TNECurrency get(String world) {
    if(TNE.instance().hasWorldManager(world)) {
      final String normalized = TNE.instance().getWorldManager(world).getConfigurationWorld();
      if (TNE.instance().api().hasConfiguration("World." + world + ".DefaultCurrency")) {
        final String defaultCurrency = TNE.instance().api().getString("World." + normalized + ".DefaultCurrency");

        if(TNE.instance().getWorldManager(world).hasCurrency(defaultCurrency)) {
          return TNE.instance().getWorldManager(world).getCurrency(defaultCurrency);
        }

      }
    }


    for(TNECurrency currency : TNE.instance().getWorldManager(world).getCurrencies()) {
      TNE.debug("Currency: " + currency.name() + " World: " + world + " Default? " + currency.isDefault());
      if(currency.isDefault()) {
        TNE.debug("Returning default Currency of " + currency.name() + " for world " + world);
        return currency;
      }
    }
    return null;
  }

  public TNECurrency get(String world, Location location) {
    return MISCUtils.findCurrency(world, location);
  }

  public TNECurrency get(String world, String name) {
    //TNE.debug("Currency: " + name);
    //TNE.debug("World: " + world);
    //TNE.debug("WorldManager null for " + world +"? " + (TNE.instance().getWorldManager(world) == null));
    if(TNE.instance().getWorldManager(world).containsCurrency(name)) {
      //TNE.debug("Returning Currency " + name + " for world " + world);
      return TNE.instance().getWorldManager(world).getCurrency(name);
    }
    return get(world);
  }

  public BigDecimal convert(TNECurrency from, TNECurrency to, BigDecimal amount) {
    double fromRate = from.getRate();
    double toRate = to.getRate();

    return convert(fromRate, toRate, amount);
  }

  public BigDecimal convert(TNECurrency from, double toRate, BigDecimal amount) {
    return convert(from.getRate(), toRate, amount);
  }

  public BigDecimal convert(double fromRate, double toRate, BigDecimal amount) {
    double rate = fromRate - toRate;
    BigDecimal difference = amount.multiply(new BigDecimal(rate + ""));

    return amount.add(difference);
  }

  public boolean contains(String world) {
    return TNE.instance().getWorldManager(world) != null;
  }

  public Collection<TNECurrency> getWorldCurrencies(String world) {
    TNE.debug("=====START CurrencyManager =====");
    TNE.debug("World: " + world);
    world = TNE.instance().getWorldManager(world).getBalanceWorld();
    return TNE.instance().getWorldManager(world).getCurrencies();
  }

  public Collection<TNECurrency> getCurrencies() {
    List<TNECurrency> currencies = new ArrayList<>();
    TNE.instance().getWorldManagers().forEach((worldManager)->{
      currencies.addAll(worldManager.getCurrencies());
    });

    return currencies;
  }

  public void rename(String world, String currency, String newName, boolean renameInternal) {
    if(renameInternal && TNE.instance().getWorldManager(world).containsCurrency(currency)) {
      TNECurrency reference = get(world, currency);
      reference.setSingle(newName);
      TNE.instance().getWorldManager(world).removeCurrency(currency);
      TNE.instance().getWorldManager(world).addCurrency(reference);
    }
    TNE.manager().getAccounts().forEach((id, account)->{
      account.setHoldings(world, newName, account.getHoldings(world, TNE.manager().currencyManager().get(world, currency)));
      //account.getWorldHoldings(world).remove(currency);
      try {
        TNE.saveManager().getTNEManager().getTNEProvider().deleteBalance(id, world, currency);
      } catch (SQLException e) {
        TNE.debug(e);
      }
    });
  }

  public void register(net.tnemc.core.economy.currency.Currency currency) {
    addCurrency(TNE.instance().defaultWorld, TNECurrency.fromReserve(currency));
  }

  public ItemStack createNote(String currency, String world, BigDecimal amount) {
    ItemStack stack = TNE.manager().currencyManager().get(world, currency).getNote().build();

    ItemMeta meta = stack.getItemMeta();

    List<String> lore = (meta.hasLore())? meta.getLore() : new ArrayList<>();
    lore.add(ChatColor.WHITE + "Currency: " + currency);
    lore.add(ChatColor.WHITE + "World: " + world);
    lore.add(ChatColor.WHITE + "Amount: " + amount.toPlainString());
    lore.add(ChatColor.GREEN + "Right click to claim note.");

    meta.setDisplayName("Currency Note");
    meta.setLore(lore);
    stack.setItemMeta(meta);

    return stack;
  }

  public Optional<TNETransaction> claimNote(UUID id, ItemStack stack) {
    TNE.debug("=====START CurrencyManager.claimNote =====");
    String currency = null;
    String world = null;
    String amount = null;

    if(stack.hasItemMeta()) {
      ItemMeta meta = stack.getItemMeta();

      for(String s : meta.getLore()) {
        String[] info = s.split(":");

        switch (ChatColor.stripColor(info[0]).toLowerCase()) {
          case "currency":
            TNE.debug("Currency Found: " + info[1]);
            currency = info[1].trim();
            break;
          case "world":
            TNE.debug("World Found: " + info[1]);
            world = info[1].trim();
            break;
          case "amount":
            TNE.debug("Amount Found: " + info[1]);
            amount = info[1].trim();
            break;
        }
      }
    }

    if(currency == null || world == null || amount == null) return null;


    BigDecimal value = new BigDecimal(amount);
    TNE.debug("Value: " + value.toPlainString());

    TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(id), TNE.manager().getAccount(id), world, TNE.transactionManager().getType("noteclaim"));
    transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world, currency),
        value, TransactionChargeType.GAIN)
    );
    TNE.debug("RecipientCharge: " + transaction.recipientCharge().getAmount().toPlainString());
    TNE.debug("=====END CurrencyManager.claimNote =====");
    return Optional.of(transaction);
  }

  public Optional<TNECurrency> currencyFromItem(String world, ItemStack stack) {
    ItemStack clone = stack.clone();
    clone.setAmount(1);
    for(TNECurrency currency : TNE.instance().getWorldManager(world).getCurrencies()) {
      if(currency.isItem() && (isMajorItem(world, currency.name(), clone) ||
          isMinorItem(world, currency.name(), clone))) {
        return Optional.of(currency);
      }
    }
    return Optional.empty();
  }

  public boolean isMajorItem(String world, String currency, ItemStack stack) {
    for(Object tier : TNE.instance().getWorldManager(world).getCurrency(currency).getTNEMajorTiers().values()) {
      if((tier instanceof TNETier) && ((TNETier)tier).getItemInfo().toStack().equals(stack)) {
        return true;
      }
    }
    return false;
  }

  public boolean isMinorItem(String world, String currency, ItemStack stack) {
    for(Object tier : TNE.instance().getWorldManager(world).getCurrency(currency).getTNEMinorTiers().values()) {
      if((tier instanceof TNETier) && ((TNETier)tier).getItemInfo().toStack().equals(stack)) {
        return true;
      }
    }
    return false;
  }

  public boolean contains(String world, String name) {
    TNE.debug("CurrencyManager.contains(" + world + ", " + name + ")");
    return TNE.instance().getWorldManager(world).containsCurrency(name);
  }
}