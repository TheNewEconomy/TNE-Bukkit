package net.tnemc.core.common.data;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.SaveManager;
import com.github.tnerevival.core.db.SQLDatabase;
import net.tnemc.core.TNE;
import net.tnemc.dbupdater.core.TableManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 8/29/2017.
 */
public class TNESaveManager extends SaveManager {

  private Map<String, List<String>> dataTables = new HashMap<>();

  public TNESaveManager(DataManager manager) {
    super(manager);
    manager.setSsl(TNE.configurations().getBoolean("Core.Database.MySQL.SSL"));
  }

  @Override
  public void initialize() throws SQLException {
    manager.getDb().connector().initialize(manager);
    TNE.debug("Format: " + manager.getFormat());
    TNE.debug("Manager providers size: " + manager.getProviders().size());
    TNE.debug("Manager providers size: " + manager.getProviders().keySet().toString());
    TNE.debug("Manager Null: " + (manager == null));
    TNE.debug("Manager.getDB Null: " + (manager.getDb() == null));
    TNE.debug("First Run: " + manager.getDb().first());

    SQLDatabase.open();

    TNE.logger().info("Updating tables, please hold.");
    TableManager tableManager = new TableManager(manager.getFormat().toLowerCase(), manager.getPrefix());
    tableManager.generateQueriesAndRun(SQLDatabase.getDb().getConnection(), TNE.instance().getResource("tne_tables.yml"));
    TNE.logger().info("Finished updating tables.");

    SQLDatabase.close();
    load();
  }

  @Override
  public void load() throws SQLException {
    TNE.debug("====== TNESaveManager.load =======");
    TNELib.instance().getLogger().info("Finished loading data!");
  }

  @Override
  public void save() throws SQLException {
    getTNEManager().getTNEProvider().save(TNELib.instance().currentSaveVersion);
    TNE.debug("Null Account Count: " + getTNEManager().getTNEProvider().nullAccounts());
    TNE.debug("Finished saving data!");
  }

  public void registerTables(String type, List<String> tables) {
    if(dataTables.containsKey(type)) tables.addAll(dataTables.get(type));
    dataTables.put(type, tables);
  }

  public List<String> getTables(String type) {
    if(dataTables.containsKey(type)) {
      return dataTables.get(type);
    }
    return new ArrayList<>();
  }

  public TNEDataManager getTNEManager() {
    return (TNEDataManager)getDataManager();
  }
}