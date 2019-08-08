package net.tnemc.core.common;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.CurrencyNote;
import net.tnemc.core.common.currency.CurrencyType;
import net.tnemc.core.common.currency.ItemTier;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import net.tnemc.core.common.currency.recipe.CurrencyLegacyShapedRecipe;
import net.tnemc.core.common.currency.recipe.CurrencyRecipe;
import net.tnemc.core.common.currency.recipe.CurrencyShapedRecipe;
import net.tnemc.core.common.currency.recipe.CurrencyShapelessRecipe;
import net.tnemc.core.common.currency.type.ItemType;
import net.tnemc.core.common.currency.type.VirtualType;
import net.tnemc.core.common.currency.type.XPType;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.event.currency.TNECurrencyCraftingRecipeEvent;
import net.tnemc.core.event.currency.TNECurrencyLoadEvent;
import net.tnemc.core.event.currency.TNECurrencyTierLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
 * Created by creatorfromhell on 10/22/2016.
 */
public class CurrencyManager {
  private static BigDecimal largestSupported;
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
  Permission giveParent = new Permission("tne.money.give.*", "Grants access to /money give for all currencies.", PermissionDefault.OP);
  Permission setParent = new Permission("tne.money.set.*", "Grants access to /money set for all currencies.", PermissionDefault.OP);
  Permission takeParent = new Permission("tne.money.take.*", "Grants access to /money take for all currencies.", PermissionDefault.OP);

  //Non-op
  Permission convertParent = new Permission("tne.money.convert.*", "Grants access to /money convert for all currencies.", PermissionDefault.TRUE);
  Permission noteParent = new Permission("tne.money.note.*", "Grants access to /money note for all currencies.", PermissionDefault.TRUE);
  Permission payParent = new Permission("tne.money.pay.*", "Grants access to /money pay for all currencies.", PermissionDefault.TRUE);

  public CurrencyManager() {
    initPermissions();

    initTypes();

    loadCurrencies();
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

    if(TNE.instance().api().getBoolean("Core.Currency.Info.Advanced")) {
      loadCurrency(TNE.instance().getCurrencyConfigurations(), false, TNE.instance().defaultWorld);
    } else {
      loadBasic();
    }
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

  private void loadBasic() {

    final String base = "Core.Currency.Basic";

    //Currency Info Configurations.
    final String server = TNE.instance().mainConfigurations().getString(base + ".Info.Server", "Main Server");
    final String identifier = TNE.instance().mainConfigurations().getString(base + ".Identifier", "Dollar");
    final String single = TNE.instance().mainConfigurations().getString(base + ".Major_Single", "Dollar");
    final String plural = TNE.instance().mainConfigurations().getString(base + ".Major_Plural", "Dollars");
    final String singleMinor = TNE.instance().mainConfigurations().getString(base + ".Minor_Single", "Cent");
    final String pluralMinor = TNE.instance().mainConfigurations().getString(base + ".Minor_Plural", "Cents");
    final String prefixes = TNE.instance().mainConfigurations().getString(base + ".Prefixes", "kMGTPEZYXWVUN₮").trim();
    final String symbol = TNE.instance().mainConfigurations().getString(base + ".Symbol", "$");
    final String currencyType = TNE.instance().mainConfigurations().getString(base + ".Type", "virtual");

    //Currency Options Configurations.
    final String format = TNE.instance().mainConfigurations().getString(base + ".Options.Format", "<symbol><major.amount><decimal><minor.amount>").trim();
    final BigDecimal maxBalance = ((new BigDecimal(TNE.instance().mainConfigurations().getString(base + ".Options.MaxBalance", largestSupported.toPlainString())).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(TNE.instance().mainConfigurations().getString(base + ".MaxBalance", largestSupported.toPlainString())));
    final BigDecimal balance = new BigDecimal(TNE.instance().mainConfigurations().getString(base + ".Options.Balance", "200.00"));
    final String decimal = TNE.instance().mainConfigurations().getString(base + ".Options.Decimal", ".");
    final Boolean ender = TNE.instance().mainConfigurations().getBool(base + ".Options.EnderChest", true);
    final Boolean separate = TNE.instance().mainConfigurations().getBool(base + ".Options.Major_Separate", true);
    final String separator = TNE.instance().mainConfigurations().getString(base + ".Options.Major_Separator", ",");
    final Integer minorWeight = TNE.instance().mainConfigurations().getInt(base + ".Options.Minor_Weight", 100);


    //Currency Note Configurations
    final Boolean notable = TNE.instance().mainConfigurations().getBool(base + ".Note.Notable", false);
    final BigDecimal fee = new BigDecimal(TNE.instance().mainConfigurations().getString(base + ".Note.Fee", "0.00"));
    final BigDecimal minimum = new BigDecimal(TNE.instance().mainConfigurations().getString(base + ".Note.Minimum", "0.00"));


    //Note Item Configurations
    final String material = TNE.instance().mainConfigurations().getString(base + ".Note.Item.Material", "PAPER");

    CurrencyNote currencyNote = new CurrencyNote(material);
    currencyNote.setTexture(TNE.instance().mainConfigurations().getString(base + ".Note.Item.Texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA0NzE5YjNiOTdkMTk1YTIwNTcxOGI2ZWUyMWY1Yzk1Y2FmYTE2N2U3YWJjYTg4YTIxMDNkNTJiMzdkNzIyIn19fQ=="));
    currencyNote.setCustomModelData(TNE.instance().mainConfigurations().getInt(base + ".Note.Item.ModelData", -1));

    if(TNE.instance().mainConfigurations().contains(base + ".Note.Item.Enchantments")) {
      currencyNote.setEnchantments(TNE.instance().mainConfigurations().getStringList(base + ".Note.Item.Enchantments"));
    }

    if(TNE.instance().mainConfigurations().contains(base + ".Note.Item.Flags")) {
      currencyNote.setFlags(TNE.instance().mainConfigurations().getStringList(base + ".Note.Item.Flags"));
    }
    
    TNE.debug("Symbol: " + symbol);
    TNE.debug("Symbol: " + TNE.instance().mainConfigurations().getString(base + ".Symbol", "$"));
    TNE.debug("identifier: " + identifier);

    TNECurrency currency = new TNECurrency();
    currency.setNote(currencyNote);
    currency.setIdentifier(identifier);
    currency.setMaxBalance(maxBalance);
    currency.setBalance(balance);
    currency.setDecimal(decimal);
    currency.setDecimalPlaces(2);
    currency.setFormat(format);
    currency.setPrefixes(prefixes);
    currency.setSingle(single);
    currency.setPlural(plural);
    currency.setSingleMinor(singleMinor);
    currency.setPluralMinor(pluralMinor);
    currency.setServer(server);
    currency.setSymbol(symbol);
    currency.setWorldDefault(true);
    currency.setRate(1.0);
    currency.setType(currencyType);
    currency.setNotable(notable);
    currency.setFee(fee);
    currency.setMinimum(minimum);
    currency.setEnderChest(ender);
    currency.setSeparateMajor(separate);
    currency.setMajorSeparator(separator);
    currency.setMinorWeight(minorWeight);
    TNE.debug("Symbol: " + currency.symbol());

    loadBasicTiers(currency, TNE.instance().mainConfigurations());

    addCurrency(TNE.instance().defaultWorld, currency);
  }

  private void loadBasicTiers(TNECurrency currency, CommentedConfiguration configuration) {
    final String baseNode = "Core.Currency.Basic." + ((currency.isItem())? "Items" : "Virtual");
    Set<String> tiers = configuration.getSection(baseNode).getKeys(false);

    for (String tierName : tiers) {

      //Normal TNETier variables
      String unparsedValue = configuration.getString(baseNode + "." + tierName);

      final String type = (unparsedValue.contains("."))? "Minor" : "Major";

      if(type.equalsIgnoreCase("minor")) {
        unparsedValue = unparsedValue.split("\\.")[1];
      }

      ItemTier itemTier = null;

      if (currency.isItem()) {
        itemTier = new ItemTier(tierName, (short)0);
        itemTier.setName(null);
        itemTier.setLore(null);
      }

      TNETier tier = new TNETier();
      tier.setMajor(type.equalsIgnoreCase("major"));
      tier.setItemInfo(itemTier);
      tier.setSingle(tierName);
      tier.setPlural(tierName + "s");
      tier.setWeight(new BigInteger(unparsedValue));

      if (type.equalsIgnoreCase("minor")) {
        currency.addTNEMinorTier(tier);
        continue;
      }
      currency.addTNEMajorTier(tier);
    }
  }

  private void loadCurrency(CommentedConfiguration configuration, boolean world, String worldName) {
    String curBase = ((world)? "Worlds." + worldName + "." : "") + "Currencies";

    if(configuration.contains(curBase)) {

      Set<String> currencies = configuration.getSection(curBase).getKeys(false);
      TNE.debug(currencies.toArray().toString());

      for(String cur : currencies) {
        final String base = curBase + "." + cur;
        if (configuration.contains(base + ".Options.Disabled") &&
            configuration.getBool(base + ".Options.Disabled")) {
              return;
        }

        TNE.debug("[Loop]Loading Currency: " + cur + " for world: " + worldName);
        final String identifier = configuration.getString(base + ".Info.Identifier");


        //OP
        Permission give = new Permission("tne.money.give." + identifier, "Grants access to /money give for currency: " + identifier, PermissionDefault.OP);
        give.addParent(giveParent, true);
        Bukkit.getServer().getPluginManager().addPermission(give);

        Permission set = new Permission("tne.money.set." + identifier, "Grants access to /money set for currency: " + identifier, PermissionDefault.OP);
        set.addParent(setParent, true);
        Bukkit.getServer().getPluginManager().addPermission(set);

        Permission take = new Permission("tne.money.take." + identifier, "Grants access to /money take for currency: " + identifier, PermissionDefault.OP);
        take.addParent(takeParent, true);
        Bukkit.getServer().getPluginManager().addPermission(take);

        //Non-op
        Permission convert = new Permission("tne.money.convert." + identifier, "Grants access to /money convert for currency: " + identifier, PermissionDefault.TRUE);
        convert.addParent(convertParent, true);
        Bukkit.getServer().getPluginManager().addPermission(convert);

        Permission note = new Permission("tne.money.note." + identifier, "Grants access to /money note for currency: " + identifier, PermissionDefault.TRUE);
        note.addParent(noteParent, true);
        Bukkit.getServer().getPluginManager().addPermission(note);

        Permission pay = new Permission("tne.money.pay." + identifier, "Grants access to /money pay for currency: " + identifier, PermissionDefault.TRUE);
        pay.addParent(payParent, true);
        Bukkit.getServer().getPluginManager().addPermission(pay);

        //Currency Info Configurations.
        final String server = configuration.getString(base + ".Info.Server", "Main Server");
        final String single = configuration.getString(base + ".Info.Major_Single", "Dollar");
        final String plural = configuration.getString(base + ".Info.Major_Plural", "Dollars");
        final String singleMinor = configuration.getString(base + ".Info.Minor_Single", "Cent");
        final String pluralMinor = configuration.getString(base + ".Info.Minor_Plural", "Cents");
        final String prefixes = configuration.getString(base + ".Info.Prefixes", "kMGTPEZYXWVUN₮").trim();
        final String symbol = configuration.getString(base + ".Info.Symbol", "$");

        //Currency Options Configurations.
        final Boolean worldDefault = configuration.getBool(base + ".Options.Default", true);
        List<String> worlds = configuration.getStringList(base + ".Options.Worlds");
        final Boolean global = configuration.getBool(base + ".Options.Global", true);
        final String format = configuration.getString(base + ".Options.Format", "<symbol><major.amount><decimal><minor.amount>").trim();
        final BigDecimal maxBalance = ((new BigDecimal(configuration.getString(base + ".Options.MaxBalance", largestSupported.toPlainString())).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(configuration.getString(base + ".MaxBalance", largestSupported.toPlainString())));
        final BigDecimal balance = new BigDecimal(configuration.getString(base + ".Options.Balance", "200.00"));
        final String decimal = configuration.getString(base + ".Options.Decimal", ".");
        final Integer decimalPlaces = ((configuration.getInt(base + ".Options.DecimalPlaces", 2) > 4)? 4 : configuration.getInt(base + ".Options.DecimalPlaces", 2));
        final String currencyType = configuration.getString(base + ".Options.Type", "virtual");
        final Boolean ender = configuration.getBool(base + ".Options.EnderChest", true);
        final Boolean separate = configuration.getBool(base + ".Options.Major_Separate", true);
        final String separator = configuration.getString(base + ".Options.Major_Separator", ",");
        final Integer minorWeight = configuration.getInt(base + ".Options.Minor_Weight", 100);

        //Currency Note Configurations
        final Boolean notable = configuration.getBool(base + ".Note.Notable", false);
        final BigDecimal fee = new BigDecimal(configuration.getString(base + ".Note.Fee", "0.00"));
        final BigDecimal minimum = new BigDecimal(configuration.getString(base + ".Note.Minimum", "0.00"));
        
        //Note Item Configurations
        final String material = configuration.getString(base + ".Note.Item.Material", "PAPER");

        CurrencyNote currencyNote = new CurrencyNote(material);
        currencyNote.setTexture(configuration.getString(base + ".Note.Item.Texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA0NzE5YjNiOTdkMTk1YTIwNTcxOGI2ZWUyMWY1Yzk1Y2FmYTE2N2U3YWJjYTg4YTIxMDNkNTJiMzdkNzIyIn19fQ=="));
        currencyNote.setCustomModelData(configuration.getInt(base + ".Note.Item.ModelData", -1));

        if(configuration.contains(base + ".Note.Item.Enchantments")) {
          currencyNote.setEnchantments(configuration.getStringList(base + ".Note.Item.Enchantments"));
        }

        if(configuration.contains(base + ".Note.Item.Flags")) {
          currencyNote.setFlags(configuration.getStringList(base + ".Note.Item.Flags"));
        }
        
        

        //Currency Conversion Configurations.
        final Double rate = configuration.getDouble(base + ".Conversion.Rate", 1.0);

        //TNE.debug(cur + ": " + format);
        //TNE.debug(cur + ": " + decimalPlaces);
        //TNE.debug(cur + ": " + symbol);

        if(worlds == null) worlds = new ArrayList<>();
        if(worlds.size() < 1) worlds.add(TNE.instance().defaultWorld);

        TNECurrency currency = new TNECurrency();
        currency.setNote(currencyNote);
        currency.setIdentifier(identifier);
        currency.setMaxBalance(maxBalance);
        currency.setBalance(balance);
        currency.setDecimal(decimal);
        currency.setDecimalPlaces(decimalPlaces);
        currency.setFormat(format);
        currency.setPrefixes(prefixes);
        currency.setSingle(single);
        currency.setPlural(plural);
        currency.setSingleMinor(singleMinor);
        currency.setPluralMinor(pluralMinor);
        currency.setServer(server);
        currency.setSymbol(symbol);
        currency.setWorldDefault(worldDefault);
        currency.setWorlds(worlds);
        currency.setGlobal(global);
        currency.setRate(rate);
        currency.setType(currencyType);
        currency.setNotable(notable);
        currency.setFee(fee);
        currency.setMinimum(minimum);
        currency.setEnderChest(ender);
        currency.setSeparateMajor(separate);
        currency.setMajorSeparator(separator);
        currency.setMinorWeight(minorWeight);

        loadTiers(worldName, currency, configuration, base + ".Tiers");

        TNECurrencyLoadEvent event = new TNECurrencyLoadEvent(worldName, currency.name(), !Bukkit.getServer().isPrimaryThread());
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
          addCurrency(worldName, currency);
        }
      }
    }
  }

  private void loadTiers(String world, TNECurrency currency, CommentedConfiguration configuration, String baseNode) {
    Set<String> tiers = configuration.getSection(baseNode).getKeys(false);
    for(String tierName : tiers) {
      String tierBase = baseNode + "." + tierName;

      //Normal TNETier variables
      String type = configuration.getString(tierBase + ".Info.Type", "Major");
      String single = configuration.getString(tierBase + ".Info.Single", "Dollar");
      String plural = configuration.getString(tierBase + ".Info.Plural", "Dollars");
      BigInteger weight = new BigInteger(configuration.getString(tierBase + ".Options.Weight", "1"));
      ItemTier item = null;

      if(currency.isItem()) {
        //ItemTier variables
        String material = configuration.getString(tierBase + ".Options.Material", "PAPER");
        short damage = (short) configuration.getInt(tierBase + ".Options.Damage", 0);
        String customName = configuration.getString(tierBase + ".Options.Name", null);
        String lore = configuration.getString(tierBase + ".Options.Lore", null);
        int customModel = configuration.getInt(tierBase + ".Options.ModelData", -1);

        item = new ItemTier(material, damage);
        item.setName(customName);
        item.setLore(lore);

        if(customModel > 0) {
          item.setCustomModel(customModel);
        }

        if(configuration.contains(tierBase + ".Options.Enchantments")) {
          //TNE.debug("Setting enchantments list: " + configuration.getStringList(tierBase + ".Options.Enchantments").toString());
          item.setEnchantments(configuration.getStringList(tierBase + ".Options.Enchantments"));
        }

        if(configuration.contains(tierBase + ".Options.Flags")) {
          item.setFlags(configuration.getStringList(tierBase + ".Options.Flags"));
        }
      }

      if(currency.isItem() && configuration.contains(tierBase + ".Options.Crafting")) {
        if(configuration.getBool(tierBase + ".Options.Crafting.Enabled", false)) {
          final boolean shapeless = configuration.getBool(tierBase + ".Options.Crafting.Shapeless", false);
          ItemStack stack = item.toStack().clone();
          stack.setAmount(configuration.getInt(tierBase + ".Options.Crafting.Amount", 1));

          CurrencyRecipe recipe = null;

          if(shapeless) {
            recipe = new CurrencyShapelessRecipe(currency.getIdentifier(), tierName, stack);
          } else {
            if(MISCUtils.isOneThirteen()) {
              recipe = new CurrencyShapedRecipe(currency.getIdentifier(), tierName, stack);
            } else {
              recipe = new CurrencyLegacyShapedRecipe(currency.getIdentifier(), tierName, stack);
            }
          }

          recipe.setCraftingMatrix(configuration.getStringList(tierBase + ".Options.Crafting.Recipe"));
          recipe.setMaterialsRaw(configuration.getStringList(tierBase + ".Options.Crafting.Materials"));

          TNECurrencyCraftingRecipeEvent event = new TNECurrencyCraftingRecipeEvent(world, currency.getIdentifier(),
              tierName, type, recipe, !Bukkit.isPrimaryThread());

          Bukkit.getPluginManager().callEvent(event);

          currencyRecipes.put(currency.getIdentifier() + ":" + tierName, event.getRecipe());
        }
      }

      TNETier tier = new TNETier();
      tier.setMajor(type.equalsIgnoreCase("major"));
      tier.setItemInfo(item);
      tier.setSingle(single);
      tier.setPlural(plural);
      tier.setWeight(weight);

      TNECurrencyTierLoadedEvent event = new TNECurrencyTierLoadedEvent(world, currency.name(), tier.singular(), type, !Bukkit.getServer().isPrimaryThread());
      Bukkit.getServer().getPluginManager().callEvent(event);

      if(!event.isCancelled()) {
        if (type.equalsIgnoreCase("minor")) {
          currency.addTNEMinorTier(tier);
          continue;
        }
        currency.addTNEMajorTier(tier);
      }
    }
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
    loadCurrency(TNE.instance().worldConfiguration(), true, world);
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
    ItemStack clone = stack;
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