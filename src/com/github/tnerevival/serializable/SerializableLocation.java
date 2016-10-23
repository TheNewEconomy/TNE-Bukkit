package com.github.tnerevival.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

public class SerializableLocation implements Serializable {

  private static final long serialVersionUID = 1L;

  private double x, y, z;
  private String world;
    
    public SerializableLocation(Location location) {
        x = location.getX();
        y = location.getY();
        z = location.getZ();
        world = location.getWorld().getName();
    }

    public SerializableLocation(String world) {
      this(world, 0, 0, 0);
    }

    public SerializableLocation(String world, double x, double y, double z) {
      this.world = world;
      this.x = x;
      this.y = y;
      this.z = z;
    }
    
    public Location getLocation() {
        World w = Bukkit.getWorld(world);
        if(w == null) {
            return null;
        }

        return new Location(w, x, y, z);
    }

    @Override
    public String toString() {
      return x + ":" + y + ":" + z + ":" + world;
    }

    public static SerializableLocation fromString(String parse) {
      String[] parsed = parse.split(":");

      return new SerializableLocation(parsed[3], Double.valueOf(parsed[0]), Double.valueOf(parsed[1]), Double.valueOf(parsed[2]));
    }

    @Override
    public boolean equals(Object obj) {
      if(obj == null) return false;
      if(getClass() != obj.getClass()) return false;

      final SerializableLocation cerealLocation = (SerializableLocation)obj;

      if(!world.equals(cerealLocation.world)) return false;
      if(Double.compare(x, cerealLocation.x) != 0) return false;
      if(Double.compare(y, cerealLocation.y) != 0) return false;
      if(Double.compare(z, cerealLocation.z) != 0) return false;

      return true;
    }
}