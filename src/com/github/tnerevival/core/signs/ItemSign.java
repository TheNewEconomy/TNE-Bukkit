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
package com.github.tnerevival.core.signs;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.material.MaterialHelper;
import com.github.tnerevival.core.signs.item.ItemEntry;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by creatorfromhell on 1/17/2017.
 **/
public class ItemSign extends TNESign {

  public TreeMap<Integer, ItemEntry> offers = new TreeMap<>();
  public int currentOffer = 0;

  public ItemSign(UUID owner, SerializableLocation location) {
    super(owner, location);
    setType(SignType.ITEM);
  }

  public void addOffer(Player player, String[] lines) {
    MISCUtils.debug("ItemSign addOffer lines: " + lines.toString());
    String world = location.getLocation().getWorld().getName();
    if(!IDFinder.getID(player).equals(owner)) {
      new Message("Messages.General.NoPerm").translate(world, player);
      return;
    }
    boolean admin = false;
    BigDecimal buy = new BigDecimal(-1.0);
    BigDecimal sell = new BigDecimal(-1.0);
    ItemStack trade = new ItemStack(Material.AIR);
    ItemStack item = new ItemStack(Material.AIR);
    if(offers.size() > 0 && !TNE.instance().api().getBoolean("Core.Signs.Item.Multiple", world, IDFinder.getID(player))) {
      new Message("Messages.SignShop.NoMultiple").translate(world, player);
      return;
    }

    if(offers.size() == TNE.instance().api().getInteger("Core.Signs.Item.MaxOffers", world, owner.toString())) {
      new Message("Messages.SignShop.MaxOffers").translate(world, player);
      return;
    }

    String[] mainLine = lines[1].split(":");
    Material itemType = MaterialHelper.getMaterial(mainLine[0]);
    if(!itemType.equals(Material.AIR)) {
      Integer itemAmount = (mainLine.length > 1 && MISCUtils.isInteger(mainLine[1]))? Integer.valueOf(mainLine[1]) : 1;
      item.setType(itemType);
      item.setAmount(itemAmount);
      if(lines[3].contains("Admin")) {
        if(!player.hasPermission("tne.sign.admin")) {
          new Message("Messages.General.NoPerm").translate(world, player);
          return;
        }
        admin = true;
      }
      String[] separated = lines[2].split(":");
      if(!MaterialHelper.getMaterial(separated[0]).equals(Material.AIR)) {
        Material material = MaterialHelper.getMaterial(separated[0]);
        if(!material.equals(Material.AIR)) {
          MISCUtils.debug("Adding trade offer for material " + material.name().toLowerCase());
          Integer tradeAmount = (separated.length > 1 && MISCUtils.isInteger(separated[1]))? Integer.valueOf(separated[1]) : 1;
          trade = new ItemStack(material, tradeAmount);
        }
      } else if(lines[2].toUpperCase().contains("B") || lines[2].toUpperCase().contains("S")) {
        for(String s : separated) {
          String[] split = s.trim().split(" ");
          MISCUtils.debug("Line 2: " + lines[2]);
          MISCUtils.debug("Value: " + split[0]);
          Double value = (CurrencyFormatter.isDouble(split[0], world))? CurrencyFormatter.translateDouble(split[0], world) : -1.0;
          switch(split[1].toUpperCase().trim()) {
            case "B":
              MISCUtils.debug("Added buy value of " + value);
              buy = new BigDecimal(value);
              break;
            case "S":
              MISCUtils.debug("Added sell value of " + value);
              sell = new BigDecimal(value);
              break;
            default:
              break;
          }
        }
      }
      if(trade.getType().equals(Material.AIR) && buy.doubleValue() < 0.0 && sell.doubleValue() < 0.0) {
        new Message("Messages.SignShop.InvalidBuy").translate(world, player);
        return;
      }
      MISCUtils.debug("Added offer for " + item.getType().toString());
      ItemEntry entry = new ItemEntry(offers.size(), item);
      entry.setBuy(buy);
      entry.setSell(sell);
      entry.setTrade(trade);
      entry.setAdmin(admin);
      offers.put(entry.getOrder(), entry);
      TNE.instance().manager.signs.put(location, this);
    }
  }

  public void setCurrentOffer(int offer) {
    if(offer < 0) currentOffer = offers.size() - 1;
    else if(offer == offers.size()) currentOffer = 0;
    else currentOffer = offer;
    TNE.instance().manager.signs.put(location, this);
    update();
  }

  public void update() {
    World w = location.getLocation().getWorld();
    Block b = w.getBlockAt(location.getLocation());
    if(b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) {
      Sign s = (Sign)b.getState();
      updateLines(s);
    }
  }

