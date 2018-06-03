package net.tnemc.signs;

import com.github.tnerevival.core.collection.EventMap;
import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.signs.listeners.SignsListener;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.ItemSign;
import net.tnemc.signs.signs.impl.NationSign;
import net.tnemc.signs.signs.impl.SafeSign;
import net.tnemc.signs.signs.impl.TownSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.Collection;
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

  private EventMap<SerializableLocation, TNESign> signs = new EventMap<>();
  private Map<String, SignType> signTypes = new HashMap<>();
  public static final Pattern signPattern = Pattern.compile("\\[(.*?)\\]");

  static final BlockFace[] connectedBlocks = new BlockFace[] {
    BlockFace.SOUTH, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST
  };

  public SignsManager() {
    signs.setListener(new SignsListener());

    addSignType(new SafeSign());
    addSignType(new ItemSign());
    if(Bukkit.getPluginManager().getPlugin("Towny") != null) {
      addSignType(new TownSign());
      addSignType(new NationSign());
    }
  }

  public EventMap<SerializableLocation, TNESign> getSigns() {
    return signs;
  }

  public void setSigns(EventMap<SerializableLocation, TNESign> signs) {
    this.signs = signs;
  }

  public static boolean blockAttachedSign(final Block block) {
    final Block above = block.getRelative(BlockFace.UP);
    if(above.getType() != null && above.getType().equals(Material.SIGN_POST) && validSign((Sign)above.getState())) {
      return true;
    }

    for(BlockFace face : connectedBlocks) {
      final Block facedBlock = block.getRelative(face);

      if(facedBlock.getType() != null && facedBlock.getType().equals(Material.WALL_SIGN)) {
        if(facedBlock.getState() != null && facedBlock.getState().getData() != null && facedBlock.getState().getData() instanceof org.bukkit.material.Sign) {
          final org.bukkit.material.Sign sign = (org.bukkit.material.Sign)facedBlock.getState().getData();
          return sign != null && validSign((Sign)facedBlock.getState());
        }
      }
    }
    return false;
  }

  public static boolean validSign(final Sign sign) {
    return validSign(sign.getLine(0));
  }

  public static boolean validSign(final String identifier) {
    return signPattern.matcher(identifier).matches();
  }

  public SignType getType(String identifier) {
    return signTypes.get(ChatColor.stripColor(identifier).replace("[", "").replace("]", ""));
  }

  public void saveSign(TNESign sign) {

  }

  public TNESign loadSign(Location location) {
    return null;
  }

  public Collection<TNESign> loadSigns() {
    return null;
  }

  public void deleteSign(Location location) {

  }

  public void addSignType(SignType type) {
    signTypes.put(type.name(), type);
  }

  public Map<String, SignType> getSignTypes() {
    return signTypes;
  }

  public void setSignTypes(Map<String, SignType> signTypes) {
    this.signTypes = signTypes;
  }
}