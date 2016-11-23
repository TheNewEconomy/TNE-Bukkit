/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.core.conversion.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.conversion.Converter;
import com.github.tnerevival.core.exception.InvalidDatabaseImport;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by creatorfromhell on 11/15/2016.
 **/
public class BOSEconomy extends Converter {

  @Override
  public String name() {
    return "BOSEconomy";
  }

  @Override
  public void flatfile() throws InvalidDatabaseImport {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File(TNE.instance.getDataFolder(), "../BOSEconomy/accounts.txt")));

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
            AccountUtils.transaction(null,
                                     IDFinder.getID(eco).toString(),
                                     money,
                                     TNE.instance.manager.currencyManager.get(
                                         TNE.instance.defaultWorld
                                     ),
                                     TransactionType.MONEY_GIVE,
                                     TNE.instance.defaultWorld
            );
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
      MISCUtils.debug(e);
    }
  }
}