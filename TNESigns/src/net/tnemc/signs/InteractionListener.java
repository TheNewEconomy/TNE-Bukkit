package net.tnemc.signs;

import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.transaction.type.TransactionType;
import net.tnemc.signs.signs.ItemSign;
import net.tnemc.signs.signs.ShopSign;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.EnderChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.material.Sign;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

public class InteractionListener implements Listener {

  TNE plugin;

  public InteractionListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onBreak(final BlockBreakEvent event) {
    String world = event.getBlock().getWorld().getName();
    String name = event.getBlock().getType().name();

    if(event.getBlock().getState() instanceof org.bukkit.block.Sign) {
      if(TNESign.validSign(event.getBlock().getLocation())) {
        SerializableLocation location = new SerializableLocation(event.getBlock().getLocation());
        TNESign sign = TNESign.getSign(location);
        TNE.debug(sign.getType().getName());
        if(sign.onDestroy(event.getPlayer())) {
          TNESign.removeSign(location);
          return;
        }
        event.setCancelled(true);
        return;
      }
    }

    if(event.getBlock().getState() instanceof Chest || event.getBlock().getState() instanceof DoubleChest
       || MISCUtils.isOneTen() && event.getBlock().getState() instanceof EnderChest) {
      TNESign sign = TNESign.getOwningSign(event.getBlock().getLocation());
      if (sign != null) {
        if(sign.getType().equals(SignType.BANK) || sign.getType().equals(SignType.ITEM)) {
          TNE.debug(event.getPlayer().hasPermission("tne.sign.admin") + "");
          if (!sign.getOwner().equals(IDFinder.getID(event.getPlayer())) && !event.getPlayer().hasPermission("tne.sign.admin")) {
            new Message("Messages.Sign.UnableChest").translate(world, event.getPlayer());
            event.setCancelled(true);
          }
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
  }

  @EventHandler
  public void onPlace(final BlockPlaceEvent event) {
    String name = event.getBlock().getType().name();

    if(event.getBlock().getType().equals(Material.WALL_SIGN) || event.getBlock().getType().equals(Material.SIGN_POST)) {
      if(TNESign.validSign(event.getBlock().getLocation())) {
        TNE.debug("Sign placed");
        return;
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
        TNE.debug(match[0] + " type: " + match[1]);
        TNE.debug(type.toString());
        TNESign sign = TNESign.instance(type.getName(), IDFinder.getID(event.getPlayer()), new SerializableLocation(event.getBlock().getLocation()));

        if (!sign.onCreate(event.getPlayer())) {
          event.setCancelled(true);
        } else {
          BigDecimal place = sign.getType().place(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(event.getPlayer()).toString());
          TNE.debug("Interaction " + place);
          TNE.debug("Interaction " + sign.getType().name());
          if (place != null && place.compareTo(BigDecimal.ZERO) > 0) {
            AccountUtils.transaction(IDFinder.getID(event.getPlayer()).toString(), null, place, TransactionType.MONEY_REMOVE, IDFinder.getWorld(event.getPlayer()));
            Message charged = new Message("Messages.Objects.SignPlace");
            charged.addVariable("$amount", CurrencyFormatter.format(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), place));
            charged.translate(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), player);
          }

          if(type.equals(SignType.ITEM)) {
            event.setLine(3, player.getName());
            Sign s = (Sign)event.getBlock().getState().getData();
            BlockFace face = (s.isWallSign())? s.getAttachedFace() : s.getFacing().getOppositeFace();
            Block b = event.getBlock().getRelative(face);
            TNE.debug("Placed on: " + b.getType().toString());
            if(b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) {
              TNESign itemSign = TNESign.getSign((new SerializableLocation(b.getLocation())));
              if(TNESign.validSign(b.getLocation()) && itemSign.getType().equals(SignType.ITEM)) {
                ((ItemSign)itemSign).addOffer(player, event.getLines());
                event.getBlock().setType(Material.AIR);
              }
            } else {
              if(TNESign.getOwned(IDFinder.getID(player), SignType.ITEM) >= TNE.instance().api().getInteger("Signs.Item.Max", world, IDFinder.getID(player))) {
                new Message("Messages.Sign.Max").translate(world, player);
                return;
              }
              ((ItemSign)sign).addOffer(player, event.getLines());
              event.setLine(0, ChatColor.BLUE + event.getLine(0));
              SignsModule.manager().getSigns().put(sign.getLocation(), sign);
            }
          } else {
            event.setLine(0, ChatColor.BLUE + event.getLine(0));
            SignsModule.manager().getSigns().put(sign.getLocation(), sign);
          }
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

          for(TNESign s : SignsModule.manager().getSigns().values()) {
            TNE.debug(s.getLocation().toString() + ";" + s.getType() + ";" + s.getOwner());
          }
          TNE.debug(TNESign.validSign(block.getLocation()) + "");
          TNE.debug(TNESign.getSign(location).toString() + "");
          if(sign == null) TNE.debug("Sign instance is null");

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
            BigDecimal use = sign.getType().use(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(event.getPlayer()).toString());
            AccountUtils.transaction(IDFinder.getID(event.getPlayer()).toString(), null, use, TransactionType.MONEY_REMOVE, IDFinder.getWorld(event.getPlayer()));
            Message charged = new Message("Messages.Objects.SignUse");
            charged.addVariable("$amount", CurrencyFormatter.format(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), use));
            charged.translate(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), player);
          }
        }
      } else if(action.equals(Action.RIGHT_CLICK_BLOCK) && block.getState() instanceof Chest
                || action.equals(Action.RIGHT_CLICK_BLOCK) && block.getState() instanceof DoubleChest
                || action.equals(Action.RIGHT_CLICK_BLOCK) && MISCUtils.isOneTen()
                   && block.getState() instanceof EnderChest) {
        TNESign sign = TNESign.getOwningSign(block.getLocation());
        if(sign != null) {
          TNE.debug(event.getPlayer().hasPermission("tne.sign.admin") + "");
          if(!sign.getOwner().equals(IDFinder.getID(event.getPlayer())) && !event.getPlayer().hasPermission("tne.sign.admin")) {
            new Message("Messages.Sign.UnableChest").translate(block.getWorld().getName(), event.getPlayer());
            event.setCancelled(true);
            return;
          }
        }
      }
    } else if(action.equals(Action.LEFT_CLICK_BLOCK)) {
      if(action.equals(Action.LEFT_CLICK_BLOCK) && block.getType().equals(Material.WALL_SIGN) || action.equals(Action.LEFT_CLICK_BLOCK) && block.getType().equals(Material.SIGN_POST)) {
        if(TNESign.validSign(block.getLocation())) {
          SerializableLocation location = new SerializableLocation(block.getLocation());
          TNESign sign = TNESign.getSign(location);

          for(TNESign s : SignsModule.manager().getSigns().values()) {
            TNE.debug(s.getLocation().toString() + ";" + s.getType() + ";" + s.getOwner());
          }
          TNE.debug(TNESign.validSign(block.getLocation()) + "");
          TNE.debug(TNESign.getSign(location).toString() + "");
          if(sign == null) TNE.debug("Sign instance is null");
          if (!sign.onClick(player, player.isSneaking())) {
            event.setCancelled(true);
          }
          if(!event.isCancelled()) {
            BigDecimal use = sign.getType().use(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(event.getPlayer()).toString());
            AccountUtils.transaction(IDFinder.getID(event.getPlayer()).toString(), null, use, TransactionType.MONEY_REMOVE, IDFinder.getWorld(event.getPlayer()));
            Message charged = new Message("Messages.Objects.SignUse");
            charged.addVariable("$amount", CurrencyFormatter.format(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), use));
            charged.translate(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), player);
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
}