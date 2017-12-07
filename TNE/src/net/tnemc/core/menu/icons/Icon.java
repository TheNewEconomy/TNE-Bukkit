package net.tnemc.core.menu.icons;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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
 * Created by Daniel on 11/5/2017.
 */
public class Icon {

  protected Map<String, Object> data = new HashMap<>();

  protected Integer slot;
  protected Material material;
  protected short damage;
  protected String display;
  protected List<String> lore;

  protected String node;
  protected String switchMenu;
  protected String message;
  protected boolean close;

  public Icon(Integer slot, Material material, String display) {
    this(slot, material, display, (short)0, new ArrayList<>());
  }

  public Icon(Integer slot, Material material, String display, short damage) {
    this(slot, material, display, damage, new ArrayList<>());
  }

  public Icon(Integer slot, Material material, String display, short damage, List<String> lore) {
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

  public void onClick(String menu, Player player) {
    if(!switchMenu.equalsIgnoreCase("")) close = false;
    if(close) {
      player.closeInventory();
      TNE.menuManager().removeData(IDFinder.getID(player));
    }
    if(!switchMenu.equalsIgnoreCase("")) {
      TNE.menuManager().open(switchMenu, player);
    }
    if(!message.equalsIgnoreCase("")) {
      Message m = new Message(message);
      m.translate(player.getWorld().getName(), player);
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
  }
}