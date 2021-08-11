package net.tnemc.core.menu.icons;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.menu.ResponseData;
import net.tnemc.core.menu.consumables.IconClick;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/5/2017.
 */
public class Icon {

  protected Map<String, Object> data = new HashMap<>();

  protected final Optional<ItemStack> item;

  protected Integer slot;
  protected Material material;
  protected short damage;
  protected String display;
  protected List<String> lore;

  protected Consumer<IconClick> clickAction;

  protected String node;
  protected String switchMenu;
  protected String message;
  protected String chat = "";
  protected boolean removeViewer = true;
  protected boolean close;

  public Icon(Integer slot, Material material, String display) {
    this(slot, material, display, (short)0, new ArrayList<>());
  }

  public Icon(Integer slot, Material material, String display, short damage) {
    this(slot, material, display, damage, new ArrayList<>());
  }

  public Icon(Integer slot, ItemStack stack) {
    this.item = Optional.of(stack);
    this.node = "";
    this.switchMenu = "";
    this.message = "";
    this.close = true;
  }

  public Icon(Integer slot, ItemStack stack, String display) {
    this(slot, stack.getType(), display, stack.getDurability(), new ArrayList<>());
  }

  public Icon(Integer slot, ItemStack stack, String display, List<String> lore) {
    this(slot, stack.getType(), display, stack.getDurability(), lore);
  }

  public Icon(Integer slot, Material material, String display, short damage, List<String> lore) {
    this.item = Optional.empty();
    this.slot = slot;
    this.material = material;
    this.display = display;
    this.damage = damage;
    this.lore = lore;

    this.node = "";
    this.switchMenu = "";
    this.message = "";
    this.close = true;
  }

  public ItemStack buildStack(Player player) {
    if(item.isPresent()) {
      return item.get();
    }
    ItemStack item = new ItemStack(material, 1, damage);
    ItemMeta meta = Bukkit.getServer().getItemFactory().getItemMeta(material);
    meta.setLore(lore);
    meta.setDisplayName(display);
    item.setItemMeta(meta);

    return item;
  }

  public boolean canClick(Player player) {
    if(node.trim().equalsIgnoreCase("")) return true;
    return player.hasPermission(node);
  }

  public void onClick(String menu, Player player, ClickType type) {
    if(!switchMenu.trim().equalsIgnoreCase("")) close = false;
    if(!chat.trim().equalsIgnoreCase("")) {

      final String toGo = (switchMenu.trim().equalsIgnoreCase(""))? menu : switchMenu;
      switchMenu = "";

      close = true;
      removeViewer = false;
      TNE.menuManager().response.put(player.getUniqueId(), new ResponseData(chat, toGo));
    }

    if(close) {
      Bukkit.getScheduler().runTask(TNE.instance(), player::closeInventory);

      if(removeViewer) {
        TNE.menuManager().removeData(IDFinder.getID(player));
      }
    }
    if(!switchMenu.equalsIgnoreCase("")) {
      TNE.menuManager().open(switchMenu, player);
    }
    if(!message.equalsIgnoreCase("")) {
      Message m = new Message(message);
      m.translate(player.getWorld().getName(), player);
    }

    if(clickAction != null) {
      clickAction.accept(new IconClick(this, menu, player));
    }

    if(data.size() > 0) {
      UUID id = IDFinder.getID(player);

      data.forEach((identifier, value)->TNE.menuManager().setViewerData(id, identifier, value));
    }
  }

  public Integer getSlot() {
    return slot;
  }

  public void setSlot(Integer slot) {
    this.slot = slot;
  }

  public String getNode() {
    return node;
  }

  public void setNode(String node) {
    this.node = node;
  }

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public short getDamage() {
    return damage;
  }

  public void setDamage(short damage) {
    this.damage = damage;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public List<String> getLore() {
    return lore;
  }

  public void setLore(List<String> lore) {
    this.lore = lore;
  }

  public boolean isClose() {
    return close;
  }

  public void setClose(boolean close) {
    this.close = close;
    if(this.close) removeViewer = true;
  }
}