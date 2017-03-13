package com.github.tnerevival.listeners;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.configurations.impl.ObjectConfiguration;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.event.object.InteractionType;
import com.github.tnerevival.core.event.object.TNEObjectInteractionEvent;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.ItemSign;
import com.github.tnerevival.core.signs.ShopSign;
import com.github.tnerevival.core.signs.SignType;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Sign;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class InteractionListener implements Listener {

  TNE plugin;

  public InteractionListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onCommand(final PlayerCommandPreprocessEvent event) {
    if(TNE.instance().api().getBoolean("Objects.Commands.Enabled", "objects")) {

      ObjectConfiguration configuration = TNE.configurations.getObjectConfiguration();

      Player player = event.getPlayer();
      String command = event.getMessage().substring(1);
      String[] commandSplit = command.split(" ");
      String commandName = commandSplit[0];
      String commandFirstArg = commandSplit[0] + ((commandSplit.length > 1) ? " " + commandSplit[1] : "");
      BigDecimal cost = configuration.getCommandCost(commandName.toLowerCase(), (commandSplit.length > 1) ? new String[] { commandSplit[1].toLowerCase() } : new String[0], IDFinder.getWorld(player), IDFinder.getID(player).toString());

      Message commandCost = new Message("Messages.Command.Charge");
      commandCost.addVariable("$amount", CurrencyFormatter.format(IDFinder.getWorld(event.getPlayer()), cost));
      commandCost.addVariable("$command", commandFirstArg);

      if(cost.compareTo(BigDecimal.ZERO) > 0) {
        String message = "";
        Account acc = AccountUtils.getAccount(IDFinder.getID(player));
        if(TNE.instance().manager.enabled(IDFinder.getID(player), IDFinder.getWorld(player))) {
          if(!TNE.instance().manager.confirmed(IDFinder.getID(player), IDFinder.getWorld(player))) {
            if (acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE"))
              message = "Messages.Account.Set";
            else if (!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE"))
              message = "Messages.Account.Confirm";
          }
        }

        if(!message.equals("")) {
          event.setCancelled(true);
          new Message(message).translate(IDFinder.getWorld(player), player);
          return;
        }
        event.setCancelled(true);

        boolean paid = false;

        if(acc.hasCredit(commandFirstArg)) {
          acc.removeCredit(commandFirstArg);
        } else {
          if(TNE.instance().api().fundsHas(player.getUniqueId().toString(), player.getWorld().getName(), cost)) {
            TNE.instance().api().fundsRemove(player.getUniqueId().toString(), player.getWorld().getName(), cost);
            paid = true;
          }
        }

        if(paid) {
          if(!player.performCommand(command)) {
            acc.addCredit(commandFirstArg);
            return;
          }

          commandCost.translate(IDFinder.getWorld(player), player);
        }
        return;
      }

      if(TNE.configurations.getBoolean("Objects.Commands.ZeroMessage", "objects")) {
        commandCost.translate(IDFinder.getWorld(player), player);
      }
    }
  }

  @EventHandler
  public void onBreak(final BlockBreakEvent event) {
    String world = event.getBlock().getWorld().getName();
    String name = event.getBlock().getType().name();

    if(event.getBlock().getState() instanceof org.bukkit.block.Sign) {
      if(TNESign.validSign(event.getBlock().getLocation())) {
        SerializableLocation location = new SerializableLocation(event.getBlock().getLocation());
        TNESign sign = TNESign.getSign(location);
        MISCUtils.debug(sign.getType().getName());
        if(sign.onDestroy(event.getPlayer())) {
          TNESign.removeSign(location);
          return;
        }
        event.setCancelled(true);
        return;
      }
    }

    if(event.getBlock().getState() instanceof Chest || MISCUtils.isOneEight() && event.getBlock().getState() instanceof org.bukkit.block.EnderChest) {
      TNESign sign = TNESign.getOwningSign(event.getBlock().getLocation());
      if (sign != null) {
        MISCUtils.debug(event.getPlayer().hasPermission("tne.sign.admin") + "");
        if (!sign.getOwner().equals(IDFinder.getID(event.getPlayer())) && !event.getPlayer().hasPermission("tne.sign.admin")) {
          new Message("Messages.SignShop.UnableChest").translate(world, event.getPlayer());
          event.setCancelled(true);
        }
      }
    }
    Location attached = TNESign.getAttachedSign(event.getBlock().getLocation());
    if(attached != null && TNESign.validSign(attached)) {
      TNESign sign = TNESign.getSign(new SerializableLocation(attached));
      if(!sign.getOwner().equals(IDFinder.getID(event.getPlayer())) && !event.getPlayer().hasPermission("tne.sign.admin")) {
        new Message("Messages.General.NoPerm").translate(world, event.getPlayer());
        event.setCancelled(true);
      }
    }

    if(!event.isCancelled()) {
      TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(event.getPlayer(), new ItemStack(event.getBlock().getType()), name, InteractionType.CRAFTING);
      Bukkit.getServer().getPluginManager().callEvent(e);

      if (e.isCancelled()) {
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onPlace(final BlockPlaceEvent event) {
    String name = event.getBlock().getType().name();

    if(event.getBlock().getType().equals(Material.WALL_SIGN) || event.getBlock().getType().equals(Material.SIGN_POST)) {
      if(TNESign.validSign(event.getBlock().getLocation())) {
        MISCUtils.debug("Sign placed");
        return;
      }
    }

    TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(event.getPlayer(), new ItemStack(event.getBlock().getType()), name, InteractionType.PLACING);
    Bukkit.getServer().getPluginManager().callEvent(e);

    if(e.isCancelled()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onSmelt(final FurnaceSmeltEvent event) {
    if (TNE.instance().api().getBoolean("Materials.Enabled", TNE.instance().defaultWorld, "")) {
      if (event.getResult() != null && !event.getResult().getType().equals(Material.AIR)) {
        String name = event.getBlock().getType().name();
        if (event.getBlock().getState() instanceof Furnace) {
          Furnace f = (Furnace) event.getBlock().getState();

          int amount = (f.getInventory().getResult() != null) ? f.getInventory().getResult().getAmount() : 1;

          BigDecimal cost = InteractionType.SMELTING.getCost(name, event.getBlock().getWorld().toString(), "").multiply(new BigDecimal(amount));

          List<String> lore = new ArrayList<>();
          lore.add(ChatColor.WHITE + "Smelting Cost: " + ChatColor.GOLD + cost);

          ItemStack result = event.getResult();
          ItemMeta meta = result.getItemMeta();
          meta.setLore(lore);

          result.setItemMeta(meta);
          event.setResult(result);
        }
      }
    }
  }

  @EventHandler
  public void onEnchant(final EnchantItemEvent event) {
    if (TNE.instance().api().getBoolean("Materials.Enabled", IDFinder.getWorld(event.getEnchanter()), IDFinder.getID(event.getEnchanter()))) {
      if (event.getItem() != null && !event.getItem().getType().equals(Material.AIR)) {

        ItemStack result = event.getItem();
        String name = result.getType().name();
        BigDecimal cost = InteractionType.ENCHANT.getCost(name, IDFinder.getWorld(event.getEnchanter()), IDFinder.getID(event.getEnchanter()).toString());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Enchanting Cost: " + ChatColor.GOLD + cost);

        ItemMeta meta = result.getItemMeta();
        meta.setLore(lore);

        for (Enchantment e : event.getEnchantsToAdd().keySet()) {
          meta.addEnchant(e, event.getEnchantsToAdd().get(e), false);
        }

        result.setItemMeta(meta);
        event.getInventory().setItem(0, result);
      }
    }
  }

  @EventHandler
  public void onPreCraft(final PrepareItemCraftEvent event) {
    if(event.getInventory().getResult() != null) {
      Player player = (Player)event.getView().getPlayer();

      if (TNE.instance().api().getBoolean("Materials.Enabled", IDFinder.getWorld(player), IDFinder.getID(player))) {
        String name = event.getInventory().getResult().getType().name();
        BigDecimal cost = InteractionType.CRAFTING.getCost(name, IDFinder.getWorld(player), IDFinder.getID(player).toString());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Crafting Cost: " + ChatColor.GOLD + cost);

        ItemStack result = event.getInventory().getResult();
        ItemMeta meta = result.getItemMeta();
        meta.setLore(lore);
        result.setItemMeta(meta);
        event.getInventory().setResult(result);
      }
    }
  }

  @EventHandler
  public void onCraft(final CraftItemEvent event) {

    Player player = (Player) event.getWhoClicked();

    if (TNE.instance().api().getBoolean("Materials.Enabled", IDFinder.getWorld(player), IDFinder.getID(player))) {

      String name = event.getInventory().getResult().getType().name();
      ItemStack result = event.getCurrentItem().clone();
      ItemMeta meta = result.getItemMeta();
      List<String> newLore = new ArrayList<>();
      for(String s : meta.getLore()) {
        if(!s.contains("Crafting Cost")) {
          newLore.add(s);
        }
      }
      meta.setLore(newLore);
      result.setItemMeta(meta);

      TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(player, result, name, InteractionType.CRAFTING);
      Bukkit.getServer().getPluginManager().callEvent(e);

      if (e.isCancelled()) {
        event.setCancelled(true);
        return;
      }
      if(event.getClick().isShiftClick()) {
        final Player p = player;
        final ItemStack stack = result;
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
          @Override
          public void run() {
            ItemStack[] contents = p.getInventory().getContents().clone();
            for(int i = 0; i < contents.length; i++) {
              if(contents[i] != null && contents[i].getType().equals(stack.getType())) {
                ItemStack cloneStack = contents[i].clone();
                ItemMeta meta = cloneStack.getItemMeta();
                List<String> newLore = new ArrayList<>();
                if(meta.getLore() != null) {
                  for (String s : meta.getLore()) {
                    if (!s.contains("Crafting Cost")) {
                      newLore.add(s);
                    }
                  }
                }
                meta.setLore(newLore);
                cloneStack.setItemMeta(meta);
                contents[i] = cloneStack;
              }
            }
            p.getInventory().setContents(contents);
          }
        }, 5L);
      } else {
        event.setCurrentItem(result);
      }
    }
  }

  @EventHandler
  public void onChange(final SignChangeEvent event) {
    if(event.getLine(0).contains("[tne:") && event.getLine(0).contains("]")) {
      Player player = event.getPlayer();
      String world = event.getBlock().getWorld().getName();
      String line = event.getLine(0);
      String stripped = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
      String[] match = stripped.split(":");
      SignType type = SignType.fromName(match[1]);

      if (!type.equals(SignType.UNKNOWN)) {
        MISCUtils.debug(match[0] + " type: " + match[1]);
        MISCUtils.debug(type.toString());
        TNESign sign = TNESign.instance(type.getName(), IDFinder.getID(event.getPlayer()), new SerializableLocation(event.getBlock().getLocation()));

        if (sign instanceof ShopSign) {
          if (!Shop.exists(event.getLine(1), event.getBlock().getWorld().getName())) {
            event.setCancelled(true);
            return;
          }
          ((ShopSign) sign).setName(event.getLine(1), event.getBlock().getWorld().getName());
        }

        if (!sign.onCreate(event.getPlayer())) {
          event.setCancelled(true);
        } else {
          BigDecimal place = sign.getType().place(IDFinder.getWorld(event.getPlayer()), IDFinder.getID(event.getPlayer()).toString());
          MISCUtils.debug("Interaction " + place);
          MISCUtils.debug("Interaction " + sign.getType().name());
          if (place != null && place.compareTo(BigDecimal.ZERO) > 0) {
            AccountUtils.transaction(IDFinder.getID(event.getPlayer()).toString(), null, place, TransactionType.MONEY_REMOVE, IDFinder.getWorld(event.getPlayer()));
            Message charged = new Message("Messages.Objects.SignPlace");
            charged.addVariable("$amount", CurrencyFormatter.format(IDFinder.getWorld(event.getPlayer()), place));
            charged.translate(IDFinder.getWorld(player), player);
          }

          if(type.equals(SignType.ITEM)) {
            event.setLine(3, player.getName());
            Sign s = (Sign)event.getBlock().getState().getData();
            BlockFace face = (s.isWallSign())? s.getAttachedFace() : s.getFacing().getOppositeFace();
            Block b = event.getBlock().getRelative(face);
            MISCUtils.debug("Placed on: " + b.getType().toString());
            if(b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) {
              TNESign itemSign = TNESign.getSign((new SerializableLocation(b.getLocation())));
              if(TNESign.validSign(b.getLocation()) && itemSign.getType().equals(SignType.ITEM)) {
                ((ItemSign)itemSign).addOffer(player, event.getLines());
                event.getBlock().setType(Material.AIR);
              }
            } else {
              if(TNESign.getOwned(IDFinder.getID(player), SignType.ITEM) >= TNE.instance().api().getInteger("Core.Signs.Item.Max", world, IDFinder.getID(player))) {
                new Message("Messages.SignShop.Max").translate(world, player);
                return;
              }
              ((ItemSign)sign).addOffer(player, event.getLines());
              event.setLine(0, ChatColor.BLUE + event.getLine(0));
              TNE.instance().manager.signs.put(sign.getLocation(), sign);
            }
          } else {
            event.setLine(0, ChatColor.BLUE + event.getLine(0));
            TNE.instance().manager.signs.put(sign.getLocation(), sign);
          }
        }
      }
    }
  }

  @EventHandler
  public void onInteractWithEntity(final PlayerInteractEntityEvent event) {
    Entity entity = event.getRightClicked();
    Player player = event.getPlayer();
    String world = TNE.instance().defaultWorld;

    if(MISCUtils.multiWorld()) {
      world = player.getWorld().getName();
    }

    if(entity instanceof Villager) {
      Villager villager = (Villager)entity;

      if(player.getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG) && !player.hasPermission("tne.bypass.nametag")) {
        event.setCancelled(true);
        new Message("Messages.Mob.NPCTag").translate(IDFinder.getWorld(player), player);
      }

      if(villager.getCustomName() != null && villager.getCustomName().equalsIgnoreCase("vaultkeeper")) {
        event.setCancelled(true);
        if(player.hasPermission("tne.vault.use")) {
          if(Vault.enabled(world, IDFinder.getID(player).toString())) {
            if(Vault.npc(world)) {
              if(AccountUtils.getAccount(IDFinder.getID(player)).hasBank(IDFinder.getWorld(player))) {
                Inventory inventory = AccountUtils.getAccount(IDFinder.getID(player)).getVault(world).getInventory();
                player.openInventory(inventory);
              } else {
                Message none = new Message("Messages.Vault.None");
                none.addVariable("$amount",  CurrencyFormatter.format(player.getWorld().getName(), Vault.cost(player.getWorld().getName(), IDFinder.getID(player).toString())));
                none.translate(IDFinder.getWorld(player), player);
              }
            } else {
              new Message("Messages.Vault.NoNPC").translate(IDFinder.getWorld(player), player);
            }
          } else {
            new Message("Messages.Vault.Disabled").translate(IDFinder.getWorld(player), player);
          }
        } else {
          new Message("Messages.General.NoPerm").translate(IDFinder.getWorld(player), player);
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onClick(final PlayerInteractEvent event) {
    Action action = event.getAction();
    Player player = event.getPlayer();
    Block block = event.getClickedBlock();

    if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
      if(action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.WALL_SIGN) || action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.SIGN_POST)) {
        if(TNESign.validSign(block.getLocation())) {
          if(player.isSneaking()) return;
          SerializableLocation location = new SerializableLocation(block.getLocation());
          org.bukkit.block.Sign b = (org.bukkit.block.Sign)block.getState();
          TNESign sign = TNESign.getSign(location);

          for(TNESign s : TNE.instance().manager.signs.values()) {
            MISCUtils.debug(s.getLocation().toString() + ";" + s.getType() + ";" + s.getOwner());
          }
          MISCUtils.debug(TNESign.validSign(block.getLocation()) + "");
          MISCUtils.debug(TNESign.getSign(location).toString() + "");
          if(sign == null) MISCUtils.debug("Sign instance is null");

          if(sign instanceof ShopSign) {
            if(!((ShopSign)sign).onRightClick(player, b.getLine(1), b.getWorld().getName())) {
              event.setCancelled(true);
            }
          } else{
            if (!sign.onRightClick(player, player.isSneaking())) {
              event.setCancelled(true);
            }
          }
          if(!event.isCancelled()) {
            BigDecimal use = sign.getType().use(IDFinder.getWorld(event.getPlayer()), IDFinder.getID(event.getPlayer()).toString());
            AccountUtils.transaction(IDFinder.getID(event.getPlayer()).toString(), null, use, TransactionType.MONEY_REMOVE, IDFinder.getWorld(event.getPlayer()));
            Message charged = new Message("Messages.Objects.SignUse");
            charged.addVariable("$amount", CurrencyFormatter.format(IDFinder.getWorld(event.getPlayer()), use));
            charged.translate(IDFinder.getWorld(player), player);
          }
        }
      } else if(action.equals(Action.RIGHT_CLICK_BLOCK) && block.getState() instanceof Chest || action.equals(Action.RIGHT_CLICK_BLOCK) && MISCUtils.isOneEight() && block.getState() instanceof org.bukkit.block.EnderChest) {
        TNESign sign = TNESign.getOwningSign(block.getLocation());
        if(sign != null) {
          MISCUtils.debug(event.getPlayer().hasPermission("tne.sign.admin") + "");
          if(!sign.getOwner().equals(IDFinder.getID(event.getPlayer())) && !event.getPlayer().hasPermission("tne.sign.admin")) {
            new Message("Messages.SignShop.UnableChest").translate(block.getWorld().getName(), event.getPlayer());
            event.setCancelled(true);
            return;
          }
        }
      } else {
        String name = event.getMaterial().name();
        TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(player, event.getItem(), name, InteractionType.PLACING);
        Bukkit.getServer().getPluginManager().callEvent(e);

        if(e.isCancelled()) {
          event.setCancelled(true);
          return;
        }
      }
    } else if(action.equals(Action.LEFT_CLICK_BLOCK)) {
      if(action.equals(Action.LEFT_CLICK_BLOCK) && block.getType().equals(Material.WALL_SIGN) || action.equals(Action.LEFT_CLICK_BLOCK) && block.getType().equals(Material.SIGN_POST)) {
        if(TNESign.validSign(block.getLocation())) {
          SerializableLocation location = new SerializableLocation(block.getLocation());
          TNESign sign = TNESign.getSign(location);

          for(TNESign s : TNE.instance().manager.signs.values()) {
            MISCUtils.debug(s.getLocation().toString() + ";" + s.getType() + ";" + s.getOwner());
          }
          MISCUtils.debug(TNESign.validSign(block.getLocation()) + "");
          MISCUtils.debug(TNESign.getSign(location).toString() + "");
          if(sign == null) MISCUtils.debug("Sign instance is null");
          if (!sign.onClick(player, player.isSneaking())) {
            event.setCancelled(true);
          }
          if(!event.isCancelled()) {
            BigDecimal use = sign.getType().use(IDFinder.getWorld(event.getPlayer()), IDFinder.getID(event.getPlayer()).toString());
            AccountUtils.transaction(IDFinder.getID(event.getPlayer()).toString(), null, use, TransactionType.MONEY_REMOVE, IDFinder.getWorld(event.getPlayer()));
            Message charged = new Message("Messages.Objects.SignUse");
            charged.addVariable("$amount", CurrencyFormatter.format(IDFinder.getWorld(event.getPlayer()), use));
            charged.translate(IDFinder.getWorld(player), player);
          }
        }
      }
    }
  }

  @EventHandler
  public void onItemChange(final PlayerItemHeldEvent event) {
    Block b = (MISCUtils.isOneEight())? event.getPlayer().getTargetBlock(new HashSet<>(Arrays.asList(new Material[] { Material.AIR })), 5)
              : event.getPlayer().getTargetBlock(new HashSet<>(Arrays.asList(new Byte[] { 0 })), 5);

    if(b != null) {
      if(b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) {
        if(TNESign.validSign(b.getLocation())) {
          TNESign sign = TNESign.getSign(new SerializableLocation(b.getLocation()));
          if(sign.getType().equals(SignType.ITEM)) {
            int offer = ((ItemSign)sign).currentOffer;
            ((ItemSign)sign).setCurrentOffer((event.getNewSlot() > event.getPreviousSlot())? offer + 1 : offer - 1);
          }
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityDeath(final EntityDeathEvent event) {
    LivingEntity entity = event.getEntity();

    if(entity.getKiller() != null) {
      Player killer = entity.getKiller();
      String world = IDFinder.getWorld(killer);
      String id = IDFinder.getID(killer).toString();
      String mob = entity.getCustomName();
      BigDecimal reward = TNE.configurations.mobReward("Default", world, id);
      String messageNode = "Messages.Mob.Killed";
      Boolean player = false;

      if(TNE.instance().api().getBoolean("Mobs.Enabled", world, id)) {
        switch(entity.getType()) {
          case BAT:
            mob = "Bat";
            break;
          case BLAZE:
            mob = "Blaze";
            break;
          case CAVE_SPIDER:
            mob = "CaveSpider";
            break;
          case CHICKEN:
            mob = "Chicken";
            break;
          case COW:
            mob = "Cow";
            break;
          case CREEPER:
            mob = "Creeper";
            break;
          case DONKEY:
            mob = "Donkey";
            break;
          case ELDER_GUARDIAN:
            mob = "GuardianElder";
            break;
          case ENDER_DRAGON:
            mob = "EnderDragon";
            break;
          case ENDERMAN:
            mob = "Enderman";
            break;
          case ENDERMITE:
            mob = "Endermite";
            break;
          case EVOKER:
            mob = "Evoker";
            break;
          case GHAST:
            mob = "Ghast";
            break;
          case GIANT:
            mob = "Giant";
            break;
          case GUARDIAN:
            mob = "Guardian";
            break;
          case HORSE:
            mob = "Horse";
            break;
          case HUSK:
            mob = "Husk";
            break;
          case IRON_GOLEM:
            mob = "IronGolem";
            break;
          case LLAMA:
            mob = "Llama";
            break;
          case MAGMA_CUBE:
            mob = "MagmaCube";
            break;
          case MULE:
            mob = "Mule";
            break;
          case MUSHROOM_COW:
            mob = "Mooshroom";
            break;
          case OCELOT:
            mob = "Ocelot";
            break;
          case PIG:
            mob = "Pig";
            break;
          case PIG_ZOMBIE:
            mob = "ZombiePigman";
            break;
          case PLAYER:
            mob = "Player";
            Player p = (Player)entity;
            if(p.getUniqueId() != null) {
              if (TNE.configurations.playerEnabled(p.getUniqueId(), world, id)) {
                mob = p.getUniqueId().toString();
                player = true;
                break;
              }
            }
            mob = "Player";
            break;
          case POLAR_BEAR:
            mob = "PolarBear";
            break;
          case RABBIT:
            Rabbit rab = (Rabbit)entity;
            if(rab.getType().equals(Rabbit.Type.THE_KILLER_BUNNY)) {
              mob = "RabbitKiller";
              break;
            }
            mob = "Rabbit";
            break;
          case SHEEP:
            mob = "Sheep";
            break;
          case SHULKER:
            mob = "Shulker";
            break;
          case SILVERFISH:
            mob = "Silverfish";
            break;
          case SKELETON:
            mob = "Skeleton";
            break;
          case SKELETON_HORSE:
            mob = "SkeletonHorse";
            break;
          case SLIME:
            mob = "Slime";
            break;
          case SNOWMAN:
            mob = "SnowGolem";
            break;
          case SPIDER:
            mob = "Spider";
            break;
          case SQUID:
            mob = "Squid";
            break;
          case STRAY:
            mob = "Stray";
            break;
          case VEX:
            mob = "Vex";
            break;
          case VILLAGER:
            mob = "Villager";
            break;
          case VINDICATOR:
            mob = "Vindicator";
            break;
          case WITCH:
            mob = "Witch";
            break;
          case WITHER:
            mob = "Wither";
            break;
          case WITHER_SKELETON:
            mob = "WitherSkeleton";
            break;
          case WOLF:
            mob = "Wolf";
            break;
          case ZOMBIE:
            mob = "Zombie";
            break;
          case ZOMBIE_HORSE:
            mob = "ZombieHorse";
            break;
          case ZOMBIE_VILLAGER:
            mob = "ZombieVillager";
            break;
          default:
            mob = "Default";
            break;
        }
        mob = (mob.equalsIgnoreCase("Default") && event.getEntityType().toString() != null)? "Custom.Entries." + event.getEntityType().toString() : mob;

        if(entity.getCustomName() != null && TNE.configurations.mobEnabled(entity.getCustomName(), world, id)) mob = "Custom.Entries." + entity.getCustomName();

        if(TNE.configurations.mobAge(world, id)) {
          if (entity instanceof Ageable) {
            Ageable e = (Ageable) entity;
            if (!e.isAdult()) {
              mob = mob + ".Baby";
            }
          } else if(entity instanceof Zombie) {
            Zombie e = (Zombie)entity;
            if(e.isBaby()) {
              mob = mob + ".Baby";
            }
          }
        }
        if(entity.getType().equals(EntityType.SLIME)) {
          String tier = "Small";

          switch(((Slime)entity).getSize()) {
            case 1:
              break;
            case 2:
              tier = "Medium";
              break;
            case 4:
              tier = "Large";
              break;
            default:
              tier = ((Slime)entity).getSize() + "";
              break;
          }
          if(TNE.configurations.mobEnabled(mob + "." + tier, world, id)) {
            mob = mob + "." + tier;
          }
        }

        if(!TNE.instance().mobConfigurations.contains("Mobs." + mob)) mob = "Default";
        if(entity.getCustomName() != null && TNE.instance().mobConfigurations.contains("Mobs.Custom.Entries." + entity.getCustomName())) mob = "Custom.Entries." + entity.getCustomName();
        String currency = TNE.configurations.mobCurrency(mob, world, id);
        reward = (player)? TNE.configurations.playerReward(mob, world, id) : TNE.configurations.mobReward(mob, world, id);
        reward = AccountUtils.round(world, currency, reward.multiply(TNE.configurations.getRewardMultiplier(mob, world, id)));
        String formatted = (mob.equalsIgnoreCase("Default") && event.getEntityType().toString() != null)? event.getEntityType().toString() : mob;
        if(entity.getCustomName() != null && TNE.instance().mobConfigurations.contains("Mobs.Custom.Entries." + entity.getCustomName())) formatted = entity.getCustomName();
        formatted = (TNE.instance().messageConfigurations.contains("Messages.Mob.Custom." + formatted))? TNE.instance().messageConfigurations.getString("Messages.Mob.Custom." + formatted) : formatted;
        MISCUtils.debug(formatted);
        Character firstChar = formatted.charAt(0);
        messageNode = (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u') ? "Messages.Mob.KilledVowel" : "Messages.Mob.Killed";
        if(TNE.instance().messageConfigurations.contains("Messages.Mob.Custom." + formatted.replaceAll(" ", ""))) messageNode = TNE.instance().messageConfigurations.getString("Messages.Mob.Custom." + formatted.replaceAll(" ", ""));
        if(TNE.configurations.mobEnabled(mob, world, id)) {
          AccountUtils.transaction(IDFinder.getID(killer).toString(), null, reward, TNE.instance().manager.currencyManager.get(world, currency), TransactionType.MONEY_GIVE, IDFinder.getWorld(killer));
          if(TNE.instance().api().getBoolean("Mobs.Message")) {
            Message mobKilled = new Message(messageNode);
            mobKilled.addVariable("$mob", formatted.replace(".", " "));
            mobKilled.addVariable("$reward", CurrencyFormatter.format(IDFinder.getWorld(killer), currency, reward));
            mobKilled.translate(IDFinder.getWorld(killer), killer);
          }
        }
      }
    }
  }
}