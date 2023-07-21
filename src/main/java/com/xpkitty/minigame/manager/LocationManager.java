// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.manager;

import com.xpkitty.minigame.instance.game.CornerType;
import org.bukkit.Location;

public class LocationManager {

    public static Location getSingleCornerFromTwo(Location corner1, Location corner2, CornerType cornerType) {
        Location location;
        int x;
        int y;
        int z;

        if(cornerType.equals(CornerType.LOWER)) {
            x= (int) Math.min(corner1.getX(), corner2.getX());
            y= (int) Math.min(corner1.getY(), corner2.getY());
            z= (int) Math.min(corner1.getZ(), corner2.getZ());
        } else {
            x= (int) Math.max(corner1.getX(), corner2.getX());
            y= (int) Math.max(corner1.getY(), corner2.getY());
            z= (int) Math.max(corner1.getZ(), corner2.getZ());
        }

        location=new Location(corner1.getWorld(),x,y,z);
        return location;
    }
}
