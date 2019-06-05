package net.tnemc.core.common.data;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DataProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.data.impl.H2Provider;
import net.tnemc.core.common.data.impl.MySQLProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 8/29/2017.
 */
public class TNEDataManager extends DataManager {

  public TNEDataManager(String format, String host, Integer port, String database, String user, String password,
                        String prefix, String file, boolean directSave, boolean cacheData, Integer update,
                        boolean compress) {
    super(format, host, port, database, user, password, prefix, file, directSave, cacheData, update, compress);
  }

  public void loadProviders() {
    TNE.debug("Loading core providers");


    TNE.debug("Loading providers");

    TNE.loader().getModules().forEach((key, value)->{
      TNE.debug("Looping through modules");
      value.getModule().registerProviders().forEach((identifier, provider)->{
        TNE.debug("Loading provider: " + provider.getName());
        registerProvider(provider);
      });
    });

    registerProvider(H2Provider.class);
    registerProvider(MySQLProvider.class);
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