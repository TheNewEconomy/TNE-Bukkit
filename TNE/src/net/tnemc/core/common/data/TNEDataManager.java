package net.tnemc.core.common.data;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DataProvider;
import net.tnemc.core.TNE;

import java.util.HashMap;
import java.util.Map;

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
 * Created by Daniel on 8/29/2017.
 */
public class TNEDataManager extends DataManager {

  public TNEDataManager(String format, String host, Integer port, String database, String user, String password,
                        String prefix, String file, boolean directSave, boolean cacheData, Integer update,
                        boolean compress) {
    super(format, host, port, database, user, password, prefix, file, directSave, cacheData, update, compress);
  }

  public void loadProviders() {
    TNE.debug("Loading providers");

    TNE.loader().getModules().forEach((key, value)->{
      TNE.debug("Looping through modules");
      value.getModule().registerProviders().forEach((identifier, provider)->{
        TNE.debug("Loading provider: " + provider.getName());
        registerProvider(provider);
      });
    });
  }

  public Map<String, TNEDataProvider> getTNEProviders() {
    Map<String, TNEDataProvider> converted = new HashMap<>();

    for(DataProvider provider : providers.values()) {
      if(provider instanceof TNEDataProvider) converted.put(provider.identifier(), (TNEDataProvider)provider);
    }
    return converted;
  }

  @Override
  public boolean registerProvider(Class<? extends DataProvider> provider) {
    TNE.debug("TNEDataManager.registerProvider");
    TNE.debug("super.registerProvider");
    return super.registerProvider(provider);
  }

  public TNEDataProvider getTNEProvider() {
    return (TNEDataProvider)providers.get(format);
  }

  public void setFormat(String format) {
    this.format = format;
  }
}