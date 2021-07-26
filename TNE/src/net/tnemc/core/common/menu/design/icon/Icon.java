package net.tnemc.core.common.menu.design.icon;

import net.tnemc.core.common.menu.ConvertableData;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Icon {
  Map<String, ConvertableData> apply = new HashMap<>();

  private int slot;
  private String type;
  private IconType typeInstance;
  private ItemStack item;


  public Optional<IconType> type() {
    return Optional.of(typeInstance);
  }
}