package net.tnemc.vaults.model;

import com.github.tnerevival.core.db.SQLDatabase;
import net.tnemc.core.TNE;
import net.tnemc.core.item.JSONHelper;
import net.tnemc.core.item.SerialItem;
import net.tnemc.vaults.vault.Vault;
import net.tnemc.vaults.vault.VaultTab;
import net.tnemc.vaults.vault.member.VaultMember;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class VaultData {
  public static final String prefix = TNE.saveManager().getTNEManager().getPrefix();

  public static final String VAULT_LOAD = "SELECT vault_created, vault_size, vault_tabs, vault_items, vault_members FROM " + prefix + "_VAULTS WHERE vault_owner = ? AND vault_world = ?";
  public static final String VAULT_SAVE = "INSERT INTO " + prefix + "_VAULTS (vault_owner, vault_world, vault_created, vault_size, vault_tabs, vault_items, vault_members) VALUES(?, ?, ?, ?, ?, ?, ?) " +
      "ON DUPLICATE KEY UPDATE vault_size = ?, vault_tabs = ?, vault_items = ?, vault_members = ?";


  public static void saveVault(Vault vault) {
    SQLDatabase.executePreparedUpdate(VAULT_SAVE, new Object[] {
        vault.getOwner().toString(),
        vault.getWorld(),
        vault.getCreated(),
        vault.getSize(),
        vault.getMaxTabs(),
        serializeTabs(vault),
        serializeMembers(vault),
        vault.getSize(),
        vault.getMaxTabs(),
        serializeTabs(vault),
        serializeMembers(vault)
    });
  }

  public static Vault getVault(UUID owner, String world) {
    Vault vault = new Vault(owner, world);

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(VAULT_LOAD);
        ResultSet results = SQLDatabase.executePreparedQuery(statement, new Object[] {
            owner.toString(), world
        })) {
      if(results.next()) {
        vault.setCreated(results.getLong("vault_created"));
        vault.setSize(results.getInt("vault_size"));
        vault.setMaxTabs(results.getInt("vault_tabs"));
        vault.setTabs(unSerializeTabs(results.getString("vault_items")));
        vault = unSerializeMembers(vault, results.getString("vault_members"));

      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return vault;
  }

  public static boolean hasVault(UUID owner, String world) {
    boolean exists = false;

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(VAULT_LOAD);
        ResultSet results = SQLDatabase.executePreparedQuery(statement, new Object[] {
            owner.toString(), world
        })) {
      if(results.next()) {
        exists = true;
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return exists;
  }

  public static String serializeTabs(Vault vault) {
    JSONObject json = new JSONObject();

    for(VaultTab tab : vault.getTabs().values()) {
      JSONObject tabObject = new JSONObject();

      tabObject.put("owner", tab.getOwner().toString());
      tabObject.put("world", tab.getWorld());
      tabObject.put("icon", tab.getIcon().serialize());
      tabObject.put("location", tab.getLocation());

      JSONObject items = new JSONObject();
      for(Map.Entry<Integer, SerialItem> item : tab.getItems().entrySet()) {
        items.put(item.getKey(), item.getValue().serialize());
      }
      tabObject.put("items", items);

      json.put(tab.getLocation(), tabObject);
    }

    return json.toJSONString();
  }

  public static Map<Integer, VaultTab> unSerializeTabs(String tabs) {

    Map<Integer, VaultTab> tabsMap = new HashMap<>();

    try {
      JSONObject object = (JSONObject)(new JSONParser()).parse(tabs);
      object.forEach((key, value)->{
        JSONHelper helper = new JSONHelper((JSONObject)value);

        UUID owner = UUID.fromString(helper.getString("owner"));
        String world = helper.getString("world");
        SerialItem icon = null;
        try {
          icon = SerialItem.fromJSON((JSONObject)(new JSONParser()).parse(helper.getString("icon")));
        } catch (ParseException ignore) {
        }
        int location = helper.getInteger("location");
        VaultTab tab = new VaultTab(owner, world, icon, location);

        if(helper.has("items")) {
          JSONObject items = helper.getJSON("items");
          Map<Integer, SerialItem> itemsMap = new HashMap<>();
          items.forEach((slot, item)->{
            try {
              System.out.println("Slot: " + Integer.valueOf(String.valueOf(slot)));
              System.out.println("Item: " + String.valueOf(item));
              itemsMap.put(Integer.valueOf(String.valueOf(slot)), SerialItem.unserialize(String.valueOf(item)));
            } catch (ParseException ignore) {
            }
          });
          tab.setItems(itemsMap);
        }

        tabsMap.put(location, new VaultTab(owner, world, icon, location));
      });
    } catch (ParseException ignore) {
    }

    return tabsMap;
  }

  public static String serializeMembers(Vault vault) {
    JSONObject json = new JSONObject();

    for(VaultMember member : vault.getMembers().values()) {
      JSONObject memberObject = new JSONObject();
      memberObject.put("id", member.getId().toString());
      memberObject.put("vault", member.getVault().toString());
      memberObject.put("world", member.getWorld());
      memberObject.put("permissions", String.join(",", member.getPermissions()));
      json.put(member.getId().toString(), memberObject);
    }
    return json.toJSONString();
  }

  public static Vault unSerializeMembers(Vault vault, String members) {

    try {
      JSONObject object = (JSONObject) (new JSONParser()).parse(members);
      object.forEach((key, value)-> {
        JSONHelper helper = new JSONHelper((JSONObject) value);

        UUID id = UUID.fromString(helper.getString("id"));
        UUID vaultID = UUID.fromString(helper.getString("vault"));
        String world = helper.getString("world");

        VaultMember member = new VaultMember(id, vaultID, world);
        member.setPermissions(Arrays.asList(helper.getString("permissions").split("\\,")));
        vault.getMembers().put(id, member);
      });
    } catch(Exception ignore) {

    }


    return vault;
  }
}