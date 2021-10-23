package net.tnemc.core.common.currency.loader;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.CurrencyManager;
import net.tnemc.core.common.currency.CurrencyLoader;
import net.tnemc.core.common.currency.CurrencyNote;
import net.tnemc.core.common.currency.ItemTier;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import net.tnemc.core.common.currency.recipe.CurrencyLegacyShapedRecipe;
import net.tnemc.core.common.currency.recipe.CurrencyRecipe;
import net.tnemc.core.common.currency.recipe.CurrencyShapedRecipe;
import net.tnemc.core.common.currency.recipe.CurrencyShapelessRecipe;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.event.currency.TNECurrencyCraftingRecipeEvent;
import net.tnemc.core.event.currency.TNECurrencyLoadEvent;
import net.tnemc.core.event.currency.TNECurrencyTierLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.tnemc.core.common.CurrencyManager.largestSupported;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 9/22/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AdvancedCurrencyLoader implements CurrencyLoader {


  @Override
  public void loadCurrencies() {
    final File directory = new File(TNE.instance().getDataFolder(), "currencies");

    if(directory.exists()) {
      final File[] currencies = MISCUtils.getYAMLs(directory);

      for(File curFile : currencies) {
        CommentedConfiguration config = new CommentedConfiguration(curFile, (File) null);
        config.load(false);
        
        if (config.contains("Options.Disabled") &&
            config.getBool("Options.Disabled")) {
          return;
        }

        TNE.debug("[Loop]Loading Currency: " + curFile.getName() + " for world: " + TNE.instance().defaultWorld);
        final String identifier = config.getString("Info.Identifier");


        //OP
        Permission give = new Permission("tne.money.give." + identifier, "Grants access to /money give for currency: " + identifier, PermissionDefault.OP);
        give.addParent(CurrencyManager.giveParent, true);
        Bukkit.getServer().getPluginManager().addPermission(give);

        Permission set = new Permission("tne.money.set." + identifier, "Grants access to /money set for currency: " + identifier, PermissionDefault.OP);
        set.addParent(CurrencyManager.setParent, true);
        Bukkit.getServer().getPluginManager().addPermission(set);

        Permission take = new Permission("tne.money.take." + identifier, "Grants access to /money take for currency: " + identifier, PermissionDefault.OP);
        take.addParent(CurrencyManager.takeParent, true);
        Bukkit.getServer().getPluginManager().addPermission(take);

        //Non-op
        Permission convert = new Permission("tne.money.convert." + identifier, "Grants access to /money convert for currency: " + identifier, PermissionDefault.TRUE);
        convert.addParent(CurrencyManager.convertParent, true);
        Bukkit.getServer().getPluginManager().addPermission(convert);

        Permission note = new Permission("tne.money.note." + identifier, "Grants access to /money note for currency: " + identifier, PermissionDefault.TRUE);
        note.addParent(CurrencyManager.noteParent, true);
        Bukkit.getServer().getPluginManager().addPermission(note);

        Permission pay = new Permission("tne.money.pay." + identifier, "Grants access to /money pay for currency: " + identifier, PermissionDefault.TRUE);
        pay.addParent(CurrencyManager.payParent, true);
        Bukkit.getServer().getPluginManager().addPermission(pay);

        //Currency Info configs.
        final String single = config.getString("Info.Major_Single", "Dollar");
        final String plural = config.getString("Info.Major_Plural", "Dollars");
        final String singleMinor = config.getString("Info.Minor_Single", "Cent");
        final String pluralMinor = config.getString("Info.Minor_Plural", "Cents");
        final String prefixes = config.getString("Info.Prefixes", "kMGTPEZYXWVUN₮").trim();
        final String symbol = config.getString("Info.Symbol", "$");

        //Currency Options configs.
        final Boolean worldDefault = config.getBool("Options.Default", true);
        List<String> worlds = config.getStringList("Options.Worlds");
        final Boolean global = config.getBool("Options.Global", true);
        final String format = config.getString("Options.Format", "<symbol><major.amount><decimal><minor.amount>").trim();
        final BigDecimal maxBalance = ((new BigDecimal(config.getString("Options.MaxBalance", largestSupported.toPlainString())).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(config.getString("MaxBalance", largestSupported.toPlainString())));
        final BigDecimal balance = new BigDecimal(config.getString("Options.Balance", "200.00"));
        final String decimal = config.getString("Options.Decimal", ".");
        final Integer decimalPlaces = ((config.getInt("Options.DecimalPlaces", 2) > 4)? 4 : config.getInt("Options.DecimalPlaces", 2));
        final String currencyType = config.getString("Options.Type", "virtual");
        final Boolean ender = config.getBool("Options.EnderChest", true);
        final Boolean separate = config.getBool("Options.Major_Separate", true);
        final String separator = config.getString("Options.Major_Separator", ",");
        final Integer minorWeight = config.getInt("Options.Minor_Weight", 100);

        //Currency Note configs
        final Boolean notable = config.getBool("Note.Notable", true);
        final BigDecimal fee = new BigDecimal(config.getString("Note.Fee", "0.00"));
        final BigDecimal minimum = new BigDecimal(config.getString("Note.Minimum", "0.00"));

        //Note Item configs
        final String material = config.getString("Note.Item.Material", "PAPER");

        CurrencyNote currencyNote = new CurrencyNote(material);
        currencyNote.setTexture(config.getString("Note.Item.Texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA0NzE5YjNiOTdkMTk1YTIwNTcxOGI2ZWUyMWY1Yzk1Y2FmYTE2N2U3YWJjYTg4YTIxMDNkNTJiMzdkNzIyIn19fQ=="));
        currencyNote.setCustomModelData(config.getInt("Note.Item.ModelData", -1));

        if(config.contains("Note.Item.Enchantments")) {
          currencyNote.setEnchantments(config.getStringList("Note.Item.Enchantments"));
        }

        if(config.contains("Note.Item.Flags")) {
          currencyNote.setFlags(config.getStringList("Note.Item.Flags"));
        }



        //Currency Conversion configs.
        final Double rate = config.getDouble("Conversion.Rate", 1.0);

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

        if(config.contains("Converting")) {
          Set<String> converting = config.getSection("Converting").getKeys(false);

          for(String str : converting) {
            currency.addConversion(str, config.getDouble("Converting." + str, 1.0));
          }
        }

        if(loadTiers(currency)) {

          TNECurrencyLoadEvent event = new TNECurrencyLoadEvent(TNE.instance().defaultWorld, currency.name(), !Bukkit.getServer().isPrimaryThread());
          Bukkit.getServer().getPluginManager().callEvent(event);
          if (!event.isCancelled()) {
            TNE.manager().currencyManager().addCurrency(TNE.instance().defaultWorld, currency);
          }
        }
      }
      return;
    }
    TNE.logger().warning("Unable to locate the currencies directory. Please validate that it exists.");
  }

  @Override
  public boolean loadTiers(TNECurrency currency) {
    final File directory = new File(TNE.instance().getDataFolder(), "currencies/" + currency.getIdentifier());

    if(directory.exists()) {
      final File[] tiers = MISCUtils.getYAMLs(directory);

      if(tiers.length > 0) {
        for (File tierFile : tiers) {
          CommentedConfiguration config = new CommentedConfiguration(tierFile, (File) null);
          config.load(false);

          //Normal TNETier variables
          String type = config.getString("Info.Type", "Major");
          String single = config.getString("Info.Single", "Dollar");
          String plural = config.getString("Info.Plural", "Dollars");
          BigInteger weight = new BigInteger(config.getString("Options.Weight", "1"));
          ItemTier item = null;

          if(currency.isItem()) {
            //ItemTier variables
            String material = config.getString("Options.Material", "PAPER");
            short damage = (short) config.getInt("Options.Damage", 0);
            String customName = config.getString("Options.Name", null);
            String lore = config.getString("Options.Lore", null);
            int customModel = config.getInt("Options.ModelData", -1);

            item = new ItemTier(material, damage);
            item.setName(customName);
            item.setLore(lore);

            if(customModel > 0) {
              item.setCustomModel(customModel);
            }

            if(config.contains("Options.Enchantments")) {
              //TNE.debug("Setting enchantments list: " + config.getStringList("Options.Enchantments").toString());
              item.setEnchantments(config.getStringList("Options.Enchantments"));
            }

            if(config.contains("Options.Flags")) {
              item.setFlags(config.getStringList("Options.Flags"));
            }
          }

          if(currency.isItem() && config.contains("Options.Crafting")) {
            if(config.getBool("Options.Crafting.Enabled", false)) {
              final boolean shapeless = config.getBool("Options.Crafting.Shapeless", false);
              ItemStack stack = item.toStack().clone();
              stack.setAmount(config.getInt("Options.Crafting.Amount", 1));

              CurrencyRecipe recipe = null;

              if(shapeless) {
                recipe = new CurrencyShapelessRecipe(currency.getIdentifier(), single, stack);
              } else {
                if(MISCUtils.isOneThirteen()) {
                  recipe = new CurrencyShapedRecipe(currency.getIdentifier(), single, stack);
                } else {
                  recipe = new CurrencyLegacyShapedRecipe(currency.getIdentifier(), single, stack);
                }
              }

              recipe.setCraftingMatrix(config.getStringList("Options.Crafting.Recipe"));
              recipe.setMaterialsRaw(config.getStringList("Options.Crafting.Materials"));

              TNECurrencyCraftingRecipeEvent event = new TNECurrencyCraftingRecipeEvent(TNE.instance().defaultWorld, currency.getIdentifier(),
                  single, type, recipe, !Bukkit.isPrimaryThread());

              Bukkit.getPluginManager().callEvent(event);

              TNE.manager().currencyManager().currencyRecipes.put(currency.getIdentifier() + ":" + single, event.getRecipe());
            }
          }

          TNETier tier = new TNETier();
          tier.setMajor(type.equalsIgnoreCase("major"));
          tier.setItemInfo(item);
          tier.setSingle(single);
          tier.setPlural(plural);
          tier.setWeight(weight);

          TNECurrencyTierLoadedEvent event = new TNECurrencyTierLoadedEvent(TNE.instance().defaultWorld, currency.name(), tier.singular(), type, !Bukkit.getServer().isPrimaryThread());
          Bukkit.getServer().getPluginManager().callEvent(event);

          if(!event.isCancelled()) {
            if (type.equalsIgnoreCase("minor")) {
              currency.addTNEMinorTier(tier);
              continue;
            }
            currency.addTNEMajorTier(tier);
          }
        }
        return true;
      }
      TNE.logger().warning("No tiers found for currency: " + currency.getIdentifier());
      return false;
    }
    TNE.logger().warning("Directory currencies/" + currency.getIdentifier() + " does not exist.");
    return false;
  }
}