  private void updateLines(Sign s) {
    ItemEntry entry = offers.get(currentOffer);
    MISCUtils.debug("ItemSign.updateLines Offer: " + currentOffer);
    s.setLine(1, MaterialHelper.getShopName(entry.getItem().getType()) + ((entry.getItem().getAmount() > 1)? ":" + entry.getItem().getAmount() : ""));
    String buy = "";
    if(!entry.getTrade().getType().equals(Material.AIR)) {
      MISCUtils.debug("Added trade values");
      buy = MaterialHelper.getShopName(entry.getTrade().getType());
      if(entry.getTrade().getAmount() > 1) {
        buy = buy + ":" + entry.getTrade().getAmount();
      }
    } else {
      MISCUtils.debug("Added sell/buy values");
      MISCUtils.debug("Buy Double: " + entry.getBuy().doubleValue());
      MISCUtils.debug("Sell Double: " + entry.getSell().doubleValue());
      if(entry.getBuy().doubleValue() >= 0.0) buy = entry.getBuy().doubleValue() + " B";

      if(entry.getSell().doubleValue() >= 0.0) {
        if(!buy.trim().equalsIgnoreCase("")) buy = buy + ":";
        buy = buy + entry.getSell().doubleValue() + " S";
      }
    }
    String author = (entry.admin)? ChatColor.RED + "Admin" : IDFinder.getOffline(owner.toString()).getName();
    MISCUtils.debug("Buy line: " + buy);
    s.setLine(2, buy);
    s.setLine(3, author);
    s.update(true);
  }

  private void shiftOffers(int after) {
    TreeMap<Integer, ItemEntry> newOffers = new TreeMap<>();
    for(ItemEntry entry : offers.values()) {
      int order = (entry.getOrder() > after)? entry.getOrder() - 1 : entry.getOrder();
      newOffers.put(order, entry.reorder(order));
    }
    offers = newOffers;
    if(currentOffer == offers.size()) currentOffer = offers.size() - 1;
  }

  private boolean changeStock(Player player, Material material, int amount, boolean add) {
    String world = location.getLocation().getWorld().getName();
    SignChest chest = getAttachedChest();
    if(chest != null || chest == null && TNE.instance().api().getBoolean("Core.Signs.Item.EnderChest")) {
      if(chest == null) chest = new SignChest();
      if(!chest.isEnder() || chest.isEnder() && TNE.instance().api().getBoolean("Core.Signs.Item.EnderChest")) {
        BlockState state = chest.getLocation().getBlock().getState();
        Inventory inventory = (chest.isEnder())? IDFinder.getOffline(owner.toString()).getPlayer().getEnderChest()
            : ((state instanceof DoubleChest)? ((DoubleChest) state).getInventory() : ((Chest) state).getInventory());
        int itemCount = MISCUtils.getItemCount(inventory, material);
        MISCUtils.debug("Item Count: " + itemCount);
        if (add) {
          int leftOver = MISCUtils.leftOver(inventory, material, itemCount + amount);
          if(leftOver > 0) {
            if(Bukkit.getOnlinePlayers().contains(IDFinder.getOffline(owner.toString()).getPlayer())) {
              MISCUtils.setItemCount(owner, material, MISCUtils.getItemCount(owner, material) + leftOver);
              new Message("Messages.SignShop.DroppingExtra").translate(world, IDFinder.getPlayer(owner.toString()));
              return true;
            }
            new Message("Messages.SignShop.UnableAccept").translate(world, player);
            return false;
          }
          MISCUtils.setItemCount(inventory, material, itemCount + amount);
          return true;
        }
        if(itemCount < amount) {
          new Message("Messages.SignShop.OutOfStock").translate(world, player);
          return false;
        }
        MISCUtils.setItemCount(inventory, material, itemCount - amount);
        if(chest.isEnder()) {
          IDFinder.getOffline(owner.toString()).getPlayer().getEnderChest().setContents(inventory.getContents());
        } else {
          if (state instanceof DoubleChest) ((DoubleChest) state).getInventory().setContents(inventory.getContents());
          else ((Chest) state).getInventory().setContents(inventory.getContents());
        }
        return true;
      }
    }
    return false;
  }

  private boolean hasInventory(Material material, int amount) {
    SignChest chest = getAttachedChest();
    if (chest != null || chest == null && TNE.instance().api().getBoolean("Core.Signs.Item.EnderChest")) {
      if (chest == null) chest = new SignChest();
      if (!chest.isEnder() || chest.isEnder() && TNE.instance().api().getBoolean("Core.Signs.Item.EnderChest")) {
        BlockState state = chest.getLocation().getBlock().getState();
        Inventory inventory = (chest.isEnder()) ? IDFinder.getOffline(owner.toString()).getPlayer().getEnderChest()
            : ((state instanceof DoubleChest)? ((DoubleChest) state).getInventory() : ((Chest) state).getInventory());
        return MISCUtils.hasItem(inventory, material, amount);
      }
    }
    return false;
  }

