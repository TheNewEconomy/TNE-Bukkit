package net.tnemc.core.common.menu.icon;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.menu.MenuClickType;
import net.tnemc.core.common.menu.consumable.menu.icon.IconBuild;
import net.tnemc.core.common.menu.consumable.menu.icon.IconClick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/3/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class IconType {

  //Visual variables
  private ItemStack stack;
  private String display = "";
  private List<String> lore = new ArrayList<>();

  //action variables
  private String message = "";
  private String switchMenu = "";
  private String node = "";
  private boolean close = false;
  private boolean removeViewerData = false;
  private ChatResponse response;

  //Consumers
  private Consumer<IconBuild> onBuild;
  private Consumer<IconClick> onClick;

  public IconType() {
  }

  public IconType(ItemStack stack) {
    this.stack = stack;
  }

  public IconType withStack(ItemStack stack) {
    this.stack = stack;
    return this;
  }

  public IconType display(String display) {
    this.display = display;
    return this;
  }

  public IconType node(String node) {
    this.node = node;
    return this;
  }

  public IconType message(String message) {
    this.message = message;
    return this;
  }

  public IconType withSwitch(String menu) {
    this.switchMenu = menu;
    return this;
  }

  public IconType close() {
    this.close = true;
    return this;
  }

  public IconType removeData() {
    this.removeViewerData = true;
    return this;
  }

  public IconType collectChat(ChatResponse response) {
    this.response = response;
    return this;
  }

  public IconType onBuild(Consumer<IconBuild> build) {
    this.onBuild = build;
    return this;
  }

  public IconType onClick(Consumer<IconClick> click) {
    this.onClick = click;
    return this;
  }

  public ItemStack build(String menu, Player player) {
    ItemStack icon = stack.clone();
    ItemMeta meta = icon.getItemMeta();

    if(meta == null) {
      meta = Bukkit.getServer().getItemFactory().getItemMeta(icon.getType());
    }

    if(meta != null) {

      if (!display.trim().equalsIgnoreCase("")) {
        meta.setDisplayName(display);
      }

      if (!lore.isEmpty()) {
        meta.setLore(lore);
      }
      icon.setItemMeta(meta);
    }

    if(onBuild != null) {
      onBuild.accept(new IconBuild(this, icon, menu));
    }
    return icon;
  }

  public boolean canClick(Player player) {
    if(node.trim().equalsIgnoreCase("")) return true;
    return player.hasPermission(node);
  }

  public void click(String menu, Player player, MenuClickType clickType) {
    if(!switchMenu.trim().equalsIgnoreCase("")) close = false;
    if(response != null) {
      if(response.getMenu().trim().equalsIgnoreCase("")) {
        response.setMenu(this.switchMenu);
      }

      close = true;
      removeViewerData = false;
      //TODO: add to manager.
    }

    if(close) {
      Bukkit.getScheduler().runTask(TNE.instance(), player::closeInventory);

      if(removeViewerData) {
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

    if(onClick != null) {
      onClick.accept(new IconClick(this, menu, player, clickType));
    }

    //TODO: Add data?
  }

  public ItemStack getStack() {
    return stack;
  }

  public Optional<String> getDisplay() {
    return Optional.of(display);
  }

  public List<String> getLore() {
    return lore;
  }

  public String getSwitchMenu() {
    return switchMenu;
  }

  public String getNode() {
    return node;
  }

  public boolean isClose() {
    return close;
  }

  public boolean isRemoveViewerData() {
    return removeViewerData;
  }
}