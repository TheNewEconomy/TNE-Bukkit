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
import com.github.tnerevival.core.conversion.Converter;
import com.github.tnerevival.core.exception.InvalidDatabaseImport;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by creatorfromhell on 11/13/2016.
 **/
public class CraftConomy extends Converter {
  private File configFile = new File(TNE.instance.getDataFolder() + "../Craftconomy3", "config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private String prefix = config.getString("System.Database.Prefix");

  @Override
  public String name() {
    return "CraftConomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    super.mysql();
  }

  @Override
  public void h2() throws InvalidDatabaseImport {
    super.h2();
  }
}