package net.tnemc.signs.selection;

import net.tnemc.signs.selection.impl.ChestSelection;
import net.tnemc.signs.selection.impl.CommandSelection;
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

  private Map<UUID, SelectionPlayer> selectors = new HashMap<>();

  public SelectionManager() {
    add(new ChestSelection());
    add(new CommandSelection());
  }

  public void add(Selection selection) {
    selectionMap.put(selection.name(), selection);
  }

  public void doSelection(String selection, UUID identifier, Location location, Object value) {
    if(selectionMap.containsKey(selection)) {
      selectionMap.get(selection).select(identifier, location, value);
    }
  }

  public void addPlayer(UUID id, SelectionPlayer player) {
    selectors.put(id, player);
  }

  public SelectionPlayer getSelectionInstance(UUID id) {
    return selectors.get(id);
  }

  public boolean isSelecting(UUID id, String type) {
    return selectors.containsKey(id) && selectors.get(id).getType().equalsIgnoreCase(type);
  }

  public boolean isSelectingAny(UUID id) {
    return selectors.containsKey(id);
  }

  public void remove(UUID id) {
    selectors.remove(id);
  }
}