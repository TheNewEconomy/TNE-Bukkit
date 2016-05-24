package com.github.tnerevival.commands.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.material.MaterialHelper;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import com.github.tnerevival.serializable.SerializableItemStack;

public class ShopAddCommand extends TNECommand {

	public ShopAddCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "add";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "+i" };
	}

	@Override
	public String getNode() {
		return "tne.shop.add";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/shop add <shop> [amount] [item] [gold:amount] [trade:name:amount(default 1)]  - Add a new item to your shop for [cost] and/or [trade]. Leave out item name to use currently held item.");
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		if(sender instanceof Player && arguments.length >= 1) {
			if(Shop.exists(arguments[0])) {
				if(Shop.canModify(arguments[0], (Player)sender)) {
					Player p = (Player)sender;
					Shop s = Shop.getShop(arguments[0]);
					
					ItemStack item = p.getInventory().getItemInMainHand().clone();
					item.setAmount(1);
					double gold = -1.0;
					ItemStack trade = null;
					
					if(arguments.length == 2) {
						item.setAmount(Integer.parseInt(arguments[1]));
					} else if(arguments.length > 2 && arguments.length <= 5) {
						if(arguments.length > 2 && arguments.length < 5 && !arguments[2].contains(":")) {
							Material mat = MaterialHelper.getMaterial(arguments[2]);
							
							if(mat != Material.AIR) {
								item = new ItemStack(mat, Integer.parseInt(arguments[1]));

								if(s.addItem(new ShopEntry(new SerializableItemStack((s.getItems().size() - 1), item), gold))) {
									//TODO: Added item $name message.
									return true;
								}
								
								//TODO: Couldn't add item to shop.
								return false;
							}
							//TODO: Invalid item name entered.
							return false;
						} else if(arguments.length > 2 && arguments.length < 5 && arguments[2].contains(":")) {
							item.setAmount(Integer.parseInt(arguments[1]));
							gold = Double.parseDouble(arguments[2].split(":")[1]);
							
							if(arguments.length == 4) {
								String[] split = arguments[3].split(":");
								Material mat = MaterialHelper.getMaterial(split[1]);
								if(mat != Material.AIR) {
									Integer amount = (split.length == 3) ? Integer.parseInt(split[2]) : 1;
									trade = new ItemStack(mat, amount);
									
									if(s.addItem(new ShopEntry(new SerializableItemStack((s.getItems().size() - 1), item), gold, new SerializableItemStack(0, trade)))) {
										//TODO: Added item $name message.
										return true;
									}
									
									//TODO: Couldn't add item to shop.
									return false;
								}
								//TODO: Invalid trade item name entered.
								return false;
							}

							if(s.addItem(new ShopEntry(new SerializableItemStack((s.getItems().size() - 1), item), gold))) {
								//TODO: Added item $name message.
								return true;
							}
							
							//TODO: Couldn't add item to shop.
							return false;
						} else {
							Material mat = MaterialHelper.getMaterial(arguments[2]);
							if(mat != Material.AIR) {
								item = new ItemStack(mat, Integer.parseInt(arguments[1]));
								
								String[] split = arguments[3].split(":");
								Material tradeMat = MaterialHelper.getMaterial(split[1]);
								if(tradeMat != Material.AIR) {
									Integer amount = (split.length == 3) ? Integer.parseInt(split[2]) : 1;
									trade = new ItemStack(tradeMat, amount);

									if(s.addItem(new ShopEntry(new SerializableItemStack((s.getItems().size() - 1), item), gold, new SerializableItemStack(0, trade)))) {
										//TODO: Added item $name message.
										return true;
									}
									
									//TODO: Couldn't add item to shop.
									return false;
								}
								
								//TODO: Invalid trade item name entered.
								return false;
							}
							//TODO: Invalid item name entered.
							return false;
						}
					}
				}
				//TODO: Must be shop owner to do that.
				return false;
			}
			//TODO: Shop doesn't exist message.
			return false;
		} else {
			help(sender);
		}
		return false;
	}
}