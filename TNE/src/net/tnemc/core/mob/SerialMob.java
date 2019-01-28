package net.tnemc.core.mob;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;

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
public class SerialMob {

  private String name;
  private String type;
  private boolean adult;
  private SerialMobData data;

  private LivingEntity entity;

  public SerialMob(LivingEntity entity) {
    this.entity = entity;
    this.name = entity.getCustomName();
    this.type = entity.getType().name();

    if(entity instanceof Ageable) {
      adult = ((Ageable) entity).isAdult();
    }
    buildData(entity);
  }

  private void buildData(LivingEntity entity) {

  }
}