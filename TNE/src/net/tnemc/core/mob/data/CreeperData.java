package net.tnemc.core.mob.data;

import net.tnemc.core.item.JSONHelper;
import net.tnemc.core.mob.SerialMobData;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.json.simple.JSONObject;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 1/25/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CreeperData implements SerialMobData {

  boolean powered = false;

  @Override
  public SerialMobData initialize(LivingEntity entity) {
    powered = ((Creeper)entity).isPowered();
    return this;
  }

  @Override
  public LivingEntity build(LivingEntity entity) {
    ((Creeper)entity).setPowered(powered);
    return entity;
  }

  @Override
  public JSONObject toJSON() {
    return null;
  }

  @Override
  public void readJSON(JSONHelper json) {

  }
}