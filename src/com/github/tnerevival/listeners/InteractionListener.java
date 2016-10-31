package com.github.tnerevival.listeners;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.configurations.impl.ObjectConfiguration;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.event.object.InteractionType;
import com.github.tnerevival.core.event.object.TNEObjectInteractionEvent;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.ShopSign;
import com.github.tnerevival.core.signs.SignType;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.SignUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.Skeleton.SkeletonType;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InteractionListener implements Listener {

  TNE plugin;

  public InteractionListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent event) {
    if(TNE.configurations.getBoolean("Objects.Commands.Enabled", "objects")) {

      ObjectConfiguration configuration = TNE.configurations.getObjectConfiguration();

      Player player = event.getPlayer();
      String command = event.getMessage().substring(1);
      String[] commandSplit = command.split(" ");
      String commandName = commandSplit[0];
      String commandFirstArg = commandSplit[0] + ((commandSplit.length > 1) ? " " + commandSplit[1] : "");
      double cost = configuration.getCommandCost(commandName.toLowerCase(), (commandSplit.length > 1) ? new String[] { commandSplit[1].toLowerCase() } : new String[0], MISCUtils.getWorld(player), MISCUtils.getID(player).toString());

      Message commandCost = new Message("Messages.Command.Charge");
      commandCost.addVariable("$amount", CurrencyFormatter.format(MISCUtils.getWorld(event.getPlayer()), AccountUtils.round(cost)));
      commandCost.addVariable("$command", commandFirstArg);

      if(cost > 0.0) {
        String message = "";
        Account acc = AccountUtils.getAccount(MISCUtils.getID(player));
        if(TNE.instance.manager.enabled(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
          if(!TNE.instance.manager.confirmed(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
            if (acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE"))
              message = "Messages.Account.Set";
            else if (!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE"))
              message = "Messages.Account.Confirm";
          }
        }

        if(!message.equals("")) {
          event.setCancelled(true);
          new Message(message).translate(MISCUtils.getWorld(player), player);
          return;
        }
        event.setCancelled(true);

        boolean paid = false;

        if(acc.hasCredit(commandFirstArg)) {
          acc.removeCredit(commandFirstArg);
        } else {
          if(TNE.instance.api.fundsHas(player, player.getWorld().getName(), cost)) {
            TNE.instance.api.fundsRemove(player, player.getWorld().getName(), cost);
            paid = true;
          }
        }

        if(paid) {
          if(!player.performCommand(command)) {
            acc.addCredit(commandFirstArg);
            return;
          }

          commandCost.translate(MISCUtils.getWorld(player), player);
        }
        return;
      }

      if(TNE.configurations.getBoolean("Objects.Commands.ZeroMessage", "objects")) {
        commandCost.translate(MISCUtils.getWorld(player), player);
      }
    }
  }

  @EventHandler
  public void onBreak(BlockBreakEvent event) {
    String name = event.getBlock().getType().name();

    if(event.getBlock().getType().equals(Material.WALL_SIGN) || event.getBlock().getType().equals(Material.SIGN_POST)) {
      if(SignUtils.validSign(event.getBlock().getLocation())) {
        SerializableLocation location = new SerializableLocation(event.getBlock().getLocation());
        TNESign sign = SignUtils.getSign(location);

        MISCUtils.debug(sign.toString() + "");
        if(!sign.onDestroy(event.getPlayer())) {
          event.setCancelled(true);
        } else {
          SignUtils.removeSign(location);
        }
        return;
      }
    }

    TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(event.getPlayer(), name, InteractionType.CRAFTING);
    Bukkit.getServer().getPluginManager().callEvent(e);

    if(e.isCancelled()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlace(BlockPlaceEvent event) {
    String name = event.getBlock().getType().name();

    if(event.getBlock().getType().equals(Material.WALL_SIGN) || event.getBlock().getType().equals(Material.SIGN_POST)) {
      if(SignUtils.validSign(event.getBlock().getLocation())) {
        MISCUtils.debug("Sign placed");
        return;
      }
    }

    TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(event.getPlayer(), name, InteractionType.PLACING);
    Bukkit.getServer().getPluginManager().callEvent(e);

    if(e.isCancelled()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onSmelt(FurnaceSmeltEvent event) {
    if(event.getResult() != null && !event.getResult().getType().equals(Material.AIR)) {
      String name = event.getBlock().getType().name();
      if(event.getBlock().getState() instanceof Furnace) {
        Furnace f = (Furnace)event.getBlock().getState();

        int amount = (f.getInventory().getResult() != null) ? f.getInventory().getResult().getAmount() : 1;

        Double cost = InteractionType.SMELTING.getCost(name, event.getBlock().getWorld().toString(), "") * amount;

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

  @EventHandler
  public void onEnchant(EnchantItemEvent event) {
    if(event.getItem() != null && !event.getItem().getType().equals(Material.AIR)) {

      ItemStack result = event.getItem();
      String name = result.getType().name();
      Double cost = InteractionType.ENCHANT.getCost(name, MISCUtils.getWorld(event.getEnchanter()), MISCUtils.getID(event.getEnchanter()).toString());

      List<String> lore = new ArrayList<>();
      lore.add(ChatColor.WHITE + "Enchanting Cost: " + ChatColor.GOLD + cost);

      ItemMeta meta = result.getItemMeta();
      meta.setLore(lore);

      for(Enchantment e : event.getEnchantsToAdd().keySet()) {
        meta.addEnchant(e, event.getEnchantsToAdd().get(e), false);
      }

      result.setItemMeta(meta);
      event.getInventory().setItem(0, result);
    }
  }

  @EventHandler
  public void onPreCraft(PrepareItemCraftEvent event) {
    if(event.getInventory().getResult() != null) {
      Player player = (Player)event.getView().getPlayer();

      if (TNE.instance.api.getBoolean("Materials.Enabled", MISCUtils.getWorld(player), MISCUtils.getID(player))) {
        String name = event.getInventory().getResult().getType().name();
        Double cost = InteractionType.CRAFTING.getCost(name, MISCUtils.getWorld(player), MISCUtils.getID(player).toString());

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
  public void onCraft(CraftItemEvent event) {

    Player player = (Player) event.getWhoClicked();

    if (TNE.instance.api.getBoolean("Materials.Enabled", MISCUtils.getWorld(player), MISCUtils.getID(player))) {

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

      TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(player, name, InteractionType.CRAFTING);
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
  public void onChange(SignChangeEvent event) {
    if(event.getLine(0).contains("tne:")) {
      String[] match = event.getLine(0).substring(1, event.getLine(0).length() - 1).split(":");
      Player player = event.getPlayer();

      if (match.length > 1) {
        MISCUtils.debug(match[0] + " type: " + match[1]);
        SignType type = SignType.fromName(match[1]);

        TNESign sign = SignUtils.instance(type.getName(), MISCUtils.getID(event.getPlayer()));
        sign.setLocation(new SerializableLocation(event.getBlock().getLocation()));

        if(sign instanceof ShopSign) {
          if(!Shop.exists(event.getLine(1), event.getBlock().getWorld().getName())) {
            event.setCancelled(true);
            return;
          }
          ((ShopSign) sign).setName(event.getLine(1), event.getBlock().getWorld().getName());
        }

        if (!sign.onCreate(event.getPlayer())) {
          event.setCancelled(true);
        } else {
          Double place = sign.getType().place(MISCUtils.getWorld(event.getPlayer()), MISCUtils.getID(event.getPlayer()).toString());
          MISCUtils.debug("Interaction " + place);
          MISCUtils.debug("Interaction " + sign.getType().name());
          if(place != null && place > 0.0) {
            AccountUtils.transaction(MISCUtils.getID(event.getPlayer()).toString(), null, place, TransactionType.MONEY_REMOVE, MISCUtils.getWorld(event.getPlayer()));
            Message charged = new Message("Messages.Objects.SignPlace");
            charged.addVariable("$amount", CurrencyFormatter.format(MISCUtils.getWorld(event.getPlayer()), place));
            charged.translate(MISCUtils.getWorld(player), player);
          }
          TNE.instance.manager.signs.put(sign.getLocation(), sign);
        }
      }
    }
  }

  @EventHandler
  public void onInteractWithEntity(PlayerInteractEntityEvent event) {
    Entity entity = event.getRightClicked();
    Player player = event.getPlayer();
    String world = TNE.instance.defaultWorld;

    if(MISCUtils.multiWorld()) {
      world = player.getWorld().getName();
    }

    if(entity instanceof Villager) {
      Villager villager = (Villager)entity;

      if(player.getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG) && !player.hasPermission("tne.bypass.nametag")) {
        event.setCancelled(true);
        new Message("Messages.Mob.NPCTag").translate(MISCUtils.getWorld(player), player);
      }

      if(villager.getCustomName() != null && villager.getCustomName().equalsIgnoreCase("banker")) {
        event.setCancelled(true);
        if(player.hasPermission("tne.bank.use")) {
          if(BankUtils.enabled(world, MISCUtils.getID(player).toString())) {
            if(BankUtils.npc(world)) {
              if(BankUtils.hasBank(MISCUtils.getID(player))) {
                Inventory bankInventory = BankUtils.getBankInventory(MISCUtils.getID(player));
                player.openInventory(bankInventory);
              } else {
                new Message("Messages.Bank.None").translate(MISCUtils.getWorld(player), player);
              }
            } else {
              new Message("Messages.Bank.NoNPC").translate(MISCUtils.getWorld(player), player);
            }
          } else {
            new Message("Messages.Bank.Disabled").translate(MISCUtils.getWorld(player), player);
          }
        } else {
          new Message("Messages.General.NoPerm").translate(MISCUtils.getWorld(player), player);
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onRightClick(PlayerInteractEvent event) {
    Action action = event.getAction();
    Player player = event.getPlayer();
    String world = player.getWorld().getName();
    Block block = event.getClickedBlock();
    MISCUtils.debug(TNE.instance.manager.signs.size() + "");

    if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
      if(action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.WALL_SIGN) || action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.SIGN_POST)) {
        if(SignUtils.validSign(block.getLocation())) {
          SerializableLocation location = new SerializableLocation(block.getLocation());
          Sign b = (Sign)block.getState();
          TNESign sign = SignUtils.getSign(location);

          for(TNESign s : TNE.instance.manager.signs.values()) {
            MISCUtils.debug(s.getLocation().toString() + ";" + s.getType() + ";" + s.getOwner());
          }
          MISCUtils.debug(SignUtils.validSign(block.getLocation()) + "");
          MISCUtils.debug(SignUtils.getSign(location).toString() + "");
          if(sign == null) {
            MISCUtils.debug("Sign instance is null");
          }

          if(sign instanceof ShopSign) {
            if(!((ShopSign)sign).onRightClick(player, b.getLine(1), b.getWorld().getName())) {
              event.setCancelled(true);
            }
          } else{
            if (!sign.onRightClick(player)) {
              event.setCancelled(true);
            }
          }
          if(!event.isCancelled()) {
            Double use = sign.getType().use(MISCUtils.getWorld(event.getPlayer()), MISCUtils.getID(event.getPlayer()).toString());
            AccountUtils.transaction(MISCUtils.getID(event.getPlayer()).toString(), null, use, TransactionType.MONEY_REMOVE, MISCUtils.getWorld(event.getPlayer()));
            Message charged = new Message("Messages.Objects.SignUse");
            charged.addVariable("$amount", CurrencyFormatter.format(MISCUtils.getWorld(event.getPlayer()), use));
            charged.translate(MISCUtils.getWorld(player), player);
          }
        }
      } else {
        String name = event.getMaterial().name();
        TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(player, name, InteractionType.CRAFTING);
        Bukkit.getServer().getPluginManager().callEvent(e);

        if(e.isCancelled()) {
          event.setCancelled(true);
          return;
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityDeath(EntityDeathEvent event) {
    LivingEntity entity = event.getEntity();

    if(entity.getKiller() != null) {
      Player killer = entity.getKiller();
      String mob = entity.getCustomName();
      Double reward = TNE.configurations.mobReward("Default");
      String messageNode = "Messages.Mob.Killed";
      Boolean player = false;

      if((TNE.configurations.getBoolean("Mobs.Enabled", "mob"))) {
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
          case ENDER_DRAGON:
            mob = "EnderDragon";
            break;
          case ENDERMAN:
            mob = "Enderman";
            break;
          case ENDERMITE:
            mob = "Endermite";
            break;
          case GHAST:
            mob = "Ghast";
            break;
          case GIANT:
            mob = "Giant";
            break;
          case GUARDIAN:
            Guardian guard = (Guardian)entity;
            if(guard.isElder()) {
              mob = "GuardianElder";
              break;
            }
            mob = "Guardian";
            break;
          case HORSE:
            mob = "Horse";
            break;
          case IRON_GOLEM:
            mob = "IronGolem";
            break;
          case MAGMA_CUBE:
            mob = "MagmaCube";
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
              if (TNE.configurations.playerEnabled(p.getUniqueId())) {
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
            Skeleton skelly = (Skeleton)entity;
            if(skelly.getSkeletonType().equals(SkeletonType.WITHER)) {
              mob = "WitherSkeleton";
              break;
            }  else if(MISCUtils.isOneTen() && skelly.getSkeletonType().equals(SkeletonType.STRAY)) {
              mob = "Stray";
              break;
            }
            mob = "Skeleton";
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
          case VILLAGER:
            mob = "Villager";
            break;
          case WITCH:
            mob = "Witch";
            break;
          case WITHER:
            mob = "Wither";
            break;
          case WOLF:
            mob = "Wolf";
            break;
          case ZOMBIE:
            Zombie zombles = (Zombie)entity;
            if(zombles.isVillager()) {
              mob = "ZombieVillager";
              break;
            }
            if(MISCUtils.isOneTen() && zombles.getVillagerProfession().equals(Villager.Profession.HUSK)) {
              mob = "Husk";
              break;
            }
            mob = "Zombie";
            break;
          default:
            mob = "Default";
            break;
        }
        mob = (mob.equalsIgnoreCase("Default")) ? (entity.getCustomName() != null) ? entity.getCustomName() : mob : mob;
        Character firstChar = mob.charAt(0);
        reward = (player)? TNE.configurations.playerReward(mob) : TNE.configurations.mobReward(mob);
        messageNode = (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u') ? "Messages.Mob.KilledVowel" : "Messages.Mob.Killed";
        if(TNE.configurations.mobEnabled(mob)) {
          AccountUtils.transaction(MISCUtils.getID(killer).toString(), null, reward, TransactionType.MONEY_GIVE, MISCUtils.getWorld(killer));
          if(TNE.instance.api.getBoolean("Mobs.Message")) {
            Message mobKilled = new Message(messageNode);
            mobKilled.addVariable("$mob", mob);
            mobKilled.addVariable("$reward", CurrencyFormatter.format(MISCUtils.getWorld(killer), reward));
            mobKilled.translate(MISCUtils.getWorld(killer), killer);
          }
        }
      }
    }
  }
}