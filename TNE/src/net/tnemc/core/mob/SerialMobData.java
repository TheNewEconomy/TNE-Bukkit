package net.tnemc.core.mob;

import net.tnemc.core.item.JSONHelper;
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
public interface SerialMobData {

  SerialMobData initialize(LivingEntity entity);
  LivingEntity build(LivingEntity entity);
  JSONObject toJSON();
  void readJSON(JSONHelper json);
}