  @Override
  public boolean onClick(Player player, boolean shift) {
    String world = location.getLocation().getWorld().getName();
    if(super.onClick(player, shift)) {
      if (player.hasPermission(SignType.ITEM.getUsePermission())) {
        if(shift && TNE.instance().api().getBoolean("Core.Signs.Item.Multiple", world, IDFinder.getID(player))) {
          if(!IDFinder.getID(player).equals(owner)) {
            new Message("Messages.General.NoPerm").translate(world, player);
            return false;
          }
          offers.remove(currentOffer);
          if(offers.size() == 0) {
            location.getLocation().getWorld().getBlockAt(location.getLocation()).setType(Material.AIR);
            TNESign.removeSign(location);
            return true;
          }
          shiftOffers(currentOffer);
          update();
          new Message("Messages.SignShop.Removed").translate(world, player);
          return true;
        } else if(!shift) {
          ItemEntry entry = offers.get(currentOffer);
          if(entry.getSell().doubleValue() >= 0.0) {
            if(!MISCUtils.hasItem(player.getInventory(), entry.getItem().getType(), entry.getItem().getAmount())) {
              new Message("Messages.SignShop.Insufficient").translate(world, player);
              return false;
            }
            if(!entry.isAdmin()) {
              if(!AccountUtils.transaction(owner.toString(), null, entry.getSell(), TransactionType.MONEY_INQUIRY, world)) {
                new Message("Messages.SignShop.OwnerInsufficient").translate(world, player);
                return false;
              }
              if(!changeStock(player, entry.getItem().getType(), entry.getItem().getAmount(), true)) {
                return false;
              }
              AccountUtils.transaction(owner.toString(), null, entry.getSell(), TransactionType.MONEY_REMOVE, world);
            }
            if(AccountUtils.transaction(IDFinder.getID(player).toString(), null, entry.getSell(), TransactionType.MONEY_GIVE, world)) {
              MISCUtils.setItemCount(player.getInventory(),
                                     entry.getItem().getType(),
                                     MISCUtils.getItemCount(IDFinder.getID(player),
                                                            entry.getItem().getType()
                                     ) - entry.getItem().getAmount());
              player.updateInventory();
            }
            new Message("Messages.SignShop.Successful").translate(world, player);
            return false;
          }
        }
      }
    }
    return false;
  }

  @Override
  public boolean onRightClick(Player player, boolean shift) {
    String world = location.getLocation().getWorld().getName();
    if(super.onRightClick(player, shift)) {
      if (player.hasPermission(SignType.ITEM.getUsePermission())) {
        ItemEntry entry = offers.get(currentOffer);
        if(entry.getBuy().doubleValue() >= 0.0 || !entry.getTrade().getType().equals(Material.AIR)) {
          if(!entry.getTrade().getType().equals(Material.AIR)) {
            if(!MISCUtils.hasItem(player.getInventory(), entry.getItem().getType(), entry.getItem().getAmount())) {
              new Message("Messages.SignShop.Insufficient").translate(world, player);
              return false;
            }
          }
          if(entry.getBuy().doubleValue() >= 0.0) {
            if(!AccountUtils.transaction(IDFinder.getID(player).toString(), null, entry.getBuy(), TransactionType.MONEY_INQUIRY, world)) {
              new Message("Messages.SignShop.Insufficient").translate(world, player);
              return false;
            }
            if(!entry.isAdmin() && !changeStock(player, entry.getItem().getType(), entry.getItem().getAmount(), false)) {
              return false;
            }
            if(!entry.isAdmin()) {
              AccountUtils.transaction(owner.toString(), null, entry.getBuy(), TransactionType.MONEY_GIVE, world);
            }
            AccountUtils.transaction(IDFinder.getID(player).toString(), null, entry.getBuy(), TransactionType.MONEY_REMOVE, world);
          } else {
            if(!MISCUtils.hasItems(IDFinder.getID(player), Collections.singletonList(new SerializableItemStack(0, entry.getTrade())))) {
              new Message("Messages.SignShop.Insufficient").translate(world, player);
              return false;
            }
            if(!entry.isAdmin() && !hasInventory(entry.getItem().getType(), entry.getItem().getAmount())) {
              new Message("Messages.SignShop.OutOfStock").translate(world, player);
              return false;
            }
            if(!entry.isAdmin() && !changeStock(player, entry.getTrade().getType(), entry.getTrade().getAmount(), true)) {
              return false;
            }
            if(!entry.isAdmin()) changeStock(player, entry.getItem().getType(), entry.getItem().getAmount(), false);
            MISCUtils.setItems(IDFinder.getID(player), Collections.singletonList(new SerializableItemStack(0, entry.getTrade())), false);
          }
          MISCUtils.setItemCount(player.getInventory(),
              entry.getItem().getType(),
              MISCUtils.getItemCount(IDFinder.getID(player),
                  entry.getItem().getType()
              ) + entry.getItem().getAmount());
          player.updateInventory();
          new Message("Messages.SignShop.Successful").translate(world, player);
          return true;
        }
      }
    }
    return false;
  }
}