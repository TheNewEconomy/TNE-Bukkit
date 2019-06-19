package net.tnemc.signs.selection;

import net.tnemc.signs.selection.impl.ChestSelection;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/19/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SelectionManager {

  private Map<String, Selection> selectionMap = new HashMap<>();

  private Map<UUID, String> selectors = new HashMap<>();

  public SelectionManager() {
    add(new ChestSelection());
  }

  public void add(Selection selection) {
    selectionMap.put(selection.name(), selection);
  }

  public void doSelection(String selection, UUID identifier, Location location, Object value) {
    if(selectionMap.containsKey(selection)) {
      selectionMap.get(selection).select(identifier, location, value);
    }
  }

  public void addPlayer(UUID id, String type) {
    selectors.put(id, type);
  }

  public boolean isSelecting(UUID id, String type) {
    return selectors.containsKey(id) && selectors.get(id).equalsIgnoreCase(type);
  }

  public void remove(UUID id) {
    selectors.remove(id);
  }
}