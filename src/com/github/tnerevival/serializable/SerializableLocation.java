package com.github.tnerevival.serializable;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializableLocation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private double x,y,z;
    private String world;
    public SerializableLocation(Location loc) {
        x=loc.getX();
        y=loc.getY();
        z=loc.getZ();
        world=loc.getWorld().getName();
    }
    public Location getLocation() {
        World w = Bukkit.getWorld(world);
        if(w==null)
            return null;
        Location toRet = new Location(w,x,y,z);
        return toRet;
    }
}