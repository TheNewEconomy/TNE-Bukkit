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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
 * Created by creatorfromhell on 10/22/2016.
 */
public class CurrencyManager {
  private static BigDecimal largestSupported;
  private Map<String, TNECurrency> globalCurrencies = new HashMap<>();
  private Set<String> worlds = TNE.instance().worldConfiguration().getConfigurationSection("Worlds").getKeys(false);

  //Cache-related maps.
  private List<String> globalDisabled = new ArrayList<>();

  public CurrencyManager() {
    loadCurrencies();
  }

  public void loadCurrencies() {
    largestSupported = new BigDecimal("900000000000000000000000000000000000");

    loadCurrency(TNE.instance().getConfig(), false, TNE.instance().defaultWorld);
    for(WorldManager manager : TNE.instance().getWorldManagers()) {
      initializeWorld(manager.getWorld());
    }
    largestSupported = null;
  }

  private void loadCurrency(FileConfiguration configuration, boolean world, String worldName) {
    String curBase = ((world)? "Worlds." + worldName : "Core") + ".Currency";
    if(configuration.contains(curBase)) {

      Set<String> currencies = configuration.getConfigurationSection(curBase).getKeys(false);
      TNE.debug(currencies.toArray().toString());

      for(String cur : currencies) {
        if (configuration.contains("Core.Currency." + cur + ".Disabled") &&
            configuration.getBoolean("Core.Currency." + cur + ".Disabled")) {
              return;
        }

        TNE.debug("[Loop]Loading Currency: " + cur + " for world: " + worldName);
        String base = curBase + "." + cur;
        String single = configuration.getString(base + ".Name.Major.Single", "Dollar");
        String plural = configuration.getString(base + ".Name.Major.Plural", "Dollars");
        String singleMinor = configuration.getString(base + ".Name.Minor.Single", "Cent");
        String pluralMinor = configuration.getString(base + ".Name.Minor.Plural", "Cents");
        BigDecimal balance = new BigDecimal(configuration.getString(base + ".Balance", "200.00"));
        String decimal = configuration.getString(base + ".Decimal", ".");
        Integer decimalPlaces = ((configuration.getInt(base + ".DecimalPlace", 2) > 4)? 4 : configuration.getInt(base + ".DecimalPlace", 2));
        BigDecimal maxBalance = ((new BigDecimal(configuration.getString(base + ".MaxBalance", largestSupported.toPlainString())).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(configuration.getString(base + ".MaxBalance", largestSupported.toPlainString())));
        String format = configuration.getString(base + ".Format", "<symbol><major.amount><decimal><minor.amount>").trim();
        String prefixes = configuration.getString(base + ".Prefixes", "kMGTPEZYXWV").trim();
        Boolean worldDefault = configuration.getBoolean(base + ".Default", true);
        Double rate = configuration.getDouble(base + ".Conversion", 1.0);
        Boolean item = configuration.getBoolean(base + ".ItemCurrency");
        Boolean vault = configuration.getBoolean(base + ".Vault", true);
        Boolean notable = configuration.getBoolean(base + ".Notable", false);
        Boolean bankChest = configuration.getBoolean(base + ".BankChest", true);
        Boolean ender = configuration.getBoolean(base + ".EnderChest", true);
        Boolean separate = configuration.getBoolean(base + ".Major.Separate", true);
        String separator = configuration.getString(base + ".Major.Separator", ",");
        String symbol = configuration.getString(base + ".Symbol", "$");
        Integer minorWeight = configuration.getInt(base + ".Minor.Weight", 100);

        //Interest-related configurations
        Boolean interestEnabled = configuration.getBoolean(base + ".Interest.Enabled", false);
        Double interestRate = configuration.getDouble(base + ".Interest.Rate", 0.2);
        Long interestInterval = configuration.getLong(base + ".Interest.Interval", 1800) * 1000;

        TNECurrency currency = new TNECurrency();
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
        currency.setSymbol(symbol);
        currency.setWorldDefault(worldDefault);
        currency.setRate(rate);
        currency.setItem(item);
        currency.setVault(vault);
        currency.setNotable(notable);
        currency.setBankChest(bankChest);
        currency.setEnderChest(ender);
        currency.setSeparateMajor(separate);
        currency.setMajorSeparator(separator);
        currency.setMinorWeight(minorWeight);

        //Interest-related configurations
        currency.setInterestEnabled(interestEnabled);
        currency.setInterestRate(interestRate);
        currency.setInterestInterval(interestInterval);

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
      String single = configuration.getString(tierBase + ".Name.Single", "Dollar");
      String plural = configuration.getString(tierBase + ".Name.Plural", "Dollars");
      String type = configuration.getString(tierBase + ".Type", "Major");
      Integer weight = configuration.getInt(tierBase + ".Weight", 1);
      ItemTier item = null;

      if(currency.isItem()) {
        //ItemTier variables
        String material = configuration.getString(tierBase + ".Item.Material", "PAPER");
        short damage = (short) configuration.getInt(tierBase + ".Item.Damage", 0);
        String customName = configuration.getString(tierBase + ".Item.Name", null);
        String lore = configuration.getString(tierBase + ".Item.Lore", null);

        item = new ItemTier(material, damage);
        item.setName(customName);
        item.setLore(lore);

        if(configuration.isConfigurationSection(tierBase + ".Item.Enchantments")) {
          Set<String> enchants = configuration.getConfigurationSection(tierBase + ".Item.Enchantments").getKeys(false);
          for (String enchant : enchants) {
            Enchantment parsed = Enchantment.getByName(enchant);
            if (parsed != null) {
              item.addEnchantment(parsed.getName(), configuration.getString(tierBase + ".Item.Enchantments." + enchant, "*"));
            }
          }
        }
      }

      TNETier tier = new TNETier();
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
    TNE.debug("[Add]Loading Currency: " + currency.name() + " for world: " + world);
    if(world.equalsIgnoreCase(TNE.instance().defaultWorld)) {
      globalCurrencies.put(world, currency);
    } else {
      if(TNE.instance().getWorldManager(world) != null) {
        TNE.debug("[Add]Adding Currency: " + currency.name() + " for world: " + world);
        TNE.instance().getWorldManager(world).addCurrency(currency);
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
    if(!TNE.instance().getWorldManagers().contains(world)) {
      WorldManager manager = new WorldManager(world);
      TNE.instance().addWorldManager(manager);
    }
    loadCurrency(TNE.instance().worldConfiguration(), true, world);
    for(TNECurrency currency : globalCurrencies.values()) {
      if(!globalDisabled.contains(currency.name())) {
        if(TNE.instance().getWorldManager(world) == null) {
          TNE.debug("World Manager for world: " + world + " is null. Skipping.");
          break;
        }
        TNE.instance().getWorldManager(world).addCurrency(currency);
      }
    }
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
    if(TNE.instance().getWorldManager(world).containsCurrency(name)) {
      TNE.debug("Returning Currency " + name + " for world " + world);
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
    BigDecimal difference = amount.multiply(new BigDecimal(rate));

    return amount.add(difference);
  }

  public boolean contains(String world) {
    return TNE.instance().getWorldManager(world) != null;
  }

  public Collection<TNECurrency> getWorldCurrencies(String world) {
    TNE.debug("=====START CurrencyManager =====");
    TNE.debug("World: " + world);
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
      account.setHoldings(world, newName, account.getHoldings(world, currency));
      account.getWorldHoldings(world).remove(currency);
      TNE.manager().addAccount(account);
    });
  }

  public void register(net.tnemc.core.economy.currency.Currency currency) {
    addCurrency(TNE.instance().defaultWorld, TNECurrency.fromReserve(currency));
  }

  public ItemStack createNote(UUID id, String currency, String world, BigDecimal amount) {
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

    TNETransaction transaction = new TNETransaction("", id.toString(), world, TNE.transactionManager().getType("noteclaim"));
    transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world, currency),
        value, TransactionChargeType.GAIN)
    );
    TNE.debug("RecipientCharge: " + transaction.recipientCharge().getAmount().toPlainString());
    TNE.debug("=====END CurrencyManager.claimNote =====");
    return Optional.of(transaction);
  }

  public Optional<TNECurrency> currencyFromItem(String world, ItemStack stack) {
    for(TNECurrency currency : TNE.instance().getWorldManager(world).getCurrencies()) {
      if(currency.isItem()) {
        if(isMajorItem(world, currency.name(), stack) ||
            isMinorItem(world, currency.name(), stack)) {
          return Optional.of(currency);
        }
      }
    }
    return Optional.empty();
  }

  public boolean isMajorItem(String world, String currency, ItemStack stack) {
    for(Object tier : TNE.instance().getWorldManager(world).getCurrency(currency).getTNEMajorTiers().values()) {
      if(tier instanceof TNETier) {
        if (((TNETier)tier).getItemInfo().toStack().equals(stack)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isMinorItem(String world, String currency, ItemStack stack) {
    for(Object tier : TNE.instance().getWorldManager(world).getCurrency(currency).getTNEMinorTiers().values()) {
      if(tier instanceof TNETier) {
        if (((TNETier)tier).getItemInfo().toStack().equals(stack)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean contains(String world, String name) {
    TNE.debug("CurrencyManager.contains(" + world + ", " + name + ")");
    return TNE.instance().getWorldManager(world).containsCurrency(name);
  }
}