package com.github.tnerevival.core.configurations.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.configurations.Configuration;
import com.github.tnerevival.core.objects.TNEAccessPackage;
import com.github.tnerevival.core.objects.TNECommandObject;
import com.github.tnerevival.core.objects.TNEInventoryObject;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;

import java.util.*;

public class ObjectConfiguration extends Configuration {

  private Map<String, TNECommandObject> commands = new HashMap<>();
  private Map<String, TNEInventoryObject> inventories = new HashMap<>();

  @Override
  public void load(FileConfiguration configurationFile) {
    Set<String> identifiers = TNE.instance.worldConfigurations.getConfigurationSection("Worlds").getKeys(false);

    //Load Objects
    loadCommands(configurationFile, "", null);
    loadInventories(configurationFile, "", null);
    for(String identifier : identifiers) {
      loadCommands(TNE.instance.worldConfigurations, "Worlds." + identifier + ".", identifier);
      loadInventories(TNE.instance.worldConfigurations, "Worlds." + identifier + ".", identifier);
    }

    identifiers = TNE.instance.playerConfigurations.getConfigurationSection("Players").getKeys(false);
    for(String identifier : identifiers) {
      loadCommands(TNE.instance.playerConfigurations, "Players." + identifier + ".", identifier);
      loadInventories(TNE.instance.playerConfigurations, "Players." + identifier + ".", identifier);
    }
    super.load(configurationFile);
  }

  private void loadCommands(FileConfiguration configuration, String baseNode, String identifier) {
    String base = baseNode + "Objects.Commands";

    if(configuration.contains(base)) {
      Boolean zero = !configuration.contains(base + ".ZeroMessage") || configuration.getBoolean(base + ".ZeroMessage");
      Boolean enabledMain = !configuration.contains(base + ".Enabled") || configuration.getBoolean(base + ".Enabled");
      configurations.put(base + ".ZeroMessage", zero);
      configurations.put(base + ".Enabled", enabledMain);

      Set<String> commandNames = configuration.getConfigurationSection(base).getKeys(false);

      for(String commandName : commandNames) {

        String id = (identifier != null)? identifier + ":" + commandName : commandName;
        TNECommandObject command = new TNECommandObject(commandName.toLowerCase(), configuration.getDouble(base + "." + commandName + ".Cost"));

        if(configuration.contains(base + "." + commandName + ".SubCommands")) {
          Set<String> subCommands = configuration.getConfigurationSection(base + "." + commandName + ".SubCommands").getKeys(false);

          for(String subCommand : subCommands) {
            TNECommandObject sub = new TNECommandObject(subCommand.toLowerCase(), configuration.getDouble(base + "." + commandName + ".SubCommands." + subCommand + ".Cost"));
            command.addSub(sub);
          }
        }

        commands.put(id, command);
      }
    }
  }

  private void loadInventories(FileConfiguration configuration, String baseNode, String identifier) {
    String base = baseNode + "Objects.Inventories";

    if(configuration.contains(base)) {
      Boolean zero = !configuration.contains(base + ".ZeroMessage") || configuration.getBoolean(base + ".ZeroMessage");
      Boolean enabledMain = !configuration.contains(base + ".Enabled") || configuration.getBoolean(base + ".Enabled");
      configurations.put(base + ".ZeroMessage", zero);
      configurations.put(base + ".Enabled", enabledMain);

      Set<String> inventoryNames = configuration.getConfigurationSection(base).getKeys(false);

      for(String inventoryName : inventoryNames) {

        String id = (identifier != null)? identifier + ":" + inventoryName : inventoryName;
        boolean enabled = configuration.getBoolean(base + "." + inventoryName + ".Enabled");
        boolean timed = configuration.getBoolean(base + "." + inventoryName + ".Timed");
        double cost = configuration.getDouble(base + "." + inventoryName + ".Cost");

        TNEInventoryObject inventory = new TNEInventoryObject(inventoryName, enabled, timed, cost);

        if(configuration.contains(base + "." + inventoryName + ".Packages")) {
          Set<String> packageNames = configuration.getConfigurationSection(base + "." + inventoryName + ".Packages").getKeys(false);

          for(String packageName : packageNames) {

            long packageTime = configuration.getLong(base + "." + inventoryName + ".Packages" + "." + packageName + ".Time");
            double packageCost = configuration.getDouble(base + "." + inventoryName + ".Packages" + "." + packageName + ".Cost");

            inventory.addPackage(new TNEAccessPackage(packageName, packageTime, packageCost));
          }
        }
        inventories.put(id, inventory);
      }
    }
  }

