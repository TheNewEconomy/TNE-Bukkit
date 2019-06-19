package net.tnemc.signs.signs.impl.signal;

import net.tnemc.signs.signs.SignStep;
import org.bukkit.block.Sign;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/18/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CostSelectionStep implements SignStep {
  @Override
  public int step() {
    return 2;
  }

  @Override
  public boolean onSignInteract(Sign sign, UUID player, boolean rightClick, boolean shifting) {
    return false;
  }
}