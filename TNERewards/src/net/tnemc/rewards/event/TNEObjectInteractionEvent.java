package net.tnemc.rewards.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

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
 * Created by Daniel on 10/21/2017.
 */
public class TNEObjectInteractionEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private boolean cancelled = false;

  private Player player;
  private ItemStack stack;
  private String identifier;
  private InteractionType type;
  private int amount = 1;

  public TNEObjectInteractionEvent(Player player, ItemStack stack, String identifier, InteractionType type) {
    this(player, stack, identifier, type, 1);
  }

  public TNEObjectInteractionEvent(Player player, ItemStack stack, String identifier, InteractionType type, int amount) {
    this.player = player;
    this.stack = stack;
    this.identifier = identifier;
    this.type = type;
    this.amount = amount;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public ItemStack getStack() {
    return stack;
  }

  public Player getPlayer() {
    return player;
  }

  public String getIdentifier() {
    return identifier;
  }

  public InteractionType getType() {
    return type;
  }

  public int getAmount() {
    return amount;
  }
}