  public String inventoryType(InventoryType type) {
    switch(type) {
      case ANVIL:
        return "Anvil";
      case BEACON:
        return "Beacon";
      case BREWING:
        return "BrewStand";
      case CHEST:
        return "Chest";
      case CRAFTING:
        return "Crafting";
      case CREATIVE:
        return "CreativeInventory";
      case DISPENSER:
        return "Dispenser";
      case DROPPER:
        return "Dropper";
      case ENCHANTING:
        return "EnchantmentTable";
      case ENDER_CHEST:
        return "EnderChest";
      case FURNACE:
        return "Furnace";
      case HOPPER:
        return "Hopper";
      case MERCHANT:
        return "Villager";
      case PLAYER:
        return "SurvivalInventory";
      case WORKBENCH:
        return "Workbench";
      default:
        return "Chest";
    }
  }

  public List<TNEAccessPackage> getInventoryPackages(String type, String world, String player) {
    List<TNEAccessPackage> packages = new ArrayList<>();
    if(inventories.containsKey(type)) {
      packages.addAll(inventories.get(type).getPackages());
    }

    if(inventories.containsKey(world + ":" + type)) {
      packages.addAll(inventories.get(world + ":" + type).getPackages());
    }

    if(inventories.containsKey(player + ":" + type)) {
      packages.addAll(inventories.get(player + ":" + type).getPackages());
    }

    return packages;
  }

  public boolean isTimed(InventoryType type, String world, String player) {
    String invType = inventoryType(type);
    if(containsInventory(player + ":" + invType)) return inventories.get(player + ":" + invType).isTimed();
    if(containsInventory(world + ":" + invType)) return inventories.get(world + ":" + invType).isTimed();
    return containsInventory(invType) && inventories.get(invType).isTimed();
  }

  private boolean containsInventory(String type) {
    return inventories.containsKey(type);
  }

  public boolean inventoryEnabled(InventoryType type, String world, String player) {
    String invType = inventoryType(type);
    if(configurations.containsKey("Players." + player + ".Objects.Inventories.Enabled"))
      return (Boolean)configurations.get("Players." + player + ".Objects.Inventories.Enabled");
    if(configurations.containsKey("Worlds." + world + ".Objects.Inventories.Enabled"))
      return (Boolean)configurations.get("Worlds." + world + ".Objects.Inventories.Enabled");
    if(configurations.containsKey("Objects.Inventories.Enabled"))
      return (Boolean)configurations.get("Objects.Inventories.Enabled");

    if(containsInventory(player + ":" + invType)) return inventories.get(player + ":" + invType).isEnabled();
    if(containsInventory(world + ":" + invType)) return inventories.get(world + ":" + invType).isEnabled();
    return containsInventory(invType) && inventories.get(invType).isEnabled();
  }

  public double getInventoryCost(InventoryType type, String world, String player) {
    if(inventoryEnabled(type, world, player)) {
      String invType = inventoryType(type);
      if(containsInventory(player + ":" + invType)) return inventories.get(player + ":" + invType).getCost();
      if(containsInventory(world + ":" + invType)) return inventories.get(world + ":" + invType).getCost();
      return (containsInventory(invType))? inventories.get(invType).getCost() : 0.0;
    }
    return 0.0;
  }

  public double getCommandCost(String command, String[] arguments, String world, String player) {
    TNECommandObject commandObject = commands.get(command);
    if(commands.containsKey(world + ":" + command)) {
      commandObject = commands.get(world + ":" + command);
    }

    if(commands.containsKey(player + ":" + command)) {
      commandObject = commands.get(player + ":" + command);
    }

    if(commandObject != null) {
      if(arguments.length > 0) {
        TNECommandObject sub = commandObject.findSub(arguments[0]);
        if(sub != null) {
          return sub.getCost();
        }
      }
      return commandObject.getCost();
    }
    return 0;
  }
}