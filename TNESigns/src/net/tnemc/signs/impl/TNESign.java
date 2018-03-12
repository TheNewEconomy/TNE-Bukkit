package net.tnemc.signs.impl;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.signs.event.SignEventAction;
import net.tnemc.signs.event.TNESignEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/13/2018.
 */
public abstract class TNESign {

  protected UUID owner;
  protected String type;
  protected String createPermission = "tne.sign.create";
  protected String usePermission = "tne.sign.use";
  protected Location location;

  protected SignChest chest = null;
  protected boolean requiresChest = false;

  public TNESign(UUID owner, String type, Location location, String createPermission, String usePermission) {
    this.owner = owner;
    this.type = type;
    this.location = location;
    this.createPermission = createPermission;
    this.usePermission = usePermission;
  }

  public String getLine(int line) {
    BlockState state = location.getBlock().getState();
    if(state instanceof Sign) {
      return ((Sign)state).getLine(line);
    }
    return "";
  }

  public static TNESign instance(String type, UUID owner, Location location) {
    switch(type) {
      case "item":
        return new ItemSign(owner, location);
      case "safe":
        return new SafeSign(owner, location);
      default:
        return null;
    }
  }

  /**
   * Called when a player attempts to create a TNE sign.
   * @param player
   * @return Whether or not the action was performed successfully.
   */
  public boolean onCreate(Player player) {
    if(!player.hasPermission(createPermission)) return false;
    return true;
  }

  /**
   * Called when a player attempts to destroy a TNE sign.
   * @param player
   * @return Whether or not the action was performed successfully.
   */
  public boolean onDestroy(Player player) {
    UUID id = IDFinder.getID(player);
    TNESignEvent event = new TNESignEvent(id, this, SignEventAction.DESTROYED);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if(!owner.equals(id) && !player.hasPermission("tne.override.sign")) return false;
    return (!event.isCancelled());
  }

  /**
   * Called when this sign is clicked on
   * @param player
   * @return Whether or not the action was performed successfully
   */
  public boolean onClick(Player player, ItemStack stack, boolean shift) {
    if(!player.hasPermission(usePermission)) return false;
    return true;
  }

  /**
   * Called when this sign is right clicked on
   * @param player
   * @return Whether or not the action was performed successfully
   */
  public boolean onRightClick(Player player, ItemStack stack, boolean shift) {
    if(!player.hasPermission(usePermission)) return false;
    return true;
  }

  /**
   * Called when the chest attached to this sign is attempted to be opened.
   * @param player
   * @return Whether or not the action was performed successfully
   */
  public boolean onChestOpenAttempt(Player player) {
    UUID id = IDFinder.getID(player);
    if(!id.equals(owner)) return false;
    return true;
  }
}