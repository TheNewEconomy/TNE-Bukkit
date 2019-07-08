package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;


/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BOSEconomy extends Converter {

  @Override
  public String name() {
    return "BOSEconomy";
  }

  @Override
  public String type() {
    return "flatfile";
  }

  @Override
  public void flatfile() throws InvalidDatabaseImport {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File(TNE.instance().getDataFolder(), "../BOSEconomy/accounts.txt")));

      String line;
      String id = "";
      String lowerID = "";
      boolean inBlock = false;
      boolean lower = false;
      boolean bank = false;
      double money = 0.0;
      String acc = "";
      while((line = reader.readLine()) != null) {
        if(line.contains("}")) {
          if(lower) {
            lower = false;
            lowerID = "";
          } else {
            String eco = (acc.trim().equalsIgnoreCase(""))? id : acc;
            ConversionModule.convertedAdd(eco, TNE.instance().defaultWorld, TNE.manager().currencyManager().get(TNE.instance().defaultWorld).name(), new BigDecimal(money));
            inBlock = false;
            bank = false;
            money = 0.0;
          }
        }

        if(line.contains("{")) {
          if(!inBlock) {
            inBlock = true;
          } else {
            lower = true;
          }
          String prefix = line.split(" ")[0].trim();
          if(!lower) {
            id = prefix;
          } else {
            lowerID = prefix;
          }
        }

        if(line.contains("type")) {
          bank = (line.contains("bank"));
        }
        if(line.contains("money")) {
          money = Double.parseDouble(line.split(" ")[1].trim());
        }

        if(bank && lowerID.equalsIgnoreCase("owners") && acc.trim().equalsIgnoreCase("") && !line.contains("owners")) {
          acc = line.trim();
        }
      }
    } catch(Exception e) {
      System.out.println("Unable to read BOSEconomy Data.");
      TNE.debug(e);
    }
  }
}