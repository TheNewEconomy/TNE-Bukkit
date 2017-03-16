package com.github.tnerevival.core.shops;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Shop implements Serializable {
  private static final long serialVersionUID = 1L;

  private Map<UUID, ShopPermission> permissions = new HashMap<>();
  private List<UUID> shoppers = new ArrayList<>();
  private List<ShopEntry> items = new ArrayList<>();
  private List<ShareEntry> shares = new ArrayList<>();

  private UUID owner;
  private String world;
  private String name;
  private boolean hidden = false;
  private boolean admin = false;

  public Shop(String name, String world) {
    this.name = name;
    this.world = world;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public List<ShopEntry> getSale() {
    return items;
  }

  public void setSale(List<ShopEntry> sale) {
    this.items = sale;
  }
  public List<UUID> getShoppers() {
    return shoppers;
  }

  public void setShoppers(List<UUID> shoppers) {
    this.shoppers = shoppers;
  }

  public void addShopper(UUID shopper) {
    shoppers.add(shopper);
  }

  public void removeShopper(UUID shopper) {
    shoppers.remove(shopper);
  }

  public List<ShopEntry> getItems() {
    return items;
  }

  public void setItems(List<ShopEntry> items) {
    this.items = items;
  }

  public boolean blacklisted(UUID player) {
    if(permissions.containsKey(player)) return permissions.get(player).isBlacklisted();
    return false;
  }

  public void addBlacklist(UUID player) {
    ShopPermission permission = (permissions.containsKey(player))? permissions.get(player) : new ShopPermission(player);
    permission.setBlacklisted(true);
    permissions.put(player, permission);
  }

  public void removeBlacklist(UUID player) {
    ShopPermission permission = (permissions.containsKey(player))? permissions.get(player) : new ShopPermission(player);
    permission.setBlacklisted(false);
    permissions.put(player, permission);
  }

  public boolean whitelisted(UUID player) {
    if(permissions.containsKey(player)) return permissions.get(player).isWhitelisted();
    return false;
  }

  public void addWhitelist(UUID player) {
    ShopPermission permission = (permissions.containsKey(player))? permissions.get(player) : new ShopPermission(player);
    permission.setWhitelisted(true);
    permissions.put(player, permission);
  }

  public void removeWhitelist(UUID player) {
    ShopPermission permission = (permissions.containsKey(player))? permissions.get(player) : new ShopPermission(player);
    permission.setWhitelisted(false);
    permissions.put(player, permission);
  }

  public List<ShareEntry> getShares() {
    return shares;
  }

  public void setShares(List<ShareEntry> shares) {
    this.shares = shares;
  }

  public UUID getOwner() {
    return owner;
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }


  public ShopEntry getItem(int slot) {
    for(ShopEntry entry : this.items) {
      if(entry.getItem().getSlot() == slot) {
        return entry;
      }
    }
    return null;
  }

  public int getItem(ItemStack stack, BigDecimal cost) {
    return getItem(stack, cost, true);
  }

  public int getItem(ItemStack stack, BigDecimal cost, boolean buy) {
    return getItem(stack, cost, buy, null);
  }

  public int getItem(ItemStack stack, BigDecimal cost, boolean buy, ItemStack trade) {
    for(ShopEntry entry : items) {
      final ShopEntry temp = new ShopEntry(new SerializableItemStack(0, stack), cost, 0, buy, false, new SerializableItemStack(0, trade));
      if(entry.equals(temp)) {
        return entry.getItem().getSlot();
      }
    }
    return -1;
  }

  public boolean addItem(ShopEntry entry) {
    if(items.size() >= 27) { return false; }
    Material mat = Material.getMaterial(entry.getItem().getName());
    if(hasItem(entry.getItem().toItemStack(), entry.getCost(), entry.isBuy())) {
      if(getCost(mat).compareTo(BigDecimal.ZERO) < 0 && entry.getTrade() != null && getTrade(mat) != null && entry.getTrade().getName().equals(getTrade(mat)) || getCost(mat).compareTo(entry.getCost()) == 0) {
        return false;
      }
    }
    this.items.add(entry);
    return true;
  }

  public boolean removeItem(ItemStack stack, BigDecimal cost) {
    return removeItem(stack, cost, true);
  }

  public boolean removeItem(ItemStack stack, BigDecimal cost, boolean buy) {
    return removeItem(stack, cost, buy, null);
  }

  public boolean removeItem(ItemStack stack, BigDecimal cost, boolean buy, ItemStack trade) {
    Iterator<ShopEntry> i = items.iterator();
    final ShopEntry temp = new ShopEntry(new SerializableItemStack(0, stack), cost, 0, buy, false, new SerializableItemStack(0, trade));
    while(i.hasNext()) {
      ShopEntry entry = i.next();
      if(entry.equals(temp)) {
        i.remove();
        return true;
      }
    }
    return false;
  }

  public boolean hasItem(ItemStack stack, BigDecimal cost) {
    return hasItem(stack, cost, true);
  }

  public boolean hasItem(ItemStack stack, BigDecimal cost, boolean buy) {
    return hasItem(stack, cost, buy, null);
  }

  public boolean hasItem(ItemStack stack, BigDecimal cost, boolean buy, ItemStack trade) {
    for(ShopEntry entry : items) {
      if(entry.getItem().toItemStack().getType().equals(stack.getType())
          && entry.isBuy() == buy && entry.getCost().compareTo(cost) == 0) {
        if(trade != null && !trade.getType().equals(Material.AIR) && !entry.getTrade().toItemStack().equals(trade)) {
          return false;
        }
        return true;
      }
    }
    return false;
  }

  public void update() {
    for(UUID id : shoppers) {
      Inventory inv = getInventory(Shop.canModify(name, IDFinder.getPlayer(id.toString())));
      IDFinder.getPlayer(id.toString()).openInventory(inv);
    }
  }

  public Inventory getInventory(boolean modifier) {
    Inventory inventory = Bukkit.createInventory(null, getSlots(world, owner), ChatColor.GOLD + "[Shop]" + ChatColor.RESET + name);
    for(ShopEntry entry : items) {
      MISCUtils.debug("" + entry.isBuy());
      String prefix = ChatColor.GOLD + ((entry.isBuy())? "[Buy]" : "[Sell]") + ChatColor.WHITE;
      ItemStack stack = entry.getItem().toItemStack();
      List<String> lore = new ArrayList<>();
      String instruction = (entry.isBuy())? "Left Click to buy. Right Click to trade." : "Left Click to sell.";
      lore.add(ChatColor.WHITE + instruction);
      if(modifier) {
        lore.add(ChatColor.WHITE + "Shift Right Click to remove this item.");
      }

      String message = (entry.isBuy())? "Stock: " : "Limit: ";
      String stock = (entry.isUnlimited())? "---" : "" + ((entry.isBuy())? entry.getStock() : (entry.getMaxstock() - entry.getStock()));
      lore.add(ChatColor.WHITE + message + ChatColor.GOLD + stock);

      if(entry.getCost().compareTo(BigDecimal.ZERO) > 0 || entry.getCost().compareTo(BigDecimal.ZERO) <= 0 && entry.getTrade() == null ||
         entry.getCost().compareTo(BigDecimal.ZERO) <= 0 && entry.getTrade().toItemStack().getType().equals(Material.AIR)) {
        String cost = ChatColor.WHITE + ((entry.isBuy()) ? "Cost:" : "Receive:");
        lore.add(cost + " " + ChatColor.GOLD + entry.getCost());
      }

      if(entry.getTrade() != null && !entry.getTrade().toItemStack().getType().equals(Material.AIR)) {
        lore.add(ChatColor.WHITE + "Trade: " + entry.getTrade().getAmount() + entry.getTrade().getName());
      }

      ItemMeta meta = stack.getItemMeta();
      meta.setDisplayName(prefix + entry.getItem().getName());
      meta.setLore(lore);
      stack.setItemMeta(meta);

      MISCUtils.debug(entry.getItem().getName() + ":" + entry.getItem().getSlot());

      inventory.setItem(entry.getItem().getSlot(), stack);
    }
    return inventory;
  }

  public BigDecimal getCost(Material mat) {
    for(ShopEntry entry : items) {
      if(Material.getMaterial(entry.getItem().getName()).equals(mat)) {
        return entry.getCost();
      }
    }
    return BigDecimal.ZERO;
  }

  public String getTrade(Material mat) {
    for(ShopEntry entry : items) {
      if(Material.getMaterial(entry.getItem().getName()).equals(mat)) {
        return entry.getTrade().getName();
      }
    }
    return null;
  }

  public boolean hasPermission(UUID player) {
    return owner.equals(player);
  }

  public void handlePayment(BigDecimal amount) {
    BigDecimal split = amount;
    for(ShareEntry entry : shares) {
      BigDecimal pay = amount.multiply(new BigDecimal(entry.getPercent())).multiply(new BigDecimal(100.0)).divide(new BigDecimal(100.0));
      split = split.subtract(pay);
      AccountUtils.transaction(entry.getShareOwner().toString(), null, pay, TransactionType.MONEY_GIVE, getWorld());
    }
    AccountUtils.transaction(owner.toString(), null, split, TransactionType.MONEY_GIVE, getWorld());
  }

  private double totalSharePercent() {
    double total = 0.00;
    for(ShareEntry entry : shares) {
      total += entry.getPercent();
    }
    return total;
  }

  public void remove(int slot, int amount) {
    ShopEntry entry = getItem(slot);
    int stock = (entry.isBuy())? entry.getStock() - amount : entry.getStock() + amount;
    entry.setStock(stock);
  }

  public double canBeShared() {
    return (100.00 - totalSharePercent());
  }

  public void removeShares(UUID player) {
    Iterator<ShareEntry> i = shares.iterator();
    while(i.hasNext()) {
      ShareEntry entry = i.next();
      if(entry.getShareOwner().equals(player)) {
        i.remove();
      }
    }
  }

  public void addShares(ShareEntry entry) {
    shares.add(entry);
  }

  public String permissionToString(UUID id) {
    String value = "";
    if(permissions.containsKey(id)) {
      ShopPermission permission = permissions.get(id);
      if(permission.isWhitelisted()) value += "whitelist";
      if(permission.isBlacklisted()) {
        if(value.length() == 0) value += ",";
        value += "blacklist";
      }
    }
    return value;
  }

  public void permissionFromString(UUID id, String permission) {
    String[] parsed = permission.split(",");

    for(String s : parsed) {
      switch(s) {
        case "blacklist":
          addBlacklist(id);
          break;
        case "whitelist":
          addWhitelist(id);
          break;
      }
    }
  }


  public void listFromString(String parse, boolean blacklist) {
    String[] parsed = parse.split(",");

    for(String s : parsed) {
      if(IDFinder.isUUID(s)) {
        if (blacklist) {
          addBlacklist(UUID.fromString(s));
          continue;
        }
        addWhitelist(UUID.fromString(s));
      }
    }
  }

  public String sharesToString() {
    StringBuilder builder = new StringBuilder();

    for(ShareEntry entry : shares) {
      if(builder.length() > 0) builder.append(",");
      builder.append(entry.toString());
    }
    return builder.toString();
  }

  public void sharesFromString(String parse) {
    String[] parsed = parse.split(",");

    for(String s : parsed) {
      ShareEntry entry = ShareEntry.fromString(s);
      if(entry != null) {
        shares.add(ShareEntry.fromString(s));
      }
    }
  }

  public String itemsToString() {
    StringBuilder builder = new StringBuilder();

    for(ShopEntry entry : items) {
      if(builder.length() > 0) builder.append("=");
      builder.append(entry.toString());
    }

    return builder.toString();
  }

  public void itemsFromString(String parse) {
    MISCUtils.debug(parse);
    String[] parsed = parse.split("=");

    for(String s : parsed) {
      if(ShopEntry.fromString(s) != null) {
        items.add(ShopEntry.fromString(s));
      }
    }
  }

  public Map<UUID, ShopPermission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<UUID, ShopPermission> permissions) {
    this.permissions = permissions;
  }

  /*
     * Static methods
     */
  public static boolean exists(String name, String world) {
    return TNE.instance().manager.shops.containsKey(name + ":" + world);
  }

  public static Shop getShop(String name, String world) {
    if(exists(name, world)) {
      return TNE.instance().manager.shops.get(name + ":" + world);
    }
    return null;
  }

  public static boolean shares(String name, UUID player) {
    if(exists(name, IDFinder.getWorld(player))) {
      return getShop(name, IDFinder.getWorld(player)).shares(player);
    }
    return false;
  }

  public static boolean canView(String name, UUID id) {
    if(!TNE.instance().api().getBoolean("Core.Shops.Enabled", IDFinder.getWorld(id), id)) return false;
    if(exists(name, IDFinder.getWorld(id))) {
      Shop s = getShop(name, IDFinder.getWorld(id));
      if(s.isHidden() && !s.whitelisted(id)) return false;
      if(s.blacklisted(id)) return false;

      return true;
    }
    return false;
  }

  public static boolean canModify(String name, Player p) {
    if(exists(name, IDFinder.getWorld(p))) {
      Shop s = getShop(name, IDFinder.getWorld(p));
      return s.getOwner() == null && p.hasPermission("tne.shop.admin") ||
           s.getOwner() != null && s.getOwner().equals(IDFinder.getID(p)) ||
           s.isAdmin() && p.hasPermission("tne.shop.admin");
    }
    return false;
  }


  public boolean shares(UUID player) {
    for(ShareEntry entry : shares) {
      if(entry.getShareOwner().equals(player)) {
        return true;
      }
    }
    return false;
  }

  public static int getSlots(String world, UUID owner) {
    String name = (owner != null)? owner.toString() : "";

    int amount = 6;
    int config = TNE.instance().api().getInteger("Core.Shops.Rows", world, name);
    if(config >= 1 && config <= 6) amount = config;

    return amount * 9;
  }

  public static int amount(UUID id) {
    int amount = 1;
    for(Shop s : TNE.instance().manager.shops.values()) {
      if(!s.isAdmin() && s.getOwner().equals(id)) {
        amount++;
      }
    }
    return amount;
  }
}