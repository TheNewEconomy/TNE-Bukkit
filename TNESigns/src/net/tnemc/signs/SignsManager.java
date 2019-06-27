package net.tnemc.signs;

import net.tnemc.signs.selection.SelectionManager;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.impl.CommandSign;
import net.tnemc.signs.signs.impl.ItemSign;
import net.tnemc.signs.signs.impl.NationItemSign;
import net.tnemc.signs.signs.impl.SignalSign;
import net.tnemc.signs.signs.impl.TownItemSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/28/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SignsManager {

  private Map<String, SignType> signTypes = new HashMap<>();
  public static final Pattern signPattern = Pattern.compile("\\[(.*?)\\]");

  static final BlockFace[] connectedBlocks = new BlockFace[] {
    BlockFace.SOUTH, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST
  };

  private SelectionManager selectionManager;

  public SignsManager() {

    addSignType(new CommandSign());
    addSignType(new ItemSign());
    addSignType(new SignalSign());

    if(Bukkit.getPluginManager().isPluginEnabled("Towny")) {
      addSignType(new NationItemSign());
      addSignType(new TownItemSign());
    }

    selectionManager = new SelectionManager();
  }

  public static boolean blockAttachedSign(final Block block) {
    final Block above = block.getRelative(BlockFace.UP);
    if(above != null && above.getState() instanceof Sign && validSign((Sign)above.getState())) {
      return true;
    }

    for(BlockFace face : connectedBlocks) {
      final Block facedBlock = block.getRelative(face);

      if(facedBlock.getState() != null && facedBlock.getState().getData() != null && facedBlock.getState().getData() instanceof org.bukkit.material.Sign && ((org.bukkit.material.Sign) facedBlock.getState().getData()).isWallSign()) {
        final org.bukkit.material.Sign sign = (org.bukkit.material.Sign)facedBlock.getState().getData();
        return sign != null && validSign((Sign)facedBlock.getState());
      }
    }
    return false;
  }

  public static Sign getAttachedSign(final Block block) {
    final Block above = block.getRelative(BlockFace.UP);
    if(above != null && above.getState() instanceof Sign && validSign((Sign)above.getState())) {
      return (Sign)above.getState();
    }

    for(BlockFace face : connectedBlocks) {
      final Block facedBlock = block.getRelative(face);

      if(facedBlock.getState() != null && facedBlock.getState().getData() != null && facedBlock.getState().getData() instanceof org.bukkit.material.Sign && ((org.bukkit.material.Sign) facedBlock.getState().getData()).isWallSign()) {
        final Sign sign = (Sign)facedBlock.getState();
        if(sign != null) {
          return sign;
        }
      }
    }
    return null;
  }

  public static boolean validSign(final Sign sign) {
    return validSign(sign.getLine(0));
  }

  public static boolean validSign(final String identifier) {
    return signPattern.matcher(ChatColor.stripColor(identifier)).matches();
  }

  public SignType getType(String identifier) {
    return signTypes.get(ChatColor.stripColor(identifier).replace("[", "").replace("]", ""));
  }

  public void addSignType(SignType type) {
    signTypes.put(type.name(), type);
  }

  public Map<String, SignType> getSignTypes() {
    return signTypes;
  }

  public SelectionManager getSelectionManager() {
    return selectionManager;
  }

  public void setSignTypes(Map<String, SignType> signTypes) {
    this.signTypes = signTypes;
  }
}