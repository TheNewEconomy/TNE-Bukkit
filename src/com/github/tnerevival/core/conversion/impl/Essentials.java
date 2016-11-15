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
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by creatorfromhell on 11/15/2016.
 **/
public class Essentials extends Converter {
  private File dataDirectory = new File(TNE.instance.getDataFolder() + "../Essentials/userdata");

  @Override
  public String name() {
    return "Essentials";
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {
    if(!dataDirectory.isDirectory() || dataDirectory.listFiles() == null || dataDirectory.listFiles().length == 0) return;

    for(File accountFile : dataDirectory.listFiles()) {
      FileConfiguration acc = YamlConfiguration.loadConfiguration(accountFile);

      Double money = acc.contains("money")? acc.getDouble("money") : 0.0;
      String currency = TNE.instance.manager.currencyManager.get(TNE.instance.defaultWorld).getName();

      AccountUtils.setFunds(IDFinder.getID(acc.getString("lastAccountName")), TNE.instance.defaultWorld, money, currency);
    }
  }
}