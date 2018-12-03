package net.tnemc.signs;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;

import static net.tnemc.signs.SignsManager.connectedBlocks;
import static net.tnemc.signs.SignsManager.getAttachedSign;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 6/4/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ChestHelper {

  private Chest chest;
  private Chest doubleChest;

  public ChestHelper(Chest chest) {
    this.chest = chest;
    this.doubleChest = getDouble(chest);
  }

  private Chest getDouble(Chest chest) {

    for(BlockFace face : connectedBlocks) {
      final Block facedBlock = chest.getBlock().getRelative(face);

      if(facedBlock.getType() != null && facedBlock.getType().equals(Material.CHEST)) {
        if(facedBlock.getState() != null && facedBlock.getState() instanceof Chest) {
          final Chest doubleChest = (Chest)facedBlock.getState();
          if(doubleChest != null) {
            return doubleChest;
          }
        }
      }
    }
    return null;
  }

  public boolean isDouble() {
    return doubleChest != null;
  }

  public Sign getSign() {
    Sign sign = getAttachedSign(chest.getBlock());
    if(sign != null) return sign;
    if(isDouble()) sign = getAttachedSign(doubleChest.getBlock());
    if(sign != null) return sign;
    return null;
  }

  public Chest getDoubleChest() {
    return doubleChest;
  }
}