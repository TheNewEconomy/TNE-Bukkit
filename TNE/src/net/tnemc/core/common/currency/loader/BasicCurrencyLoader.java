package net.tnemc.core.common.currency.loader;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.CurrencyLoader;
import net.tnemc.core.common.currency.CurrencyNote;
import net.tnemc.core.common.currency.ItemTier;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;

import java.math.BigDecimal;
import java.math.BigInteger;
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
public class BasicCurrencyLoader implements CurrencyLoader {

  @Override
  public void loadCurrencies() {
    final String base = "Core.Currency.Basic";

    //Currency Info Configurations.
    final String identifier = TNE.instance().mainConfigurations().getString(base + ".Identifier", "Dollar");
    final String single = TNE.instance().mainConfigurations().getString(base + ".Major_Single", "Dollar");
    final String plural = TNE.instance().mainConfigurations().getString(base + ".Major_Plural", "Dollars");
    final String singleMinor = TNE.instance().mainConfigurations().getString(base + ".Minor_Single", "Cent");
    final String pluralMinor = TNE.instance().mainConfigurations().getString(base + ".Minor_Plural", "Cents");
    final String prefixes = TNE.instance().mainConfigurations().getString(base + ".Prefixes", "kMGTPEZYXWVUN₮").trim();
    final String symbol = TNE.instance().mainConfigurations().getString(base + ".Symbol", "$");
    TNE.debug("Basic Symbol: " + symbol);
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

    if(loadTiers(currency)) {
      TNE.manager().currencyManager().addCurrency(TNE.instance().defaultWorld, currency);
    }
  }

  @Override
  public boolean loadTiers(TNECurrency currency) {
    final String baseNode = "Core.Currency.Basic." + ((currency.isItem())? "Items" : "Virtual");
    Set<String> tiers = TNE.instance().mainConfigurations().getSection(baseNode).getKeys(false);

    for (String tierName : tiers) {

      //Normal TNETier variables
      String unparsedValue = TNE.instance().mainConfigurations().getString(baseNode + "." + tierName);

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
    return true;
  }
}
