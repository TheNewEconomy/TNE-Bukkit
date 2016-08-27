package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.api.MojangAPI;
import com.github.tnerevival.serializable.SerializableItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MISCUtils {

	//True MISC Utils
	/**
	 * Returns the player's account world(or the world it's meant to share accounts with if configured to do so)
	 */
	public static String getWorld(UUID id) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.getPlayer(id) != null) {
				String actualWorld = MISCUtils.getPlayer(id).getWorld().getName();
				if(MISCUtils.worldConfigExists("Worlds." + actualWorld + ".ShareAccounts") && TNE.instance.worldConfigurations.getBoolean("Worlds." + actualWorld + ".ShareAccounts")) {
					return TNE.instance.worldConfigurations.getString("Worlds." + actualWorld + ".ShareWorld");
				}
				return actualWorld;
			}
		}
		return TNE.instance.defaultWorld;
	}
	
	public static String getWorld(Player player) {
		return MISCUtils.getWorld(MISCUtils.getID(player));
	}
	
	/**
	 * Returns the player's actual current world
	 */
	public static String getActualWorld(UUID id) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.getPlayer(id) != null) {
				return MISCUtils.getPlayer(id).getWorld().getName();
			}
		}
		return TNE.instance.defaultWorld;
	}

	public static boolean hasItems(UUID id, List<SerializableItemStack> items) {
	  for(SerializableItemStack item : items) {
	    ItemStack stack = item.toItemStack();
      if(getItemCount(id, stack.getType()) < stack.getAmount()) {
        return false;
      }
    }
    return true;
  }

  public static void setItems(UUID id, List<SerializableItemStack> items, boolean add) {
  	for(SerializableItemStack item : items) {
  	  Material type = item.toItemStack().getType();
  	  if(add) {
        setItemCount(id, type, getItemCount(id, type) + item.getAmount());
  	    continue;
      }
  	  setItemCount(id, type, getItemCount(id, type) - item.getAmount());
    }
	}
	
	public static Integer getItemCount(UUID id, Material item) {
		Player p = MISCUtils.getPlayer(id);
		int count = 0;
		if(item != null) {
			for(ItemStack i : p.getInventory().getContents()) {
				if(i != null && i.getType() != null && i.getType() == item) {
					count += i.getAmount();
				}
			}
		}
		return count;
	}
	
	public static void setItemCount(UUID id, Material item, Integer amount) {
		Player p = MISCUtils.getPlayer(id);
		Integer count = getItemCount(id, item);
		if(item != null) {
			if(count > amount) {
				Integer remove = count - amount;
				Integer slot = 0;
				for(ItemStack i : p.getInventory().getContents()) {
					if(i != null && i.getType() != null && i.getType() == item) {
						if(remove > i.getAmount()) {
							remove -= i.getAmount();
							i.setAmount(0);
							p.getInventory().setItem(slot, null);
						} else {
							if(i.getAmount() - remove > 0) {
								i.setAmount(i.getAmount() - remove);
								p.getInventory().setItem(slot, i);
								return;
							}
							p.getInventory().setItem(slot, null);
							return;
						}
					}
					slot++;
				}
			} else if(count < amount) {
				Integer add = amount - count;
				
				while(add > 0) {
					if(add > item.getMaxStackSize()) {
						if(p.getInventory().firstEmpty() != -1) {
							p.getInventory().addItem(new ItemStack(item, item.getMaxStackSize()));
						} else {
							p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(item, item.getMaxStackSize()));
						}
						add -= item.getMaxStackSize();
					} else {
						if(p.getInventory().firstEmpty() != -1) {
							p.getInventory().addItem(new ItemStack(item, add));
						} else {
							p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(item, add));
						}
						add = 0;
					}
				}
			}
		}
	}
	
	public static void reloadConfigurations(String type) {
		if(type.equalsIgnoreCase("all")) {
			TNE.instance.reloadConfig();
			reloadConfigsMaterials();
			reloadConfigsMessages();
			reloadConfigsMobs();
			reloadConfigsObjects();
			reloadConfigsWorlds();
		} else if(type.equalsIgnoreCase("config")) {
			TNE.instance.reloadConfig();
			TNE.configurations.load(TNE.instance.getConfig(), "main");
		} else if(type.equalsIgnoreCase("materials")) {
			reloadConfigsMaterials();
		} else if(type.equalsIgnoreCase("messages")) {
			reloadConfigsMessages();
		} else if(type.equalsIgnoreCase("mobs")) {
			reloadConfigsMobs();
		} else if(type.equalsIgnoreCase("objects")) {
			reloadConfigsObjects();
		} else if(type.equalsIgnoreCase("worlds")) {
			reloadConfigsWorlds();
		}
	}

	public static void reloadConfigsMaterials() {
		if(TNE.instance.materials == null) {
			TNE.instance.materials = new File(TNE.instance.getDataFolder(), "materials.yml");
		}
		TNE.instance.materialConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.materials);
		TNE.configurations.load(TNE.instance.materialConfigurations, "materials");
	}
	
	public static void reloadConfigsMobs() {
		if(TNE.instance.mobs == null) {
			TNE.instance.mobs = new File(TNE.instance.getDataFolder(), "mobs.yml");
		}
		TNE.instance.mobConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.mobs);
		TNE.configurations.load(TNE.instance.mobConfigurations, "mob");
	}
	
	public static void reloadConfigsMessages() {
		if(TNE.instance.messages == null) {
			TNE.instance.messages = new File(TNE.instance.getDataFolder(), "messages.yml");
		}
		TNE.instance.messageConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.messages);
		TNE.configurations.load(TNE.instance.messageConfigurations, "messages");
	}

	public static void reloadConfigsObjects() {
		if(TNE.instance.objects == null) {
			TNE.instance.objects = new File(TNE.instance.getDataFolder(), "objects.yml");
		}
		TNE.instance.objectConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.objects);
		TNE.configurations.load(TNE.instance.objectConfigurations, "objects");
	}
	
	public static void reloadConfigsWorlds() {
		if(TNE.instance.worlds == null) {
			TNE.instance.worlds = new File(TNE.instance.getDataFolder(), "worlds.yml");
		}
		TNE.instance.worldConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.worlds);
	}
	
	public static String sendGetRequest(String URL) {
		StringBuilder builder = new StringBuilder();
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response;
			while((response = reader.readLine()) != null) {
				builder.append(response);
			}
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	public static UUID distringuishId(String identifier) {
	  Player player = (isUUID(identifier))? MISCUtils.getPlayer(UUID.fromString(identifier)) : MISCUtils.getPlayer(identifier);

    return MISCUtils.getID(player);
	}

  public static boolean isUUID(String value) {
    try {
      UUID.fromString(value);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
	
	@SuppressWarnings("deprecation")
	public static Player getPlayer(String username) {
		if(!TNE.configurations.getBoolean("Core.UUID")) {
			return Bukkit.getPlayer(username);
		}
		UUID id = getID(username);
		return Bukkit.getPlayer(id);
	}
	
	@SuppressWarnings("deprecation")
	public static Player getPlayer(UUID id) {
		if(!TNE.configurations.getBoolean("Core.UUID")) {
			return Bukkit.getPlayer(ecoToUsername(id));
		}
		return Bukkit.getPlayer(id);
	}
	
	public static UUID ecoID(String username) {
		if(TNE.instance.manager.ecoIDs.containsKey(username)) {
			return TNE.instance.manager.ecoIDs.get(username);
		}
		UUID eco = MISCUtils.genUUID();
		TNE.instance.manager.ecoIDs.put(username, eco);
		return eco;
	}
	
	public static UUID genUUID() {
		UUID id = UUID.randomUUID();
		while(TNE.instance.manager.accounts.containsKey(id) || TNE.instance.manager.ecoIDs.containsKey(id)) {
			//This should never happen, but we'll play it safe
			id = UUID.randomUUID();
		}
		return id;
	}
	
	public static String ecoToUsername(UUID id) {
		String username = (String) getKey(TNE.instance.manager.ecoIDs, id);
		return username;
	}
	
	public static UUID getID(String player) {
		if(player.contains("town-")) {
			return MISCUtils.ecoID(player);
		}
		
		if(player.contains("nation-")) {
			return MISCUtils.ecoID(player);
		}
		
		if(!TNE.configurations.getBoolean("Core.UUID")) {
			return ecoID(player);
		}
		
		UUID mojangID = MojangAPI.getPlayerUUID(player);
		return (mojangID == null)? MISCUtils.ecoID(player) : mojangID;
	}
	
	public static UUID getID(Player player) {
		if(!TNE.configurations.getBoolean("Core.UUID")) {
			return ecoID(player.getDisplayName());
		}
		return player.getUniqueId();
	}
	
	public static UUID getID(OfflinePlayer player) {
		if(!TNE.configurations.getBoolean("Core.UUID")) {
			return ecoID(player.getName());
		}
		return player.getUniqueId();
	}
	
	@SuppressWarnings("rawtypes")
	public static Object getKey(Map m, Object value) {
		for(Object obj : m.keySet()) {
			if(m.get(obj).equals(value)) {
				return obj;
			}
		}
		return null;
	}
	
	//Format Utils
	public static Boolean shorten(String world) {
		if(multiWorld() && worldConfigExists("Worlds." + world + ".Shorten")) {
			return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Shorten");
		}
		return TNE.configurations.getBoolean("Core.Shorten");
	}
	
	public static String formatBalance(String world, double balance) {
		return (shorten(world)) ? formatBalance(world, balance, true) : formatBalance(world, balance, false);
	}
	
	public static String formatBalance(String world, double balance, Boolean shorten) {
		String balanceString = (String.valueOf(balance).contains(".")) ? String.valueOf(balance) : String.valueOf(balance) + ".0";
		String[] split = balanceString.split("\\.");
		if(Integer.valueOf(split[1]) > 0) {
			return (shorten) ? ChatColor.GOLD + getShort(Integer.valueOf(split[0])) + " " + getName(world, balance, "major") + " and " + ChatColor.GOLD + Integer.valueOf(split[1]) + " " + getName(world, balance, "minor") : ChatColor.GOLD + "" + Integer.valueOf(split[0]) + " " + getName(world, balance, "major") + " and " + ChatColor.GOLD + Integer.valueOf(split[1]) + " " + getName(world, balance, "minor");
		} else {
			return (shorten) ? ChatColor.GOLD + getShort(Integer.valueOf(split[0])) + " " + getName(world, balance, "major") : ChatColor.GOLD + "" + Integer.valueOf(split[0]) + " " + getName(world, balance, "major");
		}
	}
	
	public static String getShort(double balance) {
		Integer dollars = (int) Math.floor(balance);
		if (dollars < 1000) {
			return "" + dollars;
		}
	    int exp = (int) (Math.log(dollars) / Math.log(1000));
	    return String.format("%.1f%c", dollars / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
	}
	
	public static String getName(String world, double balance, String type) {
		String balanceString = (String.valueOf(balance).contains(".")) ? String.valueOf(balance) : String.valueOf(balance) + ".0";
		String[] split = balanceString.split("\\.");
		if(type.equalsIgnoreCase("major")) {
			return (Integer.valueOf(split[0]) != 1) ? getMajorCurrencyName(world, false) : getMajorCurrencyName(world, true);
		} else if(type.equalsIgnoreCase("minor")) {
			return (Integer.valueOf(split[0]) != 1) ? getMinorCurrencyName(world, false) : getMinorCurrencyName(world, true);
		}
		return (Integer.valueOf(split[0]) != 1) ? getMajorCurrencyName(world, false) : getMajorCurrencyName(world, true);
	}
	
	public static String getMajorCurrencyName(String world, Boolean singular) {
		if(multiWorld() && worldConfigExists("Worlds." + world + ".Currency.MajorName")) {
			return (singular) ? TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.MajorName.Single") : TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.MajorName.Plural");
		}
		return (singular) ? TNE.configurations.getString("Core.Currency.MajorName.Single") : TNE.configurations.getString("Core.Currency.MajorName.Plural");
	}
	
	public static String getMinorCurrencyName(String world, Boolean singular) {
		if(multiWorld() && worldConfigExists("Worlds." + world + ".Currency.MinorName")) {
			return (singular) ? TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.MinorName.Singular") : TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.MinorName.Plural");
		}
		return (singular) ? TNE.configurations.getString("Core.Currency.MinorName.Single") : TNE.configurations.getString("Core.Currency.MinorName.Plural");
	}
	
	//ItemStack Utils
	public static ItemStack getFurnaceSource(ItemStack result) {
		List<Recipe> recipes = TNE.instance.getServer().getRecipesFor(result);
		for(Recipe r : recipes) {
			if(r instanceof FurnaceRecipe) {
				return ((FurnaceRecipe) r).getInput();
			}
		}
		return new ItemStack(Material.AIR, 1);
	}
	
	public static Boolean chargeClick(List<String> lore, String inv) {
		if(lore != null) {
			String search = "Enchanting Cost:";
			switch(inv) {
				case "smelt":
					search = "Smelting Cost:";
					break;
				case "brew":
					search = "Brewing Cost:";
					break;
				default:
					search = "Enchanting Cost:";
					break;
			}
			
			for(String s : lore) {
				if(s.contains(search)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static Boolean useBankBalance(String world) {
		if(multiWorld()) {
			if(worldConfigExists("Bank.Connect", world)) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.Connected");
			}
			return false;
		}
		return TNE.configurations.getBoolean("Core.Bank.Connected");
	}
	
	//World Utils
	public static Boolean multiWorld() {
		return TNE.configurations.getBoolean("Core.Multiworld");
	}
	
	public static Boolean worldConfigExists(String node, String world) {
		return (TNE.instance.worldConfigurations.get("Worlds." + world + "." + node) != null);
	}
	
	public static Boolean worldConfigExists(String node) {
		return (TNE.instance.worldConfigurations.get(node) != null);
	}
}