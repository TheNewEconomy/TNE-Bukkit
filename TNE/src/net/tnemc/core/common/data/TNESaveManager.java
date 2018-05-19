package net.tnemc.core.common.data;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.SaveManager;
import net.tnemc.core.TNE;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 8/29/2017.
 */
public class TNESaveManager extends SaveManager {

  public TNESaveManager(DataManager manager) {
    super(manager);
  }

  @Override
  public void initialize() {
    TNE.debug("Format: " + manager.getFormat());
    TNE.debug("Manager providers size: " + manager.getProviders().size());
    TNE.debug("Manager providers size: " + manager.getProviders().keySet().toString());
    TNE.debug("Manager Null: " + (manager == null));
    TNE.debug("Manager.getDB Null: " + (manager.getDb() == null));
    TNE.debug("First Run: " + manager.getDb().first());
    if(manager.getDb().first()) {
      manager.getDb().initialize();
      return;
    }
    TNE.debug("Manager Version Null: " + (manager.getDb().version() == null));
    saveVersion = manager.getDb().version();
    TNE.instance().getLogger().info("Save file of version: " + saveVersion + " detected.");
    load();
  }

  @Override
  public void load() {
    TNE.debug("====== TNESaveManager.load =======");
    if(saveVersion < TNELib.instance().currentSaveVersion && saveVersion != 0) {
      getTNEManager().getTNEProvider().update(saveVersion);
      TNELib.instance().getLogger().info("Saved data has been updated!");
    }
    Double version = (saveVersion != 0.0) ? saveVersion : TNELib.instance().currentSaveVersion;
    TNE.debug("Current Save Version: " + version);
    getTNEManager().getTNEProvider().load(version);
    TNELib.instance().getLogger().info("Finished loading data!");
  }

  @Override
  public void save() {
    getTNEManager().getTNEProvider().save(TNELib.instance().currentSaveVersion);
    TNELib.instance().getLogger().info("Null Account Count: " + getTNEManager().getTNEProvider().nullAccounts());
    TNELib.instance().getLogger().info("Finished saving data!");
  }

  public TNEDataManager getTNEManager() {
    return (TNEDataManager)getDataManager();
  }
}