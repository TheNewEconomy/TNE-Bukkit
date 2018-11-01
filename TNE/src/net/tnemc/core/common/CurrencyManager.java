package net.tnemc.core.common;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.ItemTier;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.event.currency.TNECurrencyLoadEvent;
import net.tnemc.core.event.currency.TNECurrencyTierLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
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

  //Cache-related maps.
  private List<String> globalDisabled = new ArrayList<>();

  public CurrencyManager() {
    loadCurrencies();
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

  private void loadBasic() {

    final String base = "Core.Currency.Basic";

    //Currency Info Configurations.
    final String server = TNE.instance().getConfig().getString(base + ".Info.Server", "Main Server");
    final String single = TNE.instance().getConfig().getString(base + ".Major_Single", "Dollar");
    final String plural = TNE.instance().getConfig().getString(base + ".Major_Plural", "Dollars");
    final String singleMinor = TNE.instance().getConfig().getString(base + ".Minor_Single", "Cent");
    final String pluralMinor = TNE.instance().getConfig().getString(base + ".Minor_Plural", "Cents");
    final String prefixes = TNE.instance().getConfig().getString(base + ".Prefixes", "kMGTPEZYXWVUN₮").trim();
    final String symbol = TNE.instance().getConfig().getString(base + ".Symbol", "$");
    final Boolean item = TNE.instance().getConfig().getBoolean(base + ".ItemCurrency");
    final Boolean experience = TNE.instance().getConfig().getBoolean(base + ".ExperienceCurrency");

    //Currency Options Configurations.
    final String format = TNE.instance().getConfig().getString(base + ".Options.Format", "<symbol><major.amount><decimal><minor.amount>").trim();
    final BigDecimal maxBalance = ((new BigDecimal(TNE.instance().getConfig().getString(base + ".Options.MaxBalance", largestSupported.toPlainString())).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(TNE.instance().getConfig().getString(base + ".MaxBalance", largestSupported.toPlainString())));
    final BigDecimal balance = new BigDecimal(TNE.instance().getConfig().getString(base + ".Options.Balance", "200.00"));
    final String decimal = TNE.instance().getConfig().getString(base + ".Options.Decimal", ".");
    final Boolean ender = TNE.instance().getConfig().getBoolean(base + ".Options.EnderChest", true);
    final Boolean separate = TNE.instance().getConfig().getBoolean(base + ".Options.Major_Separate", true);
    final String separator = TNE.instance().getConfig().getString(base + ".Options.Major_Separator", ",");
    final Integer minorWeight = TNE.instance().getConfig().getInt(base + ".Options.Minor_Weight", 100);

    //Currency Note Configurations
    final Boolean notable = TNE.instance().getConfig().getBoolean(base + ".Note.Notable", false);
    final BigDecimal fee = new BigDecimal(TNE.instance().getConfig().getString(base + ".Note.Fee", "0.00"));
    final BigDecimal minimum = new BigDecimal(TNE.instance().getConfig().getString(base + ".Note.Minimum", "0.00"));

    TNECurrency currency = new TNECurrency();
    currency.setIdentifier(single);
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
    currency.setServer("Main Server");
    currency.setSymbol(symbol);
    currency.setWorldDefault(true);
    currency.setRate(1.0);
    currency.setItem(item);
    currency.setXp(experience);
    currency.setNotable(notable);
    currency.setFee(fee);
    currency.setMinimum(minimum);
    currency.setEnderChest(ender);
    currency.setSeparateMajor(separate);
    currency.setMajorSeparator(separator);
    currency.setMinorWeight(minorWeight);

    loadBasicTiers(currency, TNE.instance().getConfig(), item);

    addCurrency(TNE.instance().defaultWorld, currency);
  }

  private void loadBasicTiers(TNECurrency currency, FileConfiguration configuration, boolean item) {
    final String baseNode = "Core.Currency.Basic" + ((item)? "Items" : "Virtual");
    Set<String> tiers = configuration.getConfigurationSection(baseNode).getKeys(false);

    for (String tierName : tiers) {

      //Normal TNETier variables
      String unparsedValue = configuration.getString(baseNode + "." + tierName);

      final String type = (unparsedValue.contains("."))? "Minor" : "Major";

      if(type.equalsIgnoreCase("minor")) {
        unparsedValue = unparsedValue.split("\\.")[1];
      }

      ItemTier itemTier = null;

      if (item) {
        itemTier = new ItemTier(tierName, (short)0);
        itemTier.setName(null);
        itemTier.setLore(null);
      }

      TNETier tier = new TNETier();
      tier.setMajor(type.equalsIgnoreCase("major"));
      tier.setItemInfo(itemTier);
      tier.setSingle(tierName);
      tier.setPlural(tierName + "s");
      tier.setWeight(Integer.valueOf(unparsedValue));

      if (type.equalsIgnoreCase("minor")) {
        currency.addTNEMinorTier(tier);
        continue;
      }
      currency.addTNEMajorTier(tier);
    }
  }

  private void loadCurrency(FileConfiguration configuration, boolean world, String worldName) {
    String curBase = ((world)? "Worlds." + worldName : "Core") + ".Currencies";
    if(configuration.contains(curBase)) {

      Set<String> currencies = configuration.getConfigurationSection(curBase).getKeys(false);
      TNE.debug(currencies.toArray().toString());

      for(String cur : currencies) {
        if (configuration.contains("Core.Currencies." + cur + ".Options.Disabled") &&
            configuration.getBoolean("Core.Currencies." + cur + ".Options.Disabled")) {
              return;
        }

        TNE.debug("[Loop]Loading Currency: " + cur + " for world: " + worldName);
        String base = curBase + "." + cur;

        //Currency Info Configurations.
        final String server = configuration.getString(base + ".Info.Server", "Main Server");
        final String single = configuration.getString(base + ".Info.Major_Single", "Dollar");
        final String plural = configuration.getString(base + ".Info.Major_Plural", "Dollars");
        final String singleMinor = configuration.getString(base + ".Info.Minor_Single", "Cent");
        final String pluralMinor = configuration.getString(base + ".Info.Minor_Plural", "Cents");
        final String prefixes = configuration.getString(base + ".Info.Prefixes", "kMGTPEZYXWVUN₮").trim();
        final String symbol = configuration.getString(base + ".Info.Symbol", "$");

        //Currency Options Configurations.
        final Boolean worldDefault = configuration.getBoolean(base + ".Options.Default", true);
        final String format = configuration.getString(base + ".Options.Format", "<symbol><major.amount><decimal><minor.amount>").trim();
        final BigDecimal maxBalance = ((new BigDecimal(configuration.getString(base + ".Options.MaxBalance", largestSupported.toPlainString())).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(configuration.getString(base + ".MaxBalance", largestSupported.toPlainString())));
        final BigDecimal balance = new BigDecimal(configuration.getString(base + ".Options.Balance", "200.00"));
        final String decimal = configuration.getString(base + ".Options.Decimal", ".");
        final Integer decimalPlaces = ((configuration.getInt(base + ".Options.DecimalPlace", 2) > 4)? 4 : configuration.getInt(base + ".DecimalPlace", 2));
        final Boolean experience = configuration.getBoolean(base + ".Options.Experience");
        final Boolean item = configuration.getBoolean(base + ".Options.ItemCurrency");
        final Boolean ender = configuration.getBoolean(base + ".Options.EnderChest", true);
        final Boolean separate = configuration.getBoolean(base + ".Options.Major_Separate", true);
        final String separator = configuration.getString(base + ".Options.Major_Separator", ",");
        final Integer minorWeight = configuration.getInt(base + ".Options.Minor_Weight", 100);

        //Currency Note Configurations
        final Boolean notable = configuration.getBoolean(base + ".Note.Notable", false);
        final BigDecimal fee = new BigDecimal(configuration.getString(base + ".Note.Fee", "0.00"));
        final BigDecimal minimum = new BigDecimal(configuration.getString(base + ".Note.Minimum", "0.00"));

        //Currency Conversion Configurations.
        final Double rate = configuration.getDouble(base + ".Conversion.Rate", 1.0);

        TNECurrency currency = new TNECurrency();
        currency.setIdentifier(configuration.getString(base + ".Info.Identifier"));
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
        currency.setRate(rate);
        currency.setItem(item);
        currency.setXp(experience);
        currency.setNotable(notable);
        currency.setFee(fee);
        currency.setMinimum(minimum);
        currency.setEnderChest(ender);
        currency.setSeparateMajor(separate);
        currency.setMajorSeparator(separator);
        currency.setMinorWeight(minorWeight);

        loadTiers(worldName, currency, configuration, base + ".Tiers");

        TNECurrencyLoadEvent event = new TNECurrencyLoadEvent(worldName, currency.name());
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
          addCurrency(worldName, currency);
        }
      }
    }
  }

  private void loadTiers(String world, TNECurrency currency, FileConfiguration configuration, String baseNode) {
    Set<String> tiers = configuration.getConfigurationSection(baseNode).getKeys(false);
    for(String tierName : tiers) {
      String tierBase = baseNode + "." + tierName;

      //Normal TNETier variables
      String type = configuration.getString(tierBase + ".Info.Type", "Major");
      String single = configuration.getString(tierBase + ".Info.Single", "Dollar");
      String plural = configuration.getString(tierBase + ".Info.Plural", "Dollars");
      Integer weight = configuration.getInt(tierBase + ".Options.Weight", 1);
      ItemTier item = null;

      if(currency.isItem()) {
        //ItemTier variables
        String material = configuration.getString(tierBase + ".Options.Material", "PAPER");
        short damage = (short) configuration.getInt(tierBase + ".Options.Damage", 0);
        String customName = configuration.getString(tierBase + ".Options.Name", null);
        String lore = configuration.getString(tierBase + ".Options.Lore", null);

        item = new ItemTier(material, damage);
        item.setName(customName);
        item.setLore(lore);

        if(configuration.contains(tierBase + ".Options.Enchantments")) {
          //System.out.println("Setting enchantments list: " + configuration.getStringList(tierBase + ".Options.Enchantments").toString());
          item.setEnchantments(configuration.getStringList(tierBase + ".Options.Enchantments"));
        }
      }

      TNETier tier = new TNETier();
      tier.setMajor(type.equalsIgnoreCase("major"));
      tier.setItemInfo(item);
      tier.setSingle(single);
      tier.setPlural(plural);
      tier.setWeight(weight);

      TNECurrencyTierLoadedEvent event = new TNECurrencyTierLoadedEvent(world, currency.name(), tier.singular(), type);
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
    if(world.equalsIgnoreCase(TNE.instance().defaultWorld)) {
      globalCurrencies.put(currency.name(), currency);
    } else {
      WorldManager manager = TNE.instance().getWorldManager(world);
      if (manager != null) {
        TNE.debug("[Add]Adding Currency: " + currency.name() + " for world: " + world);
        manager.addCurrency(currency);
      }
      TNE.instance().addWorldManager(manager);
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
    for(TNECurrency currency : TNE.instance().getWorldManager(world).getCurrencies()) {
      TNE.debug("Currency: " + currency.name() + " World: " + world + " Default? " + currency.isDefault());
      if(currency.isDefault()) {
        TNE.debug("Returning default Currency of " + currency.name() + " for world " + world);
        return currency;
      }
    }
    return null;
  }

  public TNECurrency get(String world, String name) {
    //System.out.println("Currency: " + name);
    //System.out.println("World: " + world);
    //System.out.println("WorldManager null for " + world +"? " + (TNE.instance().getWorldManager(world) == null));
    if(TNE.instance().getWorldManager(world).containsCurrency(name)) {
      //System.out.println("Returning Currency " + name + " for world " + world);
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
      account.getWorldHoldings(world).remove(currency);
      TNE.manager().addAccount(account);
    });
  }

  public void register(net.tnemc.core.economy.currency.Currency currency) {
    addCurrency(TNE.instance().defaultWorld, TNECurrency.fromReserve(currency));
  }

  public ItemStack createNote(String currency, String world, BigDecimal amount) {
    ItemStack stack = new ItemStack(Material.PAPER, 1);